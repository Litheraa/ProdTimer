package litheraa.view.calendar;

import com.formdev.flatlaf.ui.FlatProgressBarUI;
import litheraa.view.util.AspectRatioAdapter;
import litheraa.view.util.SizeStepAdapter;
import litheraa.view.util.factory.ConstraintFactory;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;

public class ProgressBar extends JProgressBar implements AdjustableComponentInterface{
	private final ConstraintFactory constraintFactory = ConstraintFactory.getInstance();
	private Container PARENT = getParent();
	@Getter
	private boolean isFulfilled;

	public ProgressBar(int written, int goal) {
		isFulfilled = written > goal;
		setUI(new CustomProgressBarUI());
		setBorder(BorderFactory.createLineBorder(UIManager.getColor("SubTitle.background"), 1));
		setMaximum(goal);
		setStringPainted(true);
		if (isFulfilled) {
			setValue(goal);
			setString("!!! " + calculateOverFulfilling(written, goal) + "% !!!");
		} else {
			setValue(written);
		}
	}

	private int calculateOverFulfilling(int written, int goal) {
		return (int) Math.round(((double) written / goal) * 100);
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

	public static class CustomProgressBarUI extends FlatProgressBarUI {

		@Override
		public void paint(Graphics g, JComponent c) {
			Graphics2D g2 = (Graphics2D) g.create();

			Insets insets = progressBar.getInsets();
			int barWidth = progressBar.getWidth() - insets.left - insets.right;
			int barHeight = progressBar.getHeight() - insets.top - insets.bottom;

			int amountFull = ((ProgressBar)progressBar).isFulfilled() ? barWidth : getAmountFull(insets, barWidth, barHeight);
			int percent = (int) (progressBar.getPercentComplete() * 100);

			Color colorFrom = UIManager.getColor("ProgressBar.from");
			Color colorTo = UIManager.getColor("ProgressBar.to");

			int red = interpolate(colorFrom.getRed(), colorTo.getRed(), percent);
			int green = interpolate(colorFrom.getGreen(), colorTo.getGreen(), percent);
			int blue = interpolate(colorFrom.getBlue(), colorTo.getBlue(), percent);

			g2.setColor(new Color(red, green, blue));
			g2.fillRect(insets.left, insets.top, amountFull, barHeight);

			if (progressBar.isStringPainted()) {
				paintString(g2, insets.left, insets.top, barWidth, barHeight, amountFull, insets);
			}

			g2.dispose();
		}

		private int interpolate(int start, int end, int percent) {
			return start + (end - start) * percent / 100;
		}

	}
}
