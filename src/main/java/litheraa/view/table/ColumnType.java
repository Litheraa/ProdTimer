package litheraa.view.table;

import litheraa.data.ColumnDataTypeEnum;

public interface ColumnType {
    ColumnDataTypeEnum getColumnType(int column);
    int getExactModelType();
}
