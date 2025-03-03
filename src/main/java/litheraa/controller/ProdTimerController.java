package litheraa.controller;

import litheraa.*;
import litheraa.data.TextFinder;
import litheraa.data.TextOld;
import litheraa.data.models.ProdTimeModel;
import litheraa.data_base.HSQLDBWorker;
import litheraa.data.RoutineOld;
import litheraa.util.SpringContextReaders;
import litheraa.util.ViewType;
import litheraa.util.readers.ReaderFactory;
import litheraa.view.*;
import litheraa.view.message.Tip;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Controller;

import javax.swing.*;
import java.io.File;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@Controller
@Getter
public class ProdTimerController implements ProdTimerControllerInterface {
	private final ViewController viewController;
	private final RepositoryController repositoryController;

	@Autowired
	public ProdTimerController(RepositoryController repositoryController) {
		this.repositoryController = repositoryController;
		new AnnotationConfigApplicationContext(SpringContextReaders.class).getBean(ReaderFactory.class);
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
		RepositoryController.autoRun(isAutoStart);
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
			try {
				RepositoryController.collectData(TextFinder.findProd(SettingsController.collectTextPath()));
			} catch (NullPointerException e) {
				log.error("e: ", e);
				noFilesFound();
			}
		}
	}

	@Override
	public ArrayList<TextOld> getTextsData() {
		return HSQLDBWorker.selectTexts();
	}

	@Override
	public ArrayList<RoutineOld> getRoutineData() {
		return HSQLDBWorker.selectRoutine();
	}

	public ProdTimeModel getDataByPeriod(LocalDate from, LocalDate to) {
		return new ProdTimeModel(RepositoryController.getDataByPeriod(from, to).getFirst(),
				RepositoryController.getDataByPeriod(from, to).getSecond(), from, to);
	}

	public void setGoal(int goal, Long... dayId) {
		RepositoryController.setGoal(goal, dayId);
	}

	public Set<Integer> getUniqueYears() {
		return RepositoryController.getUniqueYears();
	}

	@Override
	public void saveDataByTimer() {
		DataSaver.setController(this);
		DataSaver.saveData();
	}

	public void noFilesFound() {
		int result = MainFrame.getErrorMessage("Не найдено файлов с расширениями " +
				Arrays.toString(ReaderFactory.getWildCards()) +
				". Проверьте настройки " +
				SettingsController.getPathToDirectories());
		if (result == JOptionPane.YES_OPTION) {
			chooseFile();
		}
	}

	public void chooseFile() {
		File file = FileChooser.chooseFile(viewController.getMainFrame(), ReaderFactory.getReaders());
		if (file != null) {
			SettingsController.setProdDirectory(file);
			refresh();
		}
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