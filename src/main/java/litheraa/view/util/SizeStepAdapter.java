package litheraa.view.util;

import litheraa.view.calendar.AdjustableComponentInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.LinkedList;
import java.util.List;

public class SizeStepAdapter extends ComponentAdapter {
	private final List<AdjustableComponentInterface> adjustableComponents = new LinkedList<>();
	private Step oldStep;
	private long timer;
	private final int initial;
	private final int increment;

	public SizeStepAdapter(int initial, int increment, AdjustableComponentInterface... adjustableComponent) {
		this.adjustableComponents.addAll(List.of(adjustableComponent));
		this.initial = initial;
		this.increment = increment;
	}

	@Override
	public void componentResized(ComponentEvent e) {
		Step sizeStep = calculateSizeStep(Math.min((int)(e.getComponent().getWidth() * 0.8), e.getComponent().getHeight()));
		if (oldStep != sizeStep) {
			if (System.currentTimeMillis() - timer > 50) {
				timer = System.currentTimeMillis();
				oldStep = sizeStep;
				adjustableComponents.forEach(c -> c.sizeChanged(sizeStep));
			}
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
		}
		if (SwingUtilities.isRectangleContainingRectangle(
				getStepDimension(Step.FIFTH),
				dimension)) {
			return Step.FIFTH;
		} else return Step.SIXTH;
	}

	private Step calculateSizeStep(int s) {
		if (s < initial) return Step.FIRST;
		if (s < initial + increment) return Step.SECOND;
		if (s < initial + 2 * increment) return Step.THIRD;
		if (s < initial + 3 * increment) return Step.FOURTH;
		if (s < initial + 4 * increment) return Step.FIFTH;
		return Step.SIXTH;
	}

	private Rectangle getStepDimension(Step step) {
		return switch (step) {
			case FIRST -> /*new Rectangle(90, 70);*/ new Rectangle(615, 530);
			case SECOND -> /*new Rectangle(110, 88);*/ new Rectangle(880, 770);
			case THIRD -> /*new Rectangle(135, 108);*/ new Rectangle(1050, 900);
			case FOURTH -> /*new Rectangle(165, 130);*/ new Rectangle(1595, 1020);
			case FIFTH -> /*new Rectangle(200, 154);*/ new Rectangle(2200, 1200);
			case null, default -> /*new Rectangle(240, 180);*/ new Rectangle(3000, 1500);
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