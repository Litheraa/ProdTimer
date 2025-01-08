package litheraa.view.themes;

import com.github.weisj.darklaf.theme.SolarizedLightTheme;
import litheraa.view.DateChooseDialog;

import java.awt.*;

public class SolarizedLight extends SolarizedLightTheme implements ThemeColors {
	@Override
	public void getColors(DateChooseDialog dateChooseDialog) {
		Color textColor = new Color(46, 78, 88);
		dateChooseDialog.setMonthStringForeground(textColor);
		dateChooseDialog.setMonthStringBackground(new Color(253, 246, 227));
		dateChooseDialog.setDaysOfTheWeekForeground(textColor);
		dateChooseDialog.setDayForeground(1, Color.BLACK);
		dateChooseDialog.setDayForeground(7, Color.BLACK);
		dateChooseDialog.setForeground(textColor);
		dateChooseDialog.setBackground(new Color(238, 232, 213));
	}
}
