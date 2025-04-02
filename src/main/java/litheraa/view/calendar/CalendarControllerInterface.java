package litheraa.view.calendar;

import litheraa.controller.ViewController;
import litheraa.view.util.AspectRatioAdapter;
import litheraa.view.util.SizeStepAdapter;

import javax.swing.*;
import java.time.LocalDate;

public interface CalendarControllerInterface extends AdjustableComponentInterface {
	JPanel build();

	@Override
	void aspectRatioChanged(AspectRatioAdapter.AspectRatio ratio);

	@Override
	void sizeChanged(SizeStepAdapter.Step step);

	@Override
	JComponent setParent(JComponent parent);

	void setGoal(int goal, Long timeId);

	void setView(ViewController.CalendarType type);

	void setTextId(String text);

	LocalDate getStart();

	LocalDate getEnd();

	int getRows();

	String getHeaderText();
}
