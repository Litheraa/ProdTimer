package litheraa.controller;

import com.github.weisj.darklaf.LafManager;
import com.github.weisj.darklaf.theme.Theme;
import litheraa.data.entities.Text;
import litheraa.data.entities.Time;
import litheraa.data_base.HSQLDBWorker;
import litheraa.util.ViewType;
import litheraa.view.*;
import litheraa.view.calendar.*;
import litheraa.view.menu.TableMenuBar;
import litheraa.view.table.ColumnController;
import litheraa.view.table.ProdTimerTable;
import litheraa.view.table.RoutineModel;
import litheraa.view.table.TextModel;
import litheraa.view.util.Themes;
import org.apache.commons.math3.util.Pair;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Set;

public class ViewController {
	@lombok.Getter
	private final MainFrame mainFrame;
	@lombok.Getter
	private final ColumnController columnController;
	private final ProdTimerController controller;
	private final Pair<List<Time>, List<Text>> data;

	public ViewController(ProdTimerController controller) {
		mainFrame = new MainFrame(controller);
		this.controller = controller;
		this.data = controller.getData();
		mainFrame.setLocation(SettingsController.getLocation());
		columnController = new ColumnController(mainFrame);
		LafManager.installTheme(getTheme());
	}

	public void getView() {
		ProdTimerTable table;
		TableMenuBar menu;
		switch (SettingsController.getViewType()) {
			case ViewType.TEXTS:
				table = new ProdTimerTable(mainFrame, new TextModel(controller.getTextsData()));
				table.setProgress();
				menu = new TableMenuBar(controller).createTextsMenu();
				mainFrame.setMainComponent(table);
				mainFrame.setJMenuBar(menu);
				columnController.reallignColumns();
				mainFrame.setSize(getWindowSize(ViewType.TEXTS.ordinal()));
				mainFrame.setVisible(true);
				mainFrame.setResizable(true);
				break;
			case ViewType.ROUTINE:
				table = new ProdTimerTable(mainFrame, new RoutineModel(controller.getRoutineData()));
				table.setTextArea();
				table.setProgressBar();
				menu = new TableMenuBar(controller).createRoutineMenu();
				mainFrame.setMainComponent(table);
				mainFrame.setJMenuBar(menu);
				columnController.reallignColumns();
				mainFrame.setSize(getWindowSize(ViewType.ROUTINE.ordinal()));
				mainFrame.setResizable(true);
				mainFrame.setVisible(true);
				break;
			case ViewType.CALENDAR:
				menu = new TableMenuBar(controller).createSmallWindowMenu();
				buildCalendar(SettingsController.getCalendarType());
				mainFrame.setJMenuBar(menu);
				mainFrame.setSize(getWindowSize(ViewType.CALENDAR.ordinal()));
				mainFrame.setResizable(true);
				mainFrame.setVisible(true);
				break;
			case ViewType.SMALL_WINDOW:
				menu = new TableMenuBar(controller).createSmallWindowMenu();
				mainFrame.setSize(250, 130);
				mainFrame.setResizable(false);
				mainFrame.setMainComponent(new ProgressContainer(HSQLDBWorker.selectTodayChars(), SettingsController.getProdGoal()).createHorizontalProgress());
				mainFrame.setJMenuBar(menu);
		}
	}

	public Pair<List<Time>, List<Text>> getData() {
		return controller.getData();
	}

	public void repaint() {
		mainFrame.setVisible(false);
		mainFrame.setVisible(true);
	}

	public void refresh() {
		getView();
		if (SettingsController.getViewType().ordinal() <= 1) {
			columnController.reallignColumns();
		}
		repaint();
	}

	public static Theme getTheme() {
		return Themes.getTheme(SettingsController.getThemeNo());
	}

	public void saveWindowPosition() {
		SettingsController.setLocation((int) mainFrame.getLocationOnScreen().getX(), (int) mainFrame.getLocationOnScreen().getY());
	}

	public void saveColumnPositions() {
		columnController.saveColumnPositions();
	}

	public Dimension getWindowSize(int viewNo) {
		return SettingsController.getSize(viewNo);
	}

	public void saveWindowSize() {
		SettingsController.setSize(SettingsController.getViewType().ordinal(), mainFrame.getWidth(), mainFrame.getHeight());
	}

	public void setGoal(int goal, Long... timeId) {
		controller.setGoal(goal, timeId);
	}

	public Set<Integer> getUniqueYears() {
		return controller.getUniqueYears();
	}

	public void reset() {
		controller.reset();
	}

	public void exit() {
		saveWindowPosition();
		saveWindowSize();
		if (SettingsController.isTrayExit()) {
			mainFrame.setVisible(false);
		} else {
			controller.fullSave();
			System.exit(0);
		}
	}

	public void buildCalendar(CalendarType type) {
		CalendarControllerInterface controller;
		switch (type) {
			case WEEKLY -> {
				controller = new WeeklyCalendarController(this, data);
				mainFrame.setMinimumSize(new Dimension(610, 215));
//				TODO отнимать высоту в лишних строках
				mainFrame.pack();
			}
			case DAILY -> {
				controller = new DailyCalendarController(this, data);
				mainFrame.setMinimumSize(new Dimension(270, 165));
				mainFrame.pack();
			}
			default -> {
				controller = new MonthlyCalendarController(this, data);
				mainFrame.setMinimumSize(new Dimension(632, 538));
			}
		}
		JPanel panel = controller.build();
		mainFrame.setMainComponent(panel);
		mainFrame.validate();
	}

	public enum CalendarType {
		MONTHLY,
		WEEKLY,
		DAILY;

		public static CalendarType of(int ordinal) {
			return switch (ordinal) {
				case 1 -> WEEKLY;
				case 2 -> DAILY;
				default -> MONTHLY;
			};
		}

		public static CalendarType of(String period) {
			 switch (period.charAt(2)) {
				case 'н': if (period.charAt(3) == 'е') {
					return WEEKLY;
				} else return MONTHLY;
				 case ' ': return DAILY;
				 default: return MONTHLY;
			}
		}
	}
}
