package litheraa.view.calendar;

import litheraa.controller.ViewController;
import litheraa.view.themes.ThemeColors;
import litheraa.view.util.AspectRatioAdapter;
import litheraa.view.util.SizeStepAdapter;
import litheraa.view.util.fabric.ConstraintFactory;
import litheraa.view.util.fabric.DimensionLightWeight;
import litheraa.view.util.fabric.FontLightWeight;
import litheraa.view.util.fabric.IconFactory;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;

public class DayPanel extends JPanel implements AdjustableComponentInterface{
	@Getter
	private final LocalDate ID;
	private final List<AdjustableComponentInterface> COMPONENTS;

	private DayPanel(LocalDate Id, List<AdjustableComponentInterface> components) {
		ID = Id;
		COMPONENTS = components;
		for (AdjustableComponentInterface component : components) {
			component.wireWithParent(this);
		}
		setLayout(new GridBagLayout());
	}

	public static DayPanelBuilder builder(LocalDate date,
	                                      FontLightWeight fontLightWeight,
	                                      ConstraintFactory constraintFactory,
	                                      DimensionLightWeight dimensionLightWeight,
	                                      IconFactory iconFactory) {
		return new DayPanelBuilder(date, fontLightWeight, constraintFactory, dimensionLightWeight, iconFactory);
	}

	public static DayPanelBuilder builder(LocalDate date, FontLightWeight fontLightWeight) {
		return new DayPanelBuilder(date, fontLightWeight, null, null, null);
	}

	@Override
	public void aspectRatioChanged(AspectRatioAdapter.AspectRatio ratio) {
		for (AdjustableComponentInterface component : COMPONENTS) {
			component.aspectRatioChanged(ratio);
		}
	}

	@Override
	public void sizeChanged(SizeStepAdapter.Step step) {
		for (AdjustableComponentInterface component : COMPONENTS) {
			component.sizeChanged(step);
		}
	}

	@Override
	public void wireWithParent(JComponent parent) {
		parent.add(this);
	}

	public static class DayPanelBuilder {
		private final LocalDate ID;
		private FontLightWeight fontLightWeight;
		private ConstraintFactory constraintFactory;
		private DimensionLightWeight dimensionLightWeight;
		private IconFactory iconFactory;
		private DayLabel dayLabel;
		private LabelGroup labelGroup;
		private final List<AdjustableComponentInterface> COMPONENTS = new LinkedList<>();

		private DayPanelBuilder(LocalDate id,
		                        FontLightWeight fontLightWeight,
		                        ConstraintFactory constraintFactory,
		                        DimensionLightWeight dimensionLightWeight,
		                        IconFactory iconFactory) {
			ID = id;
			this.fontLightWeight = fontLightWeight;
			this.constraintFactory = constraintFactory;
			this.dimensionLightWeight = dimensionLightWeight;
			this.iconFactory = iconFactory;
		}

		public DayPanelBuilder label(JLabel label, BiConsumer<JLabel, SizeStepAdapter.Step> biConsumer) {
			COMPONENTS.add(new AdjustableLabelContainer<>(label, biConsumer));
			return this;
		}

		public DayPanelBuilder dayName(String dayName) {
			JLabel label = new JLabel(dayName);
			label.setOpaque(true);
			label.setForeground(((ThemeColors) ViewController.getTheme()).getBackgroundDark());
			label.setBackground(((ThemeColors) ViewController.getTheme()).getForeground());
			COMPONENTS.add(new AdjustableLabelContainer<>(label,
					"dayName", fontLightWeight, 6));
			return this;
		}

		public DayPanelBuilder dayLabel() {
			dayLabel = new DayLabel(ID,
					constraintFactory,
					dimensionLightWeight,
					fontLightWeight);
			COMPONENTS.add(dayLabel);
			return this;
		}

		public DayPanelBuilder labelGroup(int written, int goal) {
			labelGroup = new LabelGroup(written, goal,
					constraintFactory,
					fontLightWeight,
					iconFactory);
			COMPONENTS.add(labelGroup);
			return this;
		}

		public DayPanelBuilder progressBar(int written, int goal) {
			COMPONENTS.add(new ProgressBar(written, goal, constraintFactory));
			return this;
		}

		public DayPanelBuilder sizeStepListener(DayPanelController controller) {
			if (labelGroup != null) {
				labelGroup.addComponentListener(new SizeStepAdapter(controller));
			} else if (dayLabel != null) {
				dayLabel.addComponentListener(new SizeStepAdapter(controller));
			}
			return this;
		}

		public DayPanel build() {
			return new DayPanel(ID, COMPONENTS);
		}

	}
}
