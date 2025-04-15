package litheraa.view.calendar.calendar_settings;

import litheraa.controller.CalendarController;
import litheraa.controller.SettingsController;
import litheraa.controller.ViewControllerInterface;
import litheraa.util.CalendarWrapper;
import litheraa.util.ViewType;
import litheraa.view.DateChooseDialog;
import org.apache.commons.math3.util.Pair;
import org.jdesktop.swingx.JXMonthView;
import org.jdesktop.swingx.calendar.DateSelectionModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class CalendarSettings extends JDialog {
	private final JComboBox<String> textJComboBox;
	private final JLabel selection = new JLabel();
	private final LinkedJComboBoxModel<Year> yearModel;
	private final LinkedJComboBoxModel<String> monthModel;
	private final RunnableJComboBox weekJComboBox;

	{
		yearModel = new LinkedJComboBoxModel<>(Stream
				.iterate(Year.now(),
						y -> y.isAfter(Year.parse(SettingsController.getCutDate().substring(6))),
						y -> y.minusYears(1L))
				.toArray(Year[]::new)
		);

		monthModel = new LinkedJComboBoxModel<>(Arrays
				.stream(Month.values())
				.map(CalendarWrapper::localeRu)
				.toArray(String[]::new)
		);
		selection.setFont(new Font("Aerial", Font.BOLD, 20));
	}

	public CalendarSettings(ViewControllerInterface controller, String setting, String[] textNames) {
		setSize(480, 350);
		setTitle("Выберите режим отображения");
		setResizable(false);

		selection.setText(setting);

		LinkedJComboBox<Year> yearJComboBox1 = new LinkedJComboBox<>(yearModel);
		LinkedJComboBox<String> monthJComboBox1 = new LinkedJComboBox<>(monthModel);

		LinkedJComboBox<Year> yearJComboBox2 = new LinkedJComboBox<>(yearModel);
		LinkedJComboBox<String> monthJComboBox2 = new LinkedJComboBox<>(monthModel);

		JPanel monthPanel = new JPanel();
		monthPanel.add(yearJComboBox1);
		monthPanel.add(monthJComboBox1);

		weekJComboBox = new RunnableJComboBox(
				IntStream.range(1,
								CalendarWrapper
										.getWeeks(
												YearMonth.of(((Year) yearModel.getSelectedItem()).getValue(),
														CalendarWrapper.deLocaleMonth((String) monthModel.getSelectedItem()))) + 1)
						.boxed()
						.map(i -> i + " неделя")
						.toArray(String[]::new),
				yearModel,
				monthModel);

		MutableText text1 = new MutableText(selection, yearJComboBox1, monthJComboBox1);

		yearJComboBox1.setText(text1);
		monthJComboBox1.setText(text1);

		MutableText text2 = new MutableText(selection, yearJComboBox2, monthJComboBox2, weekJComboBox);

		yearJComboBox2.setText(text2);
		monthJComboBox2.setText(text2);
		weekJComboBox.setText(text2);


		yearModel.setRunner(weekJComboBox);
		yearModel.setSelectedItem(Year.of(SettingsController.getPeriod().getYear()));

		monthModel.setRunner(weekJComboBox);
		monthModel.setSelectedItem(CalendarWrapper.localeRu(SettingsController.getPeriod().getMonth()));

		JPanel weekPanel = new JPanel();
		weekPanel.add(yearJComboBox2);
		weekPanel.add(monthJComboBox2);
		weekPanel.add(weekJComboBox);

		JPanel dayPanel = new JPanel();

		DateChooseDialog dialog = new DateChooseDialog();
		dialog.wrapWithButtons(dayPanel, LocalDate.now().toString());
		dialog.setSelectionMode(DateSelectionModel.SelectionMode.SINGLE_SELECTION);
		dialog.addActionListener(e -> {
			selection.setText(CalendarWrapper.localeRu(((JXMonthView) e.getSource()).getSelectionDate()));
		});

		textJComboBox = new JComboBox<>(textNames);
		textJComboBox.insertItemAt("Все тексты", 0);
		textJComboBox.setSelectedIndex(0);

		JTabbedPane pane = new JTabbedPane();
		pane.addChangeListener(e -> {
			switch (((JTabbedPane) e.getSource()).getSelectedIndex()) {
				case 0 -> text1.mutate();
				case 1 -> text2.mutate();
				default -> text1.mutate();
			}
		});
		pane.addTab("По месяцам", monthPanel);
		pane.addTab("По неделям", weekPanel);
		pane.addTab("Один день", dayPanel);

		JButton ok = new JButton("Ок");
		ok.addActionListener(e -> {
			String period = selection.getText();
			SettingsController.setPeriod(getPeriod(period).getFirst());
			controller.setTextId(textJComboBox.getSelectedItem().toString());
			controller.concreteView(getPeriod(period).getSecond());
			setVisible(false);
		});

		JButton cansel = new JButton("Отмена");
		cansel.addActionListener(e ->
				setVisible(false)
		);

		SpringLayout layout = new SpringLayout();
		layout.putConstraint(SpringLayout.NORTH, selection, 0, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, selection, 0, SpringLayout.HORIZONTAL_CENTER, pane);
		layout.putConstraint(SpringLayout.SOUTH, selection, 35, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.NORTH, pane, 0, SpringLayout.SOUTH, selection);
		layout.putConstraint(SpringLayout.NORTH, textJComboBox, 0, SpringLayout.SOUTH, pane);
		layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, textJComboBox, 0, SpringLayout.HORIZONTAL_CENTER, pane);
		layout.putConstraint(SpringLayout.NORTH, ok, 0, SpringLayout.SOUTH, textJComboBox);
		layout.putConstraint(SpringLayout.NORTH, cansel, 0, SpringLayout.SOUTH, textJComboBox);
		layout.putConstraint(SpringLayout.EAST, ok, -5, SpringLayout.HORIZONTAL_CENTER, pane);
		layout.putConstraint(SpringLayout.WEST, cansel, 5, SpringLayout.HORIZONTAL_CENTER, pane);

		setLayout(layout);

		add(selection);
		add(pane);
		add(textJComboBox);
		add(ok);
		add(cansel);
	}

	private Pair<LocalDate, ViewType> getPeriod(String text) {
		ViewType type = ViewType.of(text);
		String[] strings = text.split(" ");
		Pair<LocalDate, ViewType> result;
		switch (type) {
			case WEEKLY -> {
				int year = Integer.parseInt(strings[3]);
				Month month = CalendarWrapper.deLocaleMonth(strings[2]);
				result = new Pair<>(
						LocalDate
								.of(year, month,
										(Integer.parseInt(strings[0]) * 7) -
												YearMonth
														.of(year, month)
														.atDay(1)
														.getDayOfWeek()
														.ordinal()
								),
						type);
			}
			case DAILY -> {
				int year = Integer.parseInt(strings[2]);
				Month month = CalendarWrapper.deLocaleMonth(strings[1]);
				String s = strings[0];
				int day;
				if (s.charAt(0) == '0') {
					day = Integer.parseInt(s.substring(1));
				} else {
					day = Integer.parseInt(s);
				}
				result = new Pair<>(LocalDate.of(year, month, day), type);
			}
			default ->
					result = new Pair<>(YearMonth.of(Integer.parseInt(strings[1]), CalendarWrapper.deLocaleMonth(strings[0])).atDay(1), type);
		}
		return result;
	}
}
