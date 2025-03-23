package litheraa.view.calendar;

import litheraa.view.util.AspectRatioAdapter;
import litheraa.view.util.SizeStepAdapter;
import lombok.Getter;
import org.apache.commons.math3.util.Pair;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.*;
import java.util.List;
import java.util.function.BiConsumer;

public class AdjustablePanel extends JPanel implements AdjustableComponentInterface {
	@Getter
	private final LocalDate id;
	private final List<AdjustableComponentInterface> COMPONENTS;

	private AdjustablePanel(LocalDate id, List<AdjustableComponentInterface> components) {
		this.id = id;
		setLayout(new GridBagLayout());
		COMPONENTS = components;
		COMPONENTS.forEach(c -> add(c.setParent(this)));
	}

	private AdjustablePanel(LocalDate id, List<Pair<AdjustableComponentInterface, Object>> components, LayoutManager layout) {
		this.id = id;
		setLayout(layout);
		COMPONENTS = new LinkedList<>();
		components.stream()
				.peek(c -> COMPONENTS.add(c.getFirst()))
				.forEach((pair) ->
						Optional.ofNullable(pair.getSecond())
				.ifPresentOrElse(e ->
						add(pair.getFirst().setParent(this), pair.getSecond()),
				() -> add(pair.getFirst().setParent(getThis()))));
	}

	private AdjustablePanel getThis() {
		return this;
	}

	public void addSizeStepListener(DayPanelController controller) {
		Arrays.stream(getComponents())
				.filter(component -> component instanceof DayLabel || component instanceof LabelGroup)
				.findFirst()
				.ifPresent(component -> component.addComponentListener(new SizeStepAdapter(controller)));
	}

	public static DayPanelBuilder dayPanelbuilder(LocalDate date) {
		return new DayPanelBuilder(date);
	}

	public static AdjustablePanelBuilder adjustablePanelBuilder(LocalDate date) {
		return new AdjustablePanelBuilder(date);
	}

	@Override
	public void aspectRatioChanged(AspectRatioAdapter.AspectRatio ratio) {
		COMPONENTS.forEach(component -> component.aspectRatioChanged(ratio));
	}

	@Override
	public void sizeChanged(SizeStepAdapter.Step step) {
		COMPONENTS.forEach(component -> component.sizeChanged(step));
	}

	@Override
	public JComponent setParent(JComponent parent) {
		parent.add(this);
		return this;
	}

	public static class DayPanelBuilder {
		private final LocalDate ID;
		private final List<AdjustableComponentInterface> COMPONENTS = new LinkedList<>();

		private DayPanelBuilder(LocalDate id) {
			ID = id;
		}

		public DayPanelBuilder dayLabel() {
			COMPONENTS.add(new DayLabel(ID));
			return this;
		}

		public DayPanelBuilder labelGroup(int written, int goal) {
			COMPONENTS.add(new LabelGroup(written, goal));
			return this;
		}

		public DayPanelBuilder progressBar(int written, int goal) {
			COMPONENTS.add(new ProgressBar(written, goal));
			return this;
		}

		public AdjustablePanel build() {
			return new AdjustablePanel(ID, COMPONENTS);
		}
	}

	public static class AdjustablePanelBuilder {
		private LayoutManager layout = new FlowLayout();
		private final LocalDate ID;
		private final List<Pair<AdjustableComponentInterface, Object>> COMPONENTS = new LinkedList<>();

		private AdjustablePanelBuilder(LocalDate id) {
			ID = id;
		}

		public AdjustablePanelBuilder label(BiConsumer<JLabel, SizeStepAdapter.Step> biConsumer, JLabel... label) {
			Arrays.stream(label)
					.forEach(l -> COMPONENTS
							.add(new Pair<>(new AdjustableComponentContainer<>(l, biConsumer), null)));
			return this;
		}

		@SafeVarargs
		public final AdjustablePanelBuilder label(BiConsumer<JLabel, SizeStepAdapter.Step> biConsumer, Pair<JLabel, Object>... labelsAndConstraints) {
			Arrays.stream(labelsAndConstraints)
					.forEach(laC -> COMPONENTS
							.add(new Pair<>(new AdjustableComponentContainer<>(laC.getFirst(), biConsumer), laC.getSecond())));
			return this;
		}

		public AdjustablePanelBuilder layout(LayoutManager layout) {
			this.layout = layout;
			return this;
		}

		public AdjustablePanelBuilder sizeStepListener(DayPanelController controller, int componentNo) {
			COMPONENTS.get(componentNo).getFirst().setParent(null).addComponentListener(new SizeStepAdapter(controller));
			return this;
		}

		public AdjustablePanel build() {
			return new AdjustablePanel(ID, COMPONENTS, layout);
		}
	}
}
