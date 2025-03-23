package litheraa.view.calendar;

import litheraa.view.util.AspectRatioAdapter;
import litheraa.view.util.SizeStepAdapter;
import litheraa.view.util.fabric.ConstraintFactory;

import javax.swing.*;
import java.awt.*;

public class ProgressBar extends JProgressBar implements AdjustableComponentInterface{
	private final ConstraintFactory constraintFactory = ConstraintFactory.getInstance();
	private Container PARENT = getParent();

	public ProgressBar(int written, int goal) {

		setMaximum(goal);
		if (written < 0) {
			setValue(0);
		} else {
			setValue(written);
		}
		setStringPainted(true);
	}

	@Override
	public void aspectRatioChanged(AspectRatioAdapter.AspectRatio ratio) {
		if (ratio == AspectRatioAdapter.AspectRatio.HORIZONTAL) {
			setOrientation(JProgressBar.VERTICAL);
		} else setOrientation(JProgressBar.HORIZONTAL);
		PARENT.add(this, constraintFactory.getConstraints(getUIClassID(), ratio));
	}

	@Override
	public void sizeChanged(SizeStepAdapter.Step step) {

	}

	@Override
	public ProgressBar setParent(JComponent parent) {
		PARENT = parent;
		return this;
	}
}
