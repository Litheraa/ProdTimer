package litheraa;

import litheraa.data.calendar.Calendar;
import litheraa.data_base.HSQLDBWorker;
import litheraa.data.Routine;
import litheraa.data.Text;
import litheraa.util.ViewType;
import litheraa.util.readers.FileTypes;
import litheraa.view.*;
import litheraa.view.message.Tip;
import lombok.Getter;

import javax.swing.*;
import java.util.*;

@Getter
public class ProdTimerController implements ProdTimerControllerInterface {
	private final ViewController viewController;
	private final FileTypes fileTypes = new FileTypes();

	public ProdTimerController() {
		createDB();
		viewController = new ViewController(this);
		saveData();
		viewController.getView();
		saveDataByTimer();
		setTrayIcon(false);
	}

	public void setView(ViewType viewType) {
		SettingsController.setViewType(viewType);
	}


	@Override
	public void createDB() {
		HSQLDBWorker.createTexts();
		HSQLDBWorker.createRoutine();
	}

	@Override
	public void setAutoStart(boolean isAutoStart) {
		Core.autoRun(isAutoStart);
		SettingsController.switchAutoStart();
	}

	@Override
	public void fullSave() {
		saveData();
		viewController.saveColumnPositions();
		SettingsController.saveToFile();
	}

	@Override
	public void saveData() {
		if (SettingsController.isDirectoriesEmpty()) {
			Tip.forceShowTip(viewController.getMainFrame());
		} else {
			Core.setController(this);
			HSQLDBWorker.upsertTexts(Objects.requireNonNull(
					Core.collectTextsData(SettingsController.collectTextPath())));
			HSQLDBWorker.upsertRoutine();
		}
	}

	@Override
	public ArrayList<Text> getTextsData() {
		return HSQLDBWorker.selectTexts();
	}

	@Override
	public ArrayList<Routine> getRoutineData() {
		return HSQLDBWorker.selectRoutine();
	}

	public Calendar getCalendarData(int year, int month) {
		return HSQLDBWorker.selectCalendar(year, month);
	}

	public Calendar getCalendarData() {
		return HSQLDBWorker.selectCalendar(Calendar.getTodayYear(), Calendar.getTodayMonth());
	}

	public void setCalendarGoal(Calendar calendar, int day) {
		HSQLDBWorker.updateCalendar(calendar, day);
	}

	public ArrayList<Integer> getCalendarYears() {
		return HSQLDBWorker.selectYears();
	}

	@Override
	public void saveDataByTimer() {
		DataSaver.setController(this);
		DataSaver.saveData();
	}

	public void notFilesFound() {
		int result = MainFrame.getErrorMessage("Не найдено файлов с расширениями " + Arrays.toString(fileTypes.getWildCards()) + ". Проверьте настройки " + SettingsController.getPathToDirectories());
		if (result == JOptionPane.YES_OPTION) {
			chooseFile();
		}
	}

	public void chooseFile() {
		FileChooser.chooseFile(viewController.getMainFrame(), this);
	}

	public void refresh() {
		saveData();
		viewController.refresh();
	}

	public void reset() {
		SettingsController.loadDefault();
		viewController.refresh();
	}

	public void setTrayIcon(boolean isForced) {
		try {
			TrayView tray = new TrayView();
			if (SettingsController.isTrayEnabled() || isForced) {
				tray.setValue(HSQLDBWorker.selectTodayChars()).build(this);
			} else {
				tray.disable();
			}
		} catch (RuntimeException ignored) {
		}
	}
}