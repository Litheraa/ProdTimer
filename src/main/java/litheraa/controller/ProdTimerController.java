package litheraa.controller;

import com.formdev.flatlaf.FlatLaf;
import litheraa.*;
import litheraa.data.TextFinder;
import litheraa.data.entities.SelectableText;
import litheraa.data.entities.Text;
import litheraa.data.entities.Time;
import litheraa.data_base.HSQLDBWorker;
import litheraa.settings.DBSettings;
import litheraa.settings.SettingsManager;
import litheraa.util.ViewType;
import litheraa.util.readers.ReaderFactory;
import litheraa.view.*;
import litheraa.view.message.Tip;
import litheraa.view.themes.ThemeUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.util.Pair;
import org.jdesktop.swingx.JXLabel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Getter
@Controller
public class ProdTimerController implements ProdTimerControllerInterface {
	private ViewControllerInterface viewController;
	private final ReaderFactory readerFactory;
	private final RepositoryController repositoryController;
	private final MainFrame frame;
	private final DBSettings dbSettings;

	@Autowired
	public ProdTimerController(RepositoryController repositoryController, ReaderFactory readerFactory) {
		FlatLaf.setUseNativeWindowDecorations(true);
		ThemeUtil.applyTheme("star sky");
		UIManager.put("TitlePane.menuBarEmbedded", false);


		this.repositoryController = repositoryController;
		this.readerFactory = readerFactory;
		this.dbSettings = SettingsManager.loadDBSettings();
		frame = new MainFrame(this);

		createView(SettingsController.getViewType());
		saveData();
		saveDataByTimer();
		setTrayIcon(false);
	}

	public Component getFrame() {
		return viewController.getFrame();
	}

	public void exit() {
		dbSettings.setTextIds(viewController.getSelectableTexts().stream()
				.filter(sT -> !sT.isSelected())
				.map(sT -> sT.getText().getId())
				.collect(Collectors.toSet()));

		SettingsManager.saveSettings(SettingsController.getViewType(), frame, dbSettings);

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
				log.error("Ошибка сохранения данных: ", e);
				noFilesFound();
			}
		}
	}

	public Pair<List<Time>, ArrayList<SelectableText>> getData() {
		Pair<List<Time>, List<Text>> data = RepositoryController.getData();

		return new Pair<>(data.getFirst(), data.getSecond().stream()
				.map(text -> new SelectableText(text, !dbSettings.getTextIds().contains(text.getId())))
				.collect(Collectors.toCollection(ArrayList::new)));
	}

	public void setGoal(int goal, Long... dayId) {
		RepositoryController.setGoal(goal, dayId);
	}

	public Set<Integer> getUniqueYears() {
		return RepositoryController.getUniqueYears();
	}

	@Override
	public void saveDataByTimer() {
		Scheduler.setController(this);
		Scheduler.saveData();
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
		frame.setTray(!isForced);
		try {
			TrayView tray = new TrayView();
			if (!isForced) {
				tray.setValue(HSQLDBWorker.selectTodayChars()).build(this);
			} else {
				tray.disable();
			}
		} catch (RuntimeException ignored) {
		}
	}
}