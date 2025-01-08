package litheraa.view.table;

import litheraa.data.ColumnDataTypeEnum;
import litheraa.data.Routine;
import litheraa.data.RoutineEnum;
import litheraa.util.CalendarWrapper;
import litheraa.util.MeasureUnit;
import litheraa.util.ViewType;
import lombok.SneakyThrows;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class RoutineModel extends AbstractTableModel implements ColumnType{

        private final ArrayList<? extends Routine> data;
        private final String[] header = {"Дата", "Знаки", "В текстах"};

        public RoutineModel(ArrayList<? extends Routine> data) {
            this.data = data;
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
            Routine routine = data.get(rowIndex);
            RoutineEnum column = RoutineEnum.values()[columnIndex];

            return switch (column) {
                case RoutineEnum.DATE -> CalendarWrapper.wrapToString(routine.getLastModified().getTime());
                case RoutineEnum.PROD_CHARS -> MeasureUnit.getToUnit(routine.getProdChars());
                case RoutineEnum.NAMES -> routine.getTextNames().replaceAll("/", System.lineSeparator());
            };
        }

    @Override
        public String getColumnName(int column) {
            return header[column];
        }

    @Override
    public ColumnDataTypeEnum getColumnType(int column) {
        return RoutineEnum.getFieldType(column);
    }

    @Override
    public int getExactModelType() {
        return ViewType.ROUTINE.ordinal();
    }
}

