package litheraa.view.themes;

import com.github.weisj.darklaf.theme.OneDarkTheme;
import lombok.Getter;

import java.awt.*;

public class OneDark extends OneDarkTheme implements ThemeColors {
	private final Color ACCENT_FOREGROUND = new Color(180, 185, 197);
	private final Color ACCENT_BACKGROUND = new Color(69, 105, 137);
	private final Color FOREGROUND = new Color(215, 218, 224);
	private final Color BACKGROUND_LIGHT = new Color(40, 44, 52);
	private final Color BACKGROUND_DARK = new Color(33, 37, 43);
	private final Color SELECTION_FOREGROUND = Color.WHITE;
	private final Color SELECTION_BACKGROUND = new Color(77, 120, 204);
	@Getter
	private final Color SELECTION_MENU = new Color(50, 56, 68);

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
