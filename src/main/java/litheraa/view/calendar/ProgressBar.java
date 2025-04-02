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
		setStringPainted(true);
		if (written <= goal) {
			setValue(written);
		} else {
			setString(calculatePercentageDifference(written, goal) + "%");
		}
	}

	private int calculatePercentageDifference(int v1, int v2) {
		double average = (v1 + v2) / 2.0;
		if (average == 0) {
			throw new IllegalArgumentException("The average of V1 and V2 cannot be zero.");
		}
		return (int) (Math.abs((v1 - v2) / average) * 100);
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
