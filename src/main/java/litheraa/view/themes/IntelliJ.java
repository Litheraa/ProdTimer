package litheraa.view.themes;

import com.github.weisj.darklaf.theme.IntelliJTheme;
import litheraa.view.DateChooseDialog;

import java.awt.*;

public class IntelliJ extends IntelliJTheme implements ThemeColors {
	private final Color WEEKEND_COLOR = new Color(0, 0, 140);
	@lombok.Getter
	private final Color TEXT_COLOR = Color.BLACK;
	@Override
	public void getColors(DateChooseDialog dateChooseDialog) {
		dateChooseDialog.setMonthStringForeground(TEXT_COLOR);
		dateChooseDialog.setMonthStringBackground(Color.WHITE);
		dateChooseDialog.setDaysOfTheWeekForeground(TEXT_COLOR);
		dateChooseDialog.setDayForeground(1, WEEKEND_COLOR);
		dateChooseDialog.setDayForeground(7, WEEKEND_COLOR);
		dateChooseDialog.setForeground(TEXT_COLOR);
		dateChooseDialog.setBackground(new Color(242, 242, 242));
	}
}
