package litheraa.view.calendar;

import litheraa.view.util.AspectRatioAdapter;
import litheraa.view.util.SizeStepAdapter;
import litheraa.view.util.fabric.IconFactory;

import javax.swing.*;

public class AdjustableIconContainer<T extends JLabel> implements AdjustableComponentInterface {
	private final T COMPONENT;
	private final String ICON_NAME;
	private final IconFactory ICON_FACTORY;

	public AdjustableIconContainer(T COMPONENT, String iconName, IconFactory iconFactory) {
		this.COMPONENT = COMPONENT;
		ICON_NAME = iconName;
		ICON_FACTORY = iconFactory;
	}

	@Override
	public void aspectRatioChanged(AspectRatioAdapter.AspectRatio ratio) {
	}

	@Override
	public void sizeChanged(SizeStepAdapter.Step step) {
		COMPONENT.setIcon(ICON_FACTORY.getIcon(ICON_NAME, step, 10));
	}

	@Override
	public void wireWithParent(JComponent parent) {
		parent.add(COMPONENT);
	}
}
