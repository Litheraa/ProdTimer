package litheraa.view.calendar;

import litheraa.view.util.AspectRatioAdapter;
import litheraa.view.util.SizeStepAdapter;

import javax.swing.*;
import java.util.function.BiConsumer;

public class AdjustableComponentContainer<T extends JComponent> implements AdjustableComponentInterface {
	private final T COMPONENT;
	private final BiConsumer<T, SizeStepAdapter.Step> consumer;

	public AdjustableComponentContainer(T component, BiConsumer<T, SizeStepAdapter.Step> consumer) {
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
	public JComponent setParent(JComponent parent) {
		parent.add(COMPONENT);
		return COMPONENT;
	}
}
