package litheraa.view.util;

import litheraa.view.calendar.AdjustableComponentInterface;
import litheraa.view.calendar.DayPanelController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class SizeStepAdapter extends ComponentAdapter {
	private final AdjustableComponentInterface controller;
	private Step oldStep;

	public SizeStepAdapter(DayPanelController controller) {
		this.controller = controller;
	}

	@Override
	public void componentResized(ComponentEvent e) {
		Step sizeStep = calculateSizeStep(e.getComponent().getSize());
		if (oldStep != sizeStep) {
			oldStep = sizeStep;
			controller.sizeChanged(sizeStep);
		}
	}

	private Step calculateSizeStep(Dimension d) {
		Rectangle dimension = new Rectangle(d);
		if (SwingUtilities.isRectangleContainingRectangle(
				getStepDimension(Step.SECOND),
				dimension)) {
			return Step.FIRST;
		}
		if (SwingUtilities.isRectangleContainingRectangle(
				getStepDimension(Step.THIRD),
				dimension)) {
			return Step.SECOND;
		}
		if (SwingUtilities.isRectangleContainingRectangle(
				getStepDimension(Step.FOURTH),
				dimension)) {
			return Step.THIRD;
		}
		if (SwingUtilities.isRectangleContainingRectangle(
				getStepDimension(Step.FIFTH),
				dimension)) {
			return Step.FOURTH;
		} else return Step.FIFTH;
	}

	private Rectangle getStepDimension(Step step) {
		return switch (step) {
			case FIRST -> new Rectangle(90, 70);
			case SECOND -> new Rectangle(110, 88);
			case THIRD -> new Rectangle(135, 108);
			case FOURTH -> new Rectangle(175, 130);
			case FIFTH -> new Rectangle(220, 154);
			case null, default -> new Rectangle(260, 180);
		};
	}

	public enum Step {
		FIRST,
		SECOND,
		THIRD,
		FOURTH,
		FIFTH,
		SIXTH,
	}
}
