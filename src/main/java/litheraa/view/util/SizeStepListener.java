package litheraa.view.util;

import litheraa.view.calendar.DayPanelController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class SizeStepListener extends ComponentAdapter {
	private final DayPanelController controller;
	private Step oldStep;

	public SizeStepListener(DayPanelController controller) {
		this.controller = controller;
	}

	@Override
	public void componentResized(ComponentEvent e) {
		Step sizeStep = calculateSizeStep(e.getComponent().getSize());
		if (oldStep != sizeStep) {
			oldStep = sizeStep;
			controller.sizeStepChanged(sizeStep);
		}
	}

	private Step calculateSizeStep(Dimension d) {
		Rectangle dimension = new Rectangle(d);
		if (SwingUtilities.isRectangleContainingRectangle(
				new Rectangle(getStepDimension(Step.SECOND)),
				dimension)) {
			return Step.FIRST;
		}
		if (SwingUtilities.isRectangleContainingRectangle(
				new Rectangle(getStepDimension(Step.THIRD)),
				dimension)) {
			return Step.SECOND;
		}
		if (SwingUtilities.isRectangleContainingRectangle(
				new Rectangle(getStepDimension(Step.FOURTH)),
				dimension)) {
			return Step.THIRD;
		}
		if (SwingUtilities.isRectangleContainingRectangle(
				new Rectangle(getStepDimension(Step.FIFTH)),
				dimension)) {
			return Step.FOURTH;
		} else return Step.FIFTH;
	}

	private Dimension getStepDimension(Step step) {
		return switch (step) {
			case FIRST -> new Dimension(90, 70);
			case SECOND -> new Dimension(120, 93);
			case THIRD -> new Dimension(150, 116);
			case FIFTH -> new Dimension(180, 140);
			case null, default -> new Dimension(210, 163);
		};
	}

	public enum Step {
		FIRST,
		SECOND,
		THIRD,
		FOURTH,
		FIFTH
	}
}
