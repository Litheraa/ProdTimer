package litheraa.data;

public enum RoutineEnum {
    DATE,
    PROD_CHARS,
    NAMES;

    public static ColumnDataTypeEnum getFieldType(int column) {
        RoutineEnum routineEnum = RoutineEnum.values()[column];
        return switch (routineEnum) {
            case DATE -> ColumnDataTypeEnum.DATE;
            case PROD_CHARS -> ColumnDataTypeEnum.DOUBLE;
            case NAMES -> ColumnDataTypeEnum.STRING;
        };
    }

    public static ColumnDataTypeEnum getFieldType(RoutineEnum routineField) {
        return switch (routineField) {
            case DATE -> ColumnDataTypeEnum.DATE;
            case PROD_CHARS -> ColumnDataTypeEnum.DOUBLE;
            case NAMES -> ColumnDataTypeEnum.STRING;
        };
    }
}
