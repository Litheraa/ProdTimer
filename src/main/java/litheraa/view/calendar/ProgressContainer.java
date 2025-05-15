package litheraa.view.calendar;

import litheraa.controller.CalendarController;
import litheraa.controller.SettingsController;
import litheraa.data.models.CalendarModel;
import litheraa.util.MeasureUnitConverter;
import litheraa.util.NumberDeclensionRu;
import litheraa.view.util.IntegerFilter;
import org.jdesktop.swingx.JXLabel;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
	private int date;
	private CalendarModel calendarModel;

	public ProgressContainer(double value, int maxValue) {
		measuredValue = MeasureUnitConverter.toChars(value);
		this.value = value;
		this.maxValue = maxValue;
	}

//	public ProgressContainer(CalendarModel calendarModel, int date) {
//		this(calendarModel.getWritten(date), calendarModel.getGoal(date));
//		this.calendarModel = calendarModel;
//		this.date = date;
//	}

	public void createVerticalProgress(CalendarController controller) {
//		<a href="https://www.flaticon.com/free-icons/mission" title="mission icons">Mission icons created by Us and Up - Flaticon</a>
//		<div> Icons made by <a href="" title="SANB"> SANB </a> from <a href="https://www.flaticon.com/" title="Flaticon">www.flaticon.com'</a></div>
//		<a href="https://www.flaticon.com/free-icons/myth" title="myth icons">Myth icons created by Aranagraphics - Flaticon</a>
		progressBar = new JProgressBar(JProgressBar.VERTICAL);
		progressBar.setMaximum(maxValue);
		progressBar.setValue(measuredValue);
		progressBar.setStringPainted(true);

		ClassLoader loader = ProgressContainer.class.getClassLoader();
		goalIcon = new ImageIcon(Objects.requireNonNull(loader.getResource("mission.png")));
		doneIcon = new ImageIcon(Objects.requireNonNull(loader.getResource("magic-book.png")));
		blankIcon = new ImageIcon(Objects.requireNonNull(loader.getResource("writed-book.png")));

		dateLabel = new JLabel(String.valueOf(date));
		setDatePopUp();

		goal = new JLabel(String.valueOf(maxValue));
		goal.setIcon(goalIcon);
		setGoalPopUp(controller);

		done = new JLabel(String.valueOf(measuredValue));
		done.setIcon(doneIcon);
		remaining = new JLabel(String.valueOf(maxValue - measuredValue));
		remaining.setIcon(blankIcon);

		SpringLayout layout = new SpringLayout();
		layout.putConstraint(SpringLayout.SOUTH, progressBar, -1, SpringLayout.SOUTH, this);
		layout.putConstraint(SpringLayout.NORTH, progressBar, 1, SpringLayout.NORTH, this);
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

	}

	public ProgressContainer createHorizontalProgress() {
		JLabel label = new JLabel("Написано: " + measuredValue + " Осталось: " + (maxValue - measuredValue));

		progressBar = new JProgressBar();
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
			String measure = SettingsController.isChars() ? "знак" : "АЛка";
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
		measuredValue = MeasureUnitConverter.toChars(value);
	}

	private void setGoalPopUp(CalendarController controller) {
		JPopupMenu menu = new JPopupMenu();
		JTextField textField = new JTextField(goal.getText());
		textField.setPreferredSize(new Dimension(50, textField.getHeight() + 25));
		AbstractDocument document = (AbstractDocument) textField.getDocument();
		document.setDocumentFilter(new IntegerFilter());
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					String dayGoal = textField.getText();
					goal.setText(dayGoal);
//					calendarModel.setGoal(Integer.parseInt(dayGoal), date);
//					controller.setGoal(Integer.parseInt(dayGoal), calendarModel.getTimeId(date));
					menu.setVisible(false);
				}
			}
		});
		menu.add(textField);
		goal.setComponentPopupMenu(menu);
		goal.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!menu.isVisible()) {
					menu.setVisible(true);
					menu.show(goal, goal.getX() + goal.getWidth(), goal.getY() - (goal.getHeight() * 4));
					textField.getCaret().setDot(0);
				}
			}
		});
	}

	private void setDatePopUp() {
		JPopupMenu menu = new JPopupMenu();
		JXLabel textNames = new JXLabel();
//		textNames.setText("Вы работали в текстах:" + System.lineSeparator() + calendarModel.getTextNames(date));
		textNames.setSize(150, 10);
		textNames.setVerticalTextPosition(SwingConstants.TOP);
		textNames.setTextAlignment(JXLabel.TextAlignment.CENTER);
		textNames.setLineWrap(true);
		menu.add(textNames);
		dateLabel.setComponentPopupMenu(menu);
		dateLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!menu.isVisible()) {
					menu.show(dateLabel, dateLabel.getX() + dateLabel.getWidth(), dateLabel.getY());
					menu.pack();
				}
			}
		});

	}
}
