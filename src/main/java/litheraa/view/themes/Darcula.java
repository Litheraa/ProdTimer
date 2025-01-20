package litheraa.view.themes;

import com.github.weisj.darklaf.theme.DarculaTheme;

import java.awt.*;

public class Darcula extends DarculaTheme implements ThemeColors {
	private final Color ACCENT_FOREGROUND = new Color(44, 44, 44);
	private final Color ACCENT_BACKGROUND = new Color(112, 146, 190);
	private final Color FOREGROUND = new Color(187, 187, 187);
	private final Color BACKGROUND_LIGHT = new Color(69, 73, 74);
	private final Color BACKGROUND_DARK = new Color(60, 63, 65);
	private final Color SELECTION_FOREGROUND = Color.WHITE;
	private final Color SELECTION_BACKGROUND = new Color(75, 110, 175);

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
