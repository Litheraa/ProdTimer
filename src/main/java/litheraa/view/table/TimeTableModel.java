package litheraa.view.table;

import litheraa.data.models.CalendarModel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import javax.swing.table.AbstractTableModel;
import java.time.LocalDate;
import java.util.Arrays;

@NoArgsConstructor
public class TimeTableModel extends AbstractTableModel {
	private CalendarModel calendarModel;

	public TimeTableModel(CalendarModel calendarModel) {
		this.calendarModel = calendarModel;
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return Header.of(columnIndex).clazz;
	}

	@Override
	public int getRowCount() {
		return calendarModel.getValidDates().size();
	}

	@Override
	public int getColumnCount() {
		return Header.values().length;
	}

	@SneakyThrows
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		LocalDate id = calendarModel.getValidDates().get(rowIndex);
		Header column = Header.of(columnIndex);

		return switch (column) {
			case MODIFIED -> id;
			case WRITTEN -> calendarModel.getWritten(id);
			case GOAL -> calendarModel.getGoal(id);
			case TOGO -> calendarModel.getGoal(id) - calendarModel.getWritten(id);
			case PRODS -> calendarModel.getTexts().stream().map(sT -> sT.getText().getName()).toArray();
		};
	}

	@Override
	public String getColumnName(int column) {
		return Header.values()[column].locale;
	}

	public enum Header {
		MODIFIED("Дата", LocalDate.class),
		WRITTEN("Написано", Integer.class),
		GOAL("Цель", Integer.class),
		TOGO("Осталось", Integer.class),
		PRODS("Проды", Arrays.class);

		private final String locale;
		private final Class<?> clazz;

		Header(String locale, Class<?> clazz) {
			this.locale = locale;
			this.clazz = clazz;
		}

		public static Header of(int ordinal) {
			return values()[ordinal];
		}
	}
}

