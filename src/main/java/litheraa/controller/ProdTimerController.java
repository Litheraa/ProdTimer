package litheraa.controller;

import litheraa.*;
import litheraa.data.TextFinder;
import litheraa.data.TextOld;
import litheraa.data.entities.Text;
import litheraa.data.entities.Time;
import litheraa.data_base.HSQLDBWorker;
import litheraa.data.RoutineOld;
import litheraa.util.SpringContextReaders;
import litheraa.util.ViewType;
import litheraa.util.readers.ReaderFactory;
import litheraa.view.*;
import litheraa.view.message.Tip;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.util.Pair;
import org.jdesktop.swingx.JXLabel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Controller;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.List;

@Slf4j
@Controller
@Getter
public class ProdTimerController implements ProdTimerControllerInterface {
	private ViewControllerInterface viewController;
	private final RepositoryController repositoryController;
	private final MainFrame frame;

	@Autowired
	public ProdTimerController(RepositoryController repositoryController) {
		this.repositoryController = repositoryController;
		frame = new MainFrame(this);
		new AnnotationConfigApplicationContext(SpringContextReaders.class).getBean(ReaderFactory.class);
		createDB();
		createView(SettingsController.getViewType());
		saveData();
		saveDataByTimer();
		setTrayIcon(false);
	}

	public Component getFrame() {
		return viewController.getFrame();
	}

	public void exit() {
		saveWindowPosition();
		saveWindowSize();
		if (SettingsController.isTrayExit()) {
			frame.setVisible(false);
		} else {
			fullSave();
			System.exit(0);
		}
	}

	public void repaint() {
		frame.repaint();
	}

	public void saveWindowPosition() {
		SettingsController.setLocation((int) frame.getLocationOnScreen().getX(), (int) frame.getLocationOnScreen().getY());
	}

	public void saveWindowSize() {
		SettingsController.setSize(SettingsController.getViewType().ordinal(), frame.getWidth(), frame.getHeight());
	}

	public void createView(ViewType type) {
		switch (type) {
			case TEXTS, TIME -> viewController = new TableController(this, frame, type);
			default -> viewController = new CalendarController(this, frame, type);
		}
	}

	public void setView(ViewType type) {
		SettingsController.setViewType(type);
		createView(type);
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
//		viewController.saveColumnPositions();
		SettingsController.saveToFile();
	}

	@Override
	public void saveData() {
		if (SettingsController.isDirectoriesEmpty()) {
			Tip.forceShowTip(frame);
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

	public Pair<List<Time>, List<Text>> getData() {
		return RepositoryController.getData();
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
			JXLabel label = new JXLabel("Не найдено файлов с расширениями " +
					Arrays.toString(ReaderFactory.getWildCards()) +
					". Проверьте настройки " +
					SettingsController.getPathToDirectories());
			label.setLineWrap(true);

		int result =  JOptionPane.showOptionDialog(null,
					label,
					"Ошибка",
					JOptionPane.DEFAULT_OPTION,
					JOptionPane.PLAIN_MESSAGE, null, null, null);

		if (result == JOptionPane.YES_OPTION) {
			chooseFile();
		}
	}

	public void chooseFile() {
		File file = FileChooser.chooseFile(viewController.getFrame(), ReaderFactory.getReaders());
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