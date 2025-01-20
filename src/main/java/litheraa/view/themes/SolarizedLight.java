package litheraa.view.themes;

import com.github.weisj.darklaf.theme.SolarizedLightTheme;

import java.awt.*;

public class SolarizedLight extends SolarizedLightTheme implements ThemeColors {

	private final Color ACCENT_FOREGROUND = Color.BLACK;
	private final Color ACCENT_BACKGROUND = new Color(114, 141, 115);
	private final Color FOREGROUND = new Color(46, 78, 88);
	private final Color BACKGROUND_LIGHT = new Color(253, 246, 227);
	private final Color BACKGROUND_DARK = new Color(238, 232, 213);
	private final Color SELECTION_FOREGROUND = Color.WHITE;
	private final Color SELECTION_BACKGROUND = new Color(53, 112, 205);

	@Override
	public Color getBackgroundLight() {
		return BACKGROUND_LIGHT;
	}

	@Override
	public Color getForeground() {
		return FOREGROUND;
	}

	@Override
	public Color getSelectionForeground() {
		return SELECTION_FOREGROUND;
	}

	@Override
	public Color getSelectionBackground() {
		return SELECTION_BACKGROUND;
	}

	@Override
	public Color getBackgroundDark() {
		return BACKGROUND_DARK;
	}

	@Override
	public Color getAccentForeground() {
		return ACCENT_FOREGROUND;
	}

	@Override
	public Color getAccentBackground() {
		return ACCENT_BACKGROUND;
	}
}
