package litheraa.view.calendar;

import litheraa.SettingsController;
import litheraa.util.MeasureUnit;
import litheraa.util.NumberDeclensionRu;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class ProgressContainer extends JPanel {
	private int measuredValue;
	private final double value;
	private final int maxValue;
	private JLabel dateLabel;
	private JLabel goal;
	private JLabel done;
	private JLabel remaining;
	private ImageIcon goalIcon;
	private ImageIcon doneIcon;
	private ImageIcon blankIcon;
	private JProgressBar progressBar;

	public ProgressContainer(double value, int maxValue) {
		measuredValue = MeasureUnit.toChars(value);
		this.value = value;
		this.maxValue = maxValue;
	}

	public ProgressContainer createVerticalProgress(int date) {
//		<a href="https://www.flaticon.com/free-icons/mission" title="mission icons">Mission icons created by Us and Up - Flaticon</a>
//		<div> Icons made by <a href="" title="SANB"> SANB </a> from <a href="https://www.flaticon.com/" title="Flaticon">www.flaticon.com'</a></div>
//		<a href="https://www.flaticon.com/free-icons/myth" title="myth icons">Myth icons created by Aranagraphics - Flaticon</a>
		progressBar = new JProgressBar(JProgressBar.VERTICAL);
		progressBar.setOpaque(true);
		progressBar.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
		progressBar.setMaximum(maxValue);
		progressBar.setStringPainted(true);
		progressBar.setValue(measuredValue);

		ClassLoader loader = ProgressContainer.class.getClassLoader();
		goalIcon = new ImageIcon(Objects.requireNonNull(loader.getResource("mission.png")));
		doneIcon = new ImageIcon(Objects.requireNonNull(loader.getResource("magic-book.png")));
		blankIcon = new ImageIcon(Objects.requireNonNull(loader.getResource("writed-book.png")));


		dateLabel = new JLabel(String.valueOf(date));
		goal = new JLabel(String.valueOf(maxValue));
		goal.setIcon(goalIcon);
		done = new JLabel(String.valueOf(measuredValue));
		done.setIcon(doneIcon);
		remaining = new JLabel(String.valueOf(maxValue - value));
		remaining.setIcon(blankIcon);

		SpringLayout layout = new SpringLayout();
		layout.putConstraint(SpringLayout.EAST, progressBar, 0, SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.HEIGHT, progressBar, 0, SpringLayout.HEIGHT, this);
		layout.putConstraint(SpringLayout.EAST, progressBar, -1, SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.WEST, dateLabel, 0, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.EAST, dateLabel, 0, SpringLayout.WEST, progressBar);
		layout.putConstraint(SpringLayout.SOUTH, dateLabel, 0, SpringLayout.VERTICAL_CENTER, this);
		layout.putConstraint(SpringLayout.NORTH, goal, 0, SpringLayout.SOUTH, dateLabel);
		layout.putConstraint(SpringLayout.EAST, goal, 0, SpringLayout.WEST, progressBar);
		layout.putConstraint(SpringLayout.WEST, goal, 1, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, done, 1, SpringLayout.SOUTH, goal);
		layout.putConstraint(SpringLayout.EAST, done, 0, SpringLayout.WEST, progressBar);
		layout.putConstraint(SpringLayout.WEST, done, 1, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, remaining, 1, SpringLayout.SOUTH, done);
		layout.putConstraint(SpringLayout.EAST, remaining, 0, SpringLayout.WEST, progressBar);
		layout.putConstraint(SpringLayout.WEST, remaining, 1, SpringLayout.WEST, this);
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

	public void adjustInnerComponentsSize(Dimension size) {
		double minimalSize = Math.min(size.getHeight() / 2, size.getWidth() - progressBar.getWidth());
		Font dateFont = new Font("Aerial", Font.BOLD, (int) (minimalSize));
		dateLabel.setFont(dateFont);
		int iconSize = (int) (minimalSize / 3);
		Font font = new Font("Aerial", Font.PLAIN, iconSize);
		goal.setIcon(new ImageIcon(goalIcon.getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH)));
		goal.setPreferredSize(new Dimension(goal.getWidth(), iconSize));
		goal.setFont(font);
		done.setIcon(new ImageIcon(doneIcon.getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH)));
		done.setPreferredSize(new Dimension(done.getWidth(), iconSize));
		done.setFont(font);
		remaining.setIcon(new ImageIcon(blankIcon.getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH)));
		remaining.setPreferredSize(new Dimension(remaining.getWidth(), iconSize));
		remaining.setFont(font);
	}
}
