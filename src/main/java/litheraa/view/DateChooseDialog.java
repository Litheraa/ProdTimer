package litheraa.view;

import com.github.weisj.darklaf.theme.Theme;
import litheraa.ViewController;
import litheraa.util.CalendarWrapper;
import litheraa.view.table.FilterTextField;
import litheraa.view.themes.ThemeColors;
import org.jdesktop.swingx.JXMonthView;
import org.jdesktop.swingx.calendar.DateSelectionModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Objects;

public class DateChooseDialog extends JXMonthView {
	private final Calendar calendar = new GregorianCalendar();
	private boolean isWrapped = false;
	private FilterTextField dateField;
	private JButton backwardYear;
	private JButton backwardMonth;
	private JButton forwardMonth;
	private JButton forwardYear;
	private JButton reset;

	private static final ClassLoader loader = DateChooseDialog.class.getClassLoader();

	private static final ImageIcon doubleLeft = new ImageIcon(Objects.requireNonNull(loader.getResource("angle-double-small-left.png")));
	private static final ImageIcon left = new ImageIcon(Objects.requireNonNull(loader.getResource("angle-small-left.png")));
	private static final ImageIcon doubleRight = new ImageIcon(Objects.requireNonNull(loader.getResource("angle-double-small-right.png")));
	private static final ImageIcon right = new ImageIcon(Objects.requireNonNull(loader.getResource("angle-small-right.png")));
	private static final ImageIcon calendarCancel = new ImageIcon(Objects.requireNonNull(loader.getResource("calendar-cancel.png")));

	public DateChooseDialog() {
		setColors(ViewController.getTheme());
		calendar.add(Calendar.MONTH, -2);
		setFirstDisplayedDay(calendar.getTime());
		setSelectionMode(DateSelectionModel.SelectionMode.SINGLE_INTERVAL_SELECTION);
		setPreferredColumnCount(3);
	}

