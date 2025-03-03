package litheraa.view.table;

import litheraa.controller.SettingsController;
import litheraa.data.ColumnDataTypeEnum;
import litheraa.data.TextOld;
import litheraa.data.TextEnum;
import litheraa.util.CalendarWrapper;
import litheraa.util.MeasureUnit;
import litheraa.util.ViewType;
import lombok.SneakyThrows;

import java.util.ArrayList;

public class TextModel extends RoutineModel implements ColumnType {

	private final ArrayList<TextOld> data;
	private final String[] header = {"Прода", "Создан", "Изменен",
			"Знаков в проде", "Знаков в тексте", "Знаков всего", "Название текста", "Путь"};

	public TextModel(ArrayList<TextOld> data) {
		super(data);
		this.data = data;
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
		return data.size();
	}

	@Override
	public int getColumnCount() {
		return header.length;
	}

	@SneakyThrows
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		TextOld textOld = data.get(rowIndex);
		TextEnum column = TextEnum.values()[columnIndex];

		return switch (column) {
			case TextEnum.PROD_NAME -> textOld.getProdName();
			case TextEnum.CREATED -> CalendarWrapper.wrapToString(textOld.getCreated().getTime());
			case TextEnum.LAST_MODIFIED -> CalendarWrapper.wrapToString(textOld.getLastModified().getTime());
			case TextEnum.PROD_CHARS -> MeasureUnit.getToUnit(textOld.getProdChars());
			case TextEnum.TEXT_CHARS -> MeasureUnit.getToUnit(textOld.getTextChars());
			case TextEnum.CHARS_TOTAL -> MeasureUnit.getToUnit(textOld.getCharsTotal());
			case TextEnum.TEXT_NAME -> textOld.getTextName();
			case TextEnum.FILE -> textOld.getPath();
		};
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return columnIndex == TextEnum.CHARS_TOTAL.ordinal() || columnIndex == TextEnum.TEXT_NAME.ordinal();
	}

	@Override
	public String getColumnName(int column) {
		return header[column];
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		TextOld textOld = data.get(rowIndex);
		if (aValue.toString().isEmpty()) {
			aValue = 0;
		}
		TextEnum column = TextEnum.values()[columnIndex];
		switch (column) {
			case TextEnum.CHARS_TOTAL -> {
				for (TextOld prod : data) {
					if (prod.getPath().equals(textOld.getPath())) {
						prod.setCharsTotal(Double.parseDouble(aValue.toString()));
						SettingsController.setCharsTotal(Double.parseDouble(aValue.toString()), prod.getPath());
					}
				}
				fireTableDataChanged();
			}
			case TextEnum.TEXT_NAME -> {
				for (TextOld prod : data) {
					if (prod.getPath().equals(textOld.getPath())) {
						prod.setTextName(aValue.toString());
						SettingsController.setTextName(aValue.toString(), prod.getPath());
					}
				}
				fireTableDataChanged();
			}
			default -> throw new IllegalArgumentException();
		}
	}
}
