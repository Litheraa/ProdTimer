package litheraa.view.table;

import litheraa.data.ColumnDataTypeEnum;
import litheraa.data.RoutineEnum;
import litheraa.data.models.CalendarModel;
import litheraa.util.ViewType;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import javax.swing.table.AbstractTableModel;
import java.time.LocalDate;

@NoArgsConstructor
public class TimeTableModel extends AbstractTableModel implements ColumnType {
	private CalendarModel calendarModel;

	public TimeTableModel(CalendarModel calendarModel) {
		this.calendarModel = calendarModel;
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
			case PRODS -> calendarModel.getTextNames();
		};
	}

	@Override
	public String getColumnName(int column) {
		return Header.values()[column].locale;
	}

	@Override
	public ColumnDataTypeEnum getColumnType(int column) {
		return RoutineEnum.getFieldType(column);
	}

	@Override
	public int getExactModelType() {
		return ViewType.TIME.ordinal();
	}

	public enum Header {
		MODIFIED("Дата"),
		WRITTEN("Написано"),
		GOAL("Цель"),
		TOGO("Осталось"),
		PRODS("Проды");

		private final String locale;

		Header(String locale) {
			this.locale = locale;
		}

		public static Header of(int ordinal) {
			return values()[ordinal];
		}
	}
}

