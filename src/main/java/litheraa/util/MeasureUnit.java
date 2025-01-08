package litheraa.util;

import litheraa.SettingsController;

import java.text.DecimalFormat;

public class MeasureUnit {
	private static final DecimalFormat INTEGER_FORMAT = new DecimalFormat("#########");
	private static final DecimalFormat DOUBLE_FORMAT = new DecimalFormat("###.###");

	public static String getToUnit(double value) {
		return isChars() ? INTEGER_FORMAT.format(value) : DOUBLE_FORMAT.format(value / 40000.0);
	}

	public static String getShortUnit(double value) {
		if (value >= 100000) {
			return "!!!";
		}
		return value < 1000 ? INTEGER_FORMAT.format(value) : INTEGER_FORMAT.format(value / 1000) + "K";
	}

	public static String inChars(double value) {
		return INTEGER_FORMAT.format(value);
	}

	public static int toChars(Object value) {
		double d = Double.parseDouble(value.toString().replace(",", "."));
		return isChars() ? Integer.parseInt(value.toString()) : Math.toIntExact(Math.round(d * 40000.0));
	}

	public static int toChars(double value) {
		return isChars() ? Math.toIntExact(Math.round(value)) : Math.toIntExact(Math.round(value * 40000.0));
	}

	private static boolean isChars() {
		return SettingsController.isChars();
	}
}
