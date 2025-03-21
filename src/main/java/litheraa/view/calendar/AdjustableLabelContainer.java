package litheraa.view.calendar;

import litheraa.view.util.AspectRatioAdapter;
import litheraa.view.util.SizeStepAdapter;
import litheraa.view.util.fabric.FontLightWeight;

import javax.swing.*;
import java.util.function.BiConsumer;

public class AdjustableLabelContainer<T extends JLabel> implements AdjustableComponentInterface {
	private final T COMPONENT;
	private String uiClassId;
	private FontLightWeight FONT_FABRIC;
	private int SIZE_STEP;
	private BiConsumer<JLabel, SizeStepAdapter.Step> consumer;

	public AdjustableLabelContainer(T component, String UIClassID, FontLightWeight fontFabric, int sizeStep) {
		COMPONENT = component;
		uiClassId = UIClassID;
		FONT_FABRIC = fontFabric;
		SIZE_STEP = sizeStep;
	}

	public AdjustableLabelContainer(T component, BiConsumer<JLabel, SizeStepAdapter.Step> consumer) {
		COMPONENT = component;
		this.consumer = consumer;
	}

	@Override
	public void aspectRatioChanged(AspectRatioAdapter.AspectRatio ratio) {
	}

	@Override
	public void sizeChanged(SizeStepAdapter.Step step) {
		consumer.accept(COMPONENT, step);
	}

	@Override
	public void wireWithParent(JComponent parent) {
		parent.add(COMPONENT);
	}
}
