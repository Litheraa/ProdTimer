package litheraa.view.themes;

import com.github.weisj.darklaf.theme.OneDarkTheme;
import litheraa.view.DateChooseDialog;

import java.awt.*;

public class OneDark extends OneDarkTheme implements ThemeColors {
	@Override
	public void getColors(DateChooseDialog dateChooseDialog) {
		Color weekEndsColor = new Color(180, 185, 197);
		Color textColor = new Color(215, 218, 224);
		dateChooseDialog.setMonthStringForeground(textColor);
		dateChooseDialog.setMonthStringBackground(new Color(40, 44, 52));
		dateChooseDialog.setDaysOfTheWeekForeground(textColor);
		dateChooseDialog.setDayForeground(1, weekEndsColor);
		dateChooseDialog.setDayForeground(7, weekEndsColor);
		dateChooseDialog.setForeground(textColor);
		dateChooseDialog.setBackground(new Color(33, 37, 43));
	}
}
