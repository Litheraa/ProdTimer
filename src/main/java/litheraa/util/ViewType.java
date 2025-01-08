package litheraa.util;

public enum ViewType {
	TEXTS,
	ROUTINE,
	CALENDAR,
	SMALL_WINDOW;

	public static String[] getNamesArray() {
		return new String[]{"По текстам", "По дням", "Календарный вид", "Компактное окно"};
	}
}
