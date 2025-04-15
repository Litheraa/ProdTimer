package litheraa.view.util;

import com.github.weisj.darklaf.theme.*;
import litheraa.controller.SettingsController;
import litheraa.view.themes.*;

import java.util.Arrays;

public class ThemeSupplier {
	private static final Theme[] themes;
	private static final ThemeColors[] themeColors;

	static {
		IntelliJ iJ = new IntelliJ();
		SolarizedLight sL = new SolarizedLight();
		OneDark oD = new OneDark();
		Darcula d = new Darcula();
		themes = new Theme[]{iJ, sL, oD, d};
		themeColors = new ThemeColors[]{iJ, sL, oD, d};
	}

	public static Theme getTheme() {
		return themes[SettingsController.getThemeNo()];
	}

	public static ThemeColors getThemeColor() {
		return themeColors[SettingsController.getThemeNo()];
	}

	public static Theme getTheme(int themeNo) {
		return themes[themeNo];
	}

	public static String[] getThemeNames() {
		return Arrays.stream(themes).map(Theme::getName).toArray(String[]::new);
	}

	public static int getThemesLength() {
		return themes.length;
	}
}