	public void setInContainer(Container parent, FilterTextField dateField) {
		//Images are parts of <uicons> icons pack, downloaded from www.flaticon.com
		//calendar icons is tCreated by Amazona Adorada & downloaded from www.flaticon.com

		backwardYear = new JButton(doubleLeft);
		backwardYear.addActionListener(new DateMover(DateMover.DateEnum.BACKWARD_YEAR));

		backwardMonth = new JButton(left);
		backwardMonth.addActionListener(new DateMover(DateMover.DateEnum.BACKWARD_MONTH));

		forwardMonth = new JButton(right);
		forwardMonth.addActionListener(new DateMover(DateMover.DateEnum.FORWARD_MONTH));

		forwardYear = new JButton(doubleRight);
		forwardYear.addActionListener(new DateMover(DateMover.DateEnum.FORWARD_YEAR));

		this.dateField = dateField;

		reset = new JButton(calendarCancel);
		reset.addActionListener(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dateField.setText("");
				dateField.setFilter("");
			}
		});

		SpringLayout layout = (SpringLayout) parent.getLayout();
		layout.putConstraint(SpringLayout.WEST, backwardYear, 8, SpringLayout.WEST, parent);
		layout.putConstraint(SpringLayout.WEST, backwardMonth, 5, SpringLayout.EAST, backwardYear);
		layout.putConstraint(SpringLayout.EAST, reset, -15, SpringLayout.WEST, this.dateField);
		layout.putConstraint(SpringLayout.SOUTH, this, -8, SpringLayout.SOUTH, parent);
		layout.putConstraint(SpringLayout.EAST, forwardYear, -10, SpringLayout.EAST, parent);
		layout.putConstraint(SpringLayout.EAST, forwardMonth, -5, SpringLayout.WEST, forwardYear);
		layout.putConstraint(SpringLayout.WEST, this, 8, SpringLayout.WEST, parent);

		parent.add(backwardYear);
		parent.add(backwardMonth);
		parent.add(reset);
		parent.add(forwardMonth);
		parent.add(forwardYear);

		isWrapped = true;
	}

	public void wrapWithButtons(Container parent, String date) {
		//TODO добавить строку поиска "с даты" посередине кнопок
		//Images are parts of <uicons> icons pack, downloaded from www.flaticon.com
		//calendar icons is tCreated by Amazona Adorada & downloaded from www.flaticon.com

		JButton backwardYear = new JButton(doubleLeft);
		backwardYear.addActionListener(new DateMover(DateMover.DateEnum.BACKWARD_YEAR));

		JButton backwardMonth = new JButton(left);
		backwardMonth.addActionListener(new DateMover(DateMover.DateEnum.BACKWARD_MONTH));

		JButton forwardMonth = new JButton(right);
		forwardMonth.addActionListener(new DateMover(DateMover.DateEnum.FORWARD_MONTH));

		JButton forwardYear = new JButton(doubleRight);
		forwardYear.addActionListener(new DateMover(DateMover.DateEnum.FORWARD_YEAR));

		dateField = new FilterTextField(false);

		if (date.compareTo("01-01-1970") == 0) {
			dateField.setText("без даты");
		} else {
			dateField.setText(date);
		}

		JButton reset = new JButton(calendarCancel);
		reset.addActionListener(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dateField.setText("без даты");
			}
		});

		Container panel = new JPanel();
		SpringLayout layout = new SpringLayout();
		layout.putConstraint(SpringLayout.WEST, backwardYear, 0, SpringLayout.WEST, panel);
		layout.putConstraint(SpringLayout.WEST, backwardMonth, 5, SpringLayout.EAST, backwardYear);
		layout.putConstraint(SpringLayout.EAST, reset, -15, SpringLayout.WEST, dateField);
		layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, dateField, 0, SpringLayout.HORIZONTAL_CENTER, panel);

		layout.putConstraint(SpringLayout.EAST, forwardMonth, -5, SpringLayout.WEST, forwardYear);
		layout.putConstraint(SpringLayout.EAST, forwardYear, -1, SpringLayout.EAST, panel);

		panel.setLayout(layout);
		panel.add(backwardYear);
		panel.add(backwardMonth);
		panel.add(reset);
		panel.add(dateField);
		panel.add(forwardMonth);
		panel.add(forwardYear);

		parent.add(panel);
		parent.add(this);

		isWrapped = true;
	}

	@Override
	public void setVisible(boolean aFlag) {
		super.setVisible(aFlag);
		if (isWrapped) {
			dateField.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			dateField.setColumns(6);
			backwardYear.setVisible(aFlag);
			backwardMonth.setVisible(aFlag);
			forwardYear.setVisible(aFlag);
			forwardMonth.setVisible(aFlag);
			reset.setVisible(aFlag);
		}
	}

	public FilterAction getFilterAction() {
		return new FilterAction();
	}

	public void setText(String text) {
		dateField.setText(text);
	}

	public String getText() {
		return dateField.getText();
	}

	public void setColors(Theme theme) {
		try {ThemeColors themeColors = (ThemeColors) theme;
			themeColors.getColors(this);
		} catch (ClassCastException e) {
			System.out.println("Тема " + theme.getName() + " не реализует интерфейс ThemeColors");
		}
	}

	public class FilterAction extends AbstractAction {

		@Override
		public void actionPerformed(ActionEvent e) {
			String afterDate = CalendarWrapper.wrapToString(getFirstSelectionDate());
			String beforeDate = CalendarWrapper.wrapToString(getLastSelectionDate());
			if (!afterDate.equals(beforeDate)) {
				dateField.setText(afterDate.concat(" ").concat(beforeDate));
				dateField.setFilter(afterDate.concat(" ").concat(beforeDate));
			} else {
				dateField.setText(afterDate);
				dateField.setFilter(afterDate);
			}
		}
	}

	private class DateMover extends AbstractAction {
		private enum DateEnum {
			BACKWARD_YEAR,
			BACKWARD_MONTH,
			FORWARD_MONTH,
			FORWARD_YEAR,
		}

		DateEnum movement;

		private DateMover(DateEnum movement) {
			this.movement = movement;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			switch (movement) {
				case BACKWARD_YEAR -> {
					calendar.add(Calendar.YEAR, -1);
					setFirstDisplayedDay(calendar.getTime());
				}
				case BACKWARD_MONTH -> {
					calendar.add(Calendar.MONTH, -1);
					setFirstDisplayedDay(calendar.getTime());
				}
				case FORWARD_MONTH -> {
					calendar.add(Calendar.MONTH, 1);
					setFirstDisplayedDay(calendar.getTime());
				}
				case FORWARD_YEAR -> {
					calendar.add(Calendar.YEAR, 1);
					setFirstDisplayedDay(calendar.getTime());
				}
			}
		}
	}
}
