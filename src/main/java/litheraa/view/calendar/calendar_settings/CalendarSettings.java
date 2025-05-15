package litheraa.view.calendar.calendar_settings;

import litheraa.controller.SettingsController;
import litheraa.controller.ViewControllerInterface;
import litheraa.data.entities.SelectableText;
import litheraa.settings.DBSettings;
import litheraa.util.CalendarWrapper;
import litheraa.util.ViewType;
import litheraa.view.DateChooseDialog;
import litheraa.view.selection_table.SelectionTable;
import org.apache.commons.math3.util.Pair;
import org.jdesktop.swingx.JXMonthView;
import org.jdesktop.swingx.calendar.DateSelectionModel;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class CalendarSettings extends JDialog {
	private final SelectionTable table;
	private final JLabel selection = new JLabel();
	private final LinkedJComboBoxModel<Year> yearModel;
	private final LinkedJComboBoxModel<String> monthModel;
	private final RunnableJComboBox weekJComboBox;

	private DBSettings.Period period;

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

	public CalendarSettings(ViewControllerInterface controller, String setting, ArrayList<SelectableText> texts) {
		setTitle("Выберите режим отображения");
		setResizable(true);
		period = controller.getSettings().getPeriod();

		selection.setText(setting);
		selection.setHorizontalAlignment(JLabel.CENTER);

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

		LocalDate lD = controller.getSettings().getPeriod().getFrom();

		yearModel.setRunner(weekJComboBox);
//		yearModel.setSelectedItem(Year.of(lD.getYear()));
		yearModel.setSelectedItem(Year.of(period.getFrom().getYear()));

		monthModel.setRunner(weekJComboBox);
//		monthModel.setSelectedItem(CalendarWrapper.localeRu(lD.getMonth()));
		monthModel.setSelectedItem(CalendarWrapper.localeRu(period.getFrom().getMonth()));

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

		JTabbedPane pane = new JTabbedPane();
		pane.addChangeListener(e -> {
			switch (((JTabbedPane) e.getSource()).getSelectedIndex()) {
				case 0 -> {
					period.setPeriodType(ViewType.MONTHLY);
					text1.mutate();
				}
				case 1 -> {
					period.setPeriodType(ViewType.WEEKLY);
					text2.mutate();
				}
				default -> {
					period.setPeriodType(ViewType.DAILY);
					text1.mutate();
				}
			}
		});
		pane.addTab("По месяцам", monthPanel);
		pane.addTab("По неделям", weekPanel);
		pane.addTab("Один день", dayPanel);

		table = new SelectionTable(texts);

		JButton ok = new JButton("Ок");
		ok.addActionListener(e -> {
			String period = selection.getText();
			LocalDate date = getPeriod(period).getFirst();
			controller.getSettings().setPeriod(new DBSettings.Period(date));
			controller.setTextId(table.getSelectableTexts());
			controller.concreteView(getPeriod(period).getSecond(), date);
			setVisible(false);
		});

		JButton cancel = new JButton("Отмена");
		cancel.addActionListener(e ->
				setVisible(false)
		);

		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1.0;
		gbc.insets = new Insets(5, 5, 5, 5);
		add(selection, gbc);

		gbc.gridy = 1;
		add(pane, gbc);

		gbc.gridy = 2;
		gbc.weighty = 1.0;
		add(table, gbc);

		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		buttonsPanel.add(ok);
		buttonsPanel.add(cancel);

		gbc.gridy = 3;
		gbc.weighty = 0;
		add(buttonsPanel, gbc);

		pack();
	}

	private Pair<LocalDate, ViewType> getPeriod(String period) {
		ViewType type = ViewType.of(period);
		String[] strings = period.split(" ");
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
