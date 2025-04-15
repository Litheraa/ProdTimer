package litheraa.view.table;

import litheraa.data.ColumnDataTypeEnum;
import litheraa.data.TextEnum;
import litheraa.data.models.TextModel;
import litheraa.util.ViewType;
import lombok.SneakyThrows;

import java.time.LocalDate;

public class TextTableModel extends TimeTableModel implements ColumnType {
	private final TextModel model;

	public TextTableModel(TextModel model) {
		this.model = model;
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return Header.of(columnIndex).clazz;
	}

	@Override
	public ColumnDataTypeEnum getColumnType(int column) {
		return TextEnum.getFieldType(column);
	}

	@Override
	public int getExactModelType() {
		return ViewType.TEXTS.ordinal();
	}

	@Override
	public int getRowCount() {
		return model.getValidTexts().size();
	}

	@Override
	public int getColumnCount() {
		return Header.values().length;
	}

	@SneakyThrows
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		TextModel.Node node = model.getText(rowIndex);
		Header column = Header.of(columnIndex);

		return switch (column) {
			case NAME -> node.getName();
			case CREATED -> node.getCreated();
			case MODIFIED -> node.getLastModified();
			case WRITTEN -> node.getWritten();
			case GOAL -> node.getGoal();
			case TOGO -> node.getGoal() - node.getWritten();
			case PATH -> node.getPath();
			case PRODS -> node.getProds();
		};
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return columnIndex == TextEnum.CHARS_TOTAL.ordinal() || columnIndex == TextEnum.TEXT_NAME.ordinal();
	}

	@Override
	public String getColumnName(int column) {
		return Header.of(column).locale;
	}
//
//	@Override
//	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
//		TextOld textOld = data.get(rowIndex);
//		if (aValue.toString().isEmpty()) {
//			aValue = 0;
//		}
//		TextEnum column = TextEnum.values()[columnIndex];
//		switch (column) {
//			case TextEnum.CHARS_TOTAL -> {
//				for (TextOld prod : data) {
//					if (prod.getPath().equals(textOld.getPath())) {
//						prod.setCharsTotal(Double.parseDouble(aValue.toString()));
//						SettingsController.setCharsTotal(Double.parseDouble(aValue.toString()), prod.getPath());
//					}
//				}
//				fireTableDataChanged();
//			}
//			case TextEnum.TEXT_NAME -> {
//				for (TextOld prod : data) {
//					if (prod.getPath().equals(textOld.getPath())) {
//						prod.setTextName(aValue.toString());
//						SettingsController.setTextName(aValue.toString(), prod.getPath());
//					}
//				}
//				fireTableDataChanged();
//			}
//			default -> throw new IllegalArgumentException();
//		}
//	}

	public enum Header {
		NAME("Название", String.class),
		CREATED("Дата создания", String.class),
		MODIFIED("Дата изменения", LocalDate.class),
		WRITTEN("Написано", Integer.class),
		GOAL("Цель", Integer.class),
		TOGO("Осталось", Integer.class),
		PATH("Путь", String.class),
		PRODS("Проды", Object.class);

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
