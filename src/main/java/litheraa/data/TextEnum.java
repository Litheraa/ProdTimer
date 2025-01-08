package litheraa.data;

public enum TextEnum {
    PROD_NAME,
    CREATED,
    LAST_MODIFIED,
    PROD_CHARS,
    TEXT_CHARS,
    CHARS_TOTAL,
    TEXT_NAME,
    FILE;

    public static ColumnDataTypeEnum getFieldType(int column) {
        TextEnum textEnum = TextEnum.values()[column];
        return switch (textEnum) {
            case PROD_NAME, TEXT_NAME, FILE -> ColumnDataTypeEnum.STRING;
            case PROD_CHARS, TEXT_CHARS, CHARS_TOTAL -> ColumnDataTypeEnum.DOUBLE;
            case CREATED, LAST_MODIFIED -> ColumnDataTypeEnum.DATE;
        };
    }

    public static ColumnDataTypeEnum getFieldType(TextEnum textField) {
        return switch (textField) {
            case PROD_NAME, TEXT_NAME, FILE -> ColumnDataTypeEnum.STRING;
            case PROD_CHARS, TEXT_CHARS, CHARS_TOTAL -> ColumnDataTypeEnum.DOUBLE;
            case CREATED, LAST_MODIFIED -> ColumnDataTypeEnum.DATE;
        };
    }
}
