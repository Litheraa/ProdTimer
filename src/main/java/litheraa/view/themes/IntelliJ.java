package litheraa.view.themes;

import com.github.weisj.darklaf.theme.IntelliJTheme;

import java.awt.*;

public class IntelliJ extends IntelliJTheme implements ThemeColors {
	private final Color ACCENT_FOREGROUND = Color.BLACK.brighter();
	private final Color ACCENT_BACKGROUND = Color.YELLOW.darker();
	private final Color FOREGROUND = new Color(70, 70, 70);
	private final Color BACKGROUND_LIGHT = Color.WHITE;
	private final Color BACKGROUND_DARK = new Color(242, 242, 242);
	private final Color SELECTION_BACKGROUND = new Color(38, 117, 191);
	private final Color SELECTION_FOREGROUND = new Color(225, 225, 225);

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
