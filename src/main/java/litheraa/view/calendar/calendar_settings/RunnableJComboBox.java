package litheraa.view.calendar.calendar_settings;

import litheraa.util.CalendarWrapper;

import javax.swing.*;
import java.time.Year;
import java.time.YearMonth;

class RunnableJComboBox extends LinkedJComboBox<String> implements Runnable{
	private final LinkedJComboBoxModel<Year> first;
	private final LinkedJComboBoxModel<String> second;

	RunnableJComboBox(String[] array, LinkedJComboBoxModel<Year> first, LinkedJComboBoxModel<String> second) {
		this.first = first;
		this.second = second;
		setModel(new DefaultComboBoxModel<>(array));
	}

	@Override
	public void run() {
		int currentSize = getModel().getSize();
		int weeks = CalendarWrapper
				.getWeeks(
						YearMonth.of(((Year) first.getSelectedItem()).getValue(),
								CalendarWrapper.deLocaleMonth((String) second.getSelectedItem())));

		if (currentSize >= weeks) {
			for (int i = currentSize; i > weeks; ) {
				removeItemAt(--i);
			}
		} else {
			for (int i = currentSize; i < weeks;) {
				addItem(++i + " неделя");
			}
		}
	}
}
