package litheraa.view.util;

import com.github.weisj.darklaf.theme.*;
import litheraa.view.themes.Darcula;
import litheraa.view.themes.IntelliJ;
import litheraa.view.themes.OneDark;
import litheraa.view.themes.SolarizedLight;
import lombok.Getter;

import java.util.Arrays;

public class Themes {
	@Getter
	private static final Theme[] themes = new Theme[] {new IntelliJ(), new SolarizedLight(), new Darcula(), new OneDark()};

	public static Theme getTheme(int themeNo) {
		return themes[themeNo];
	}

	public static String[] getThemeNames() {
		return Arrays.stream(themes).map(Theme::getName).toArray(String[]::new);
	}
}
