package litheraa.view.util;

public record ColumnKeysRecord(String tProdName, String tCreated, String tLastModified, String tProdChars,
                               String tTextChars, String tCharsTotal, String tTextName, String tFile,
                               String rDate, String rChars, String rNames) {

    public ColumnKeysRecord() {
        this("prod name", "created", "last modified", "prod chars",
                "text chars", "chars total", "text name", "file", "date", "chars", "routine names");
    }

    public String getTextKey(int columnIdentifier) {
        return switch (columnIdentifier) {
            case 0 -> tProdName();
            case 1 -> tCreated();
            case 2 -> tLastModified();
            case 3 -> tProdChars();
            case 4 -> tTextChars();
            case 5 -> tCharsTotal();
            case 6 -> tTextName();
            case 7 -> tFile();
            default -> throw new IllegalStateException("Unexpected columnIdentifier: " + columnIdentifier);
        };
    }

    public String getRoutineKey(int columnIdentifier) {
        return switch (columnIdentifier) {
            case 0 -> rDate();
            case 1 -> rChars();
            case 2 -> rNames();
            default -> throw new IllegalStateException("Unexpected columnIdentifier: " + columnIdentifier);
        };
    }
}