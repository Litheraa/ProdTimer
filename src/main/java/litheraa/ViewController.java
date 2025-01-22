package litheraa;

import com.github.weisj.darklaf.LafManager;
import com.github.weisj.darklaf.theme.Theme;
import litheraa.data.calendar.Calendar;
import litheraa.data_base.HSQLDBWorker;
import litheraa.util.ViewType;
import litheraa.view.*;
import litheraa.view.calendar.ProdCalendar;
import litheraa.view.calendar.ProgressContainer;
import litheraa.view.menu.TableMenuBar;
import litheraa.view.table.ColumnController;
import litheraa.view.table.ProdTimerTable;
import litheraa.view.table.RoutineModel;
import litheraa.view.table.TextModel;
import litheraa.view.util.Themes;

import java.awt.*;
import java.util.ArrayList;

public class ViewController {
	@lombok.Getter
	private final MainFrame mainFrame;
	@lombok.Getter
	private final ColumnController columnController;
	private final ProdTimerController controller;

	public ViewController(ProdTimerController controller) {
		this.controller = controller;
		mainFrame = new MainFrame(controller);
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
				mainFrame.setJMenuBar(menu);
				mainFrame.setSize(getWindowSize(ViewType.CALENDAR.ordinal()));
				mainFrame.setResizable(true);
				ProdCalendar calendar = new ProdCalendar(this, controller.getCalendarData());
				mainFrame.setMainComponent(calendar);
				mainFrame.setVisible(true);
				break;
			case ViewType.SMALL_WINDOW:
				menu = new TableMenuBar(controller).createSmallWindowMenu();
				mainFrame.setSize(250, 130);
				mainFrame.setResizable(false);
				mainFrame.setMainComponent(new ProgressContainer(HSQLDBWorker.selectTodayChars(), SettingsController.getProdVolume()).createHorizontalProgress());
				mainFrame.setJMenuBar(menu);
		}
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

	public Calendar getCalendarData(int year, int month) {
		return controller.getCalendarData(year, month);
	}

	public ArrayList<Integer> getCalendarYears() {
		return controller.getCalendarYears();
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
}
