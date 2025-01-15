package litheraa.view.util;

import com.github.weisj.darklaf.theme.*;
import litheraa.view.themes.Darcula;
import litheraa.view.themes.IntelliJ;
import litheraa.view.themes.OneDark;
import litheraa.view.themes.SolarizedLight;

import java.util.Arrays;

public class Themes {
	private static final Theme[] themes = new Theme[] {new IntelliJ(), new SolarizedLight(), new OneDark(), new Darcula()};

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
