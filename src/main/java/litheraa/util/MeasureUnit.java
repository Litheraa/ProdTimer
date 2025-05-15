package litheraa.util;

public enum MeasureUnit {
	CHAR("Знаки"),
	CHAR_WHITESPACE("Знаки с пробелами"),
	AUTHOR_PAGE("Авторские листы");

	private final String locale;

	MeasureUnit(String locale) {
		this.locale = locale;
	}
}
