package litheraa.view;

import litheraa.SettingsController;
import litheraa.util.MeasureUnit;
import litheraa.util.NumberDeclensionRu;

import javax.swing.*;
import java.util.Objects;

public class ProgressContainer extends JPanel {
	private int measuredValue;
	private final double value;
	private final int maxValue;

	public ProgressContainer(double value, int maxValue) {
		measuredValue = MeasureUnit.toChars(value);
		this.value = value;
		this.maxValue = maxValue;
	}

	public ProgressContainer createVerticalProgress(int date) {
//		<a href="https://www.flaticon.com/free-icons/goal" title="goal icons">Goal icons created by Sicon - Flaticon</a>
		JProgressBar progressBar = new JProgressBar(JProgressBar.VERTICAL);
		progressBar.setOpaque(true);
		progressBar.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
		progressBar.setMaximum(maxValue);
		progressBar.setStringPainted(true);
		progressBar.setValue(measuredValue);

		ClassLoader loader = ProgressContainer.class.getClassLoader();
		ImageIcon goalIcon = new ImageIcon(Objects.requireNonNull(loader.getResource("target.png")));
		ImageIcon doneIcon = new ImageIcon(Objects.requireNonNull(loader.getResource("done.png")));
		ImageIcon blankIcon = new ImageIcon(Objects.requireNonNull(loader.getResource("blank.png")));


		JLabel dateLabel = new JLabel(String.valueOf(date));
		JLabel goal = new JLabel(String.valueOf(maxValue));
		goal.setIcon(goalIcon);
		JLabel done = new JLabel(String.valueOf(measuredValue));
		done.setIcon(doneIcon);
		JLabel remaining = new JLabel(String.valueOf(maxValue - value));
		remaining.setIcon(blankIcon);

		SpringLayout layout = new SpringLayout();
		layout.putConstraint(SpringLayout.EAST, progressBar, 0, SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.HEIGHT, progressBar, 0, SpringLayout.HEIGHT, this);
		layout.putConstraint(SpringLayout.WEST, progressBar, 20, SpringLayout.HORIZONTAL_CENTER, this);
		layout.putConstraint(SpringLayout.WEST, dateLabel, 0, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.EAST, dateLabel, 0, SpringLayout.WEST, progressBar);
		layout.putConstraint(SpringLayout.SOUTH, dateLabel, 0, SpringLayout.VERTICAL_CENTER, this);
		layout.putConstraint(SpringLayout.NORTH, goal, 0, SpringLayout.SOUTH, dateLabel);
		layout.putConstraint(SpringLayout.EAST, goal, 0, SpringLayout.WEST, progressBar);
		layout.putConstraint(SpringLayout.WEST, goal, 0, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, done, 1, SpringLayout.SOUTH, goal);
		layout.putConstraint(SpringLayout.EAST, done, 0, SpringLayout.WEST, progressBar);
		layout.putConstraint(SpringLayout.WEST, done, 0, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, remaining, 0, SpringLayout.SOUTH, done);
		layout.putConstraint(SpringLayout.EAST, remaining, 0, SpringLayout.WEST, progressBar);
		layout.putConstraint(SpringLayout.WEST, remaining, 0, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.SOUTH, remaining, 0, SpringLayout.SOUTH, this);

		setLayout(layout);
		add(progressBar);
		add(dateLabel);
		add(goal);
		add(done);
		add(remaining);

		return this;
	}

	public ProgressContainer createHorizontalProgress() {
		JLabel label = new JLabel("Написано: " + measuredValue + " Осталось: " + (maxValue - measuredValue));

		JProgressBar progressBar = new JProgressBar();
		progressBar.setOpaque(true);
		progressBar.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
		progressBar.setMaximum(maxValue);
		progressBar.setStringPainted(true);

		SpringLayout layout = new SpringLayout();
		layout.putConstraint(SpringLayout.WIDTH, progressBar, 0, SpringLayout.WIDTH, this);
		layout.putConstraint(SpringLayout.WEST, label, 2, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.EAST, label, -2, SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.NORTH, label, 0, SpringLayout.SOUTH, progressBar);

		setLayout(layout);

		if (measuredValue >= maxValue) {
			String measure = SettingsController.isChars() ? "знак" : "Алка";
			progressBar.setString("Готово! " + value + " " +
					NumberDeclensionRu.decline(measure.concat(" написан"), measuredValue));
		} else {
			progressBar.setValue(measuredValue);
			add(label);
			add(progressBar);
		}
		return this;
	}

	public void setMeasuredValue(double value) {
		measuredValue = MeasureUnit.toChars(value);
	}
}
