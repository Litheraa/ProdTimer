package litheraa.util;

public record KeysRecord(String tableType, String prodVolume, String trayIcon, String onTop,
                         String location, String size,
                         String deadline, String prodNameLength, String updateInterval, String theme,
                         String sortTextColumn, String sortTextOrder, String sortRoutineColumn, String sortRoutineOrder,
                         String measureUnit, String pinTable, String autoRun, String cutDate, String showTips) {
    public KeysRecord() {
        this("table type", "prod volume", "tray icon", "on top",
                "location", "size",
                "deadline", "name length", "update interval", "theme",
                "sort text column", "sort text order", "sort routine column", "sort routine order",
                "measure unit", "pin table", "auto run", "cut date", "show tips");
    }
}
