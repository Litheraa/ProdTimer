package litheraa.util;

public class NumberDeclensionRu {
	private static final String empty = "";
	private static final String a = "а";
	private static final String ov = "ов";
	private static final String o = "о";
	private static final String i = "и";
	private static final String ok = "ок";

	public static String decline(String text, int value) {
		String[] temp = text.split(" ");
		int ten = value % 100;
		int one = value % 10;
		boolean isMale = !temp[0].endsWith(a);

		if (Boolean.logicalOr(ten == 1, ten > 20 && one == 1)) {
			return isMale ? text : text.concat(a);
		}
		if (Boolean.logicalOr(ten >= 2 && ten <= 4, ten > 20 && (one >= 2 && one <= 4))) {
			String ret = empty;
			ret = isMale ? ret.concat(temp[0]).concat(a) : ret.concat(temp[0]).replace("а", i);
			return ret.concat(" ").concat(temp[1].concat(o));
		}
		return isMale ? temp[0].concat(ov).concat(" ").concat(temp[1]).concat(o) :
				temp[0].replace("ка", ok).concat(" ").concat(temp[1]).concat(o);
	}
}
