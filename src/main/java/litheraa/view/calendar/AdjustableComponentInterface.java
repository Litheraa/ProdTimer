package litheraa.view.calendar;

import litheraa.view.util.AspectRatioAdapter;
import litheraa.view.util.SizeStepAdapter;

import javax.swing.*;

public interface AdjustableComponentInterface {
	void aspectRatioChanged(AspectRatioAdapter.AspectRatio ratio);
	void sizeChanged(SizeStepAdapter.Step step);
	JComponent setParent(JComponent parent);
}
