package litheraa.view.themes;

import com.github.weisj.darklaf.theme.DarculaTheme;
import litheraa.view.DateChooseDialog;

import java.awt.*;

public class Darcula extends DarculaTheme implements ThemeColors {
	@Override
	public void getColors(DateChooseDialog dateChooseDialog) {
		Color weekEndsColor = new Color(140, 140, 140);
		Color textColor = new Color(187, 187, 187);
		dateChooseDialog.setMonthStringForeground(textColor);
		dateChooseDialog.setMonthStringBackground(new Color(69, 73, 74));
		dateChooseDialog.setDaysOfTheWeekForeground(textColor);
		dateChooseDialog.setDayForeground(1, weekEndsColor);
		dateChooseDialog.setDayForeground(7, weekEndsColor);
		dateChooseDialog.setForeground(textColor);
		dateChooseDialog.setBackground(new Color(60, 63, 65));
	}
}
