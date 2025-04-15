package litheraa.util;

import lombok.Getter;

@Getter
public enum ViewType {
	TEXTS("По текстам"),
	TIME("По дням"),
	MONTHLY("Один месяц"),
	WEEKLY("Недельный вид"),
	DAILY("Компактный");

	private final String locale;

	ViewType(String locale) {
		this.locale = locale;
	}

	public static ViewType of(String period) {
		switch (period.charAt(2)) {
			case 'н': if (period.charAt(3) == 'е') {
				return WEEKLY;
			} else return MONTHLY;
			case ' ': return DAILY;
			default: return MONTHLY;
		}
	}
}
