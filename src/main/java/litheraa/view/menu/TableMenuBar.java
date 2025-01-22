package litheraa.view.menu;

import com.github.weisj.darklaf.LafManager;
import litheraa.data.RoutineEnum;
import litheraa.ProdTimerController;
import litheraa.SettingsController;
import litheraa.data.TextEnum;
import litheraa.util.ViewType;
import litheraa.view.table.ColumnController;
import litheraa.view.MainFrame;
import litheraa.view.TableDialog;
import litheraa.view.message.About;
import litheraa.view.message.Tip;
import litheraa.view.time_spinner.TimeSpinner;
import litheraa.view.util.Themes;
import lombok.SneakyThrows;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
//TODO подсветка радиобаттонов
public class TableMenuBar extends JMenuBar {

	private final ProdTimerController controller;
	private final ColumnController columnController;
	private final MainFrame mainFrame;
	private Point point;
	private JMenu viewMenu;

	public TableMenuBar(ProdTimerController controller) {
		this.mainFrame = controller.getViewController().getMainFrame();
		this.controller = controller;
		this.columnController = controller.getViewController().getColumnController();
	}

	private JMenu createFileMenu() {
///		Spaces after menu names are added to make gap between right edges of menu item and its containers
		JMenu fileMenu = new JMenu("Файл ");

		JMenuItem newProdMenuItem = new JMenuItem("Добавить проду ");
		JMenuItem refresh = new JMenuItem("Обновить ");
		JMenuItem exitMenuItem = new JMenuItem("Выход ");

		fileMenu.add(newProdMenuItem);
		fileMenu.add(refresh);
		fileMenu.addSeparator();
		fileMenu.add(exitMenuItem);

		newProdMenuItem.addActionListener(e -> controller.chooseFile());
		refresh.addActionListener(e -> controller.refresh());
		exitMenuItem.addActionListener(e -> controller.getViewController().exit());

		return fileMenu;
	}

	private JCheckBoxMenuItem createHideColumnCheckBox(int column, String checkBoxName) {
		JCheckBoxMenuItem checkBox = new JCheckBoxMenuItem(checkBoxName,
				SettingsController.isColumnHide(column));
		checkBox.addItemListener(e -> columnController.setColumn(
				column, checkBox.isSelected()));
		return checkBox;
	}

	private JMenu createRoutineTableMenu() {
		JMenu tableMenu = new JMenu("Таблица");

		String[] names = new String[]{"Скрыть дату", "Скрыть знаки", "Скрыть название текстов"};
		for (int i = 0; i < RoutineEnum.values().length; i++) {
			tableMenu.add(createHideColumnCheckBox(i, names[i]));
		}

		return tableMenu;
	}

	private JMenu createTextTableMenu() {
		JMenu tableMenu = new JMenu("Таблица");

		String[] names = new String[]{"Скрыть начало проды",
				"Скрыть дату создания",
				"Скрыть дату последнего изменения",
				"Скрыть знаки в проде",
				"Скрыть знаки в тексте",
				"Скрыть знаков всего",
				"Скрыть название текста",
				"Скрыть путь к тексту"};
		for (int i = 0; i < TextEnum.values().length; i++) {
			tableMenu.add(createHideColumnCheckBox(i, names[i]));
		}

		return tableMenu;
	}

	private JMenu createTableViewMenu() {
		JCheckBoxMenuItem pinElementsCheckbox = new JCheckBoxMenuItem("Закрепить элементы",
				SettingsController.isTablePinned());
		pinElementsCheckbox.addChangeListener(e -> {
			SettingsController.pinTable();
			mainFrame.pinTable(pinElementsCheckbox.isSelected());
		});

		viewMenu = createViewMenu();
		viewMenu.add(pinElementsCheckbox);

		return viewMenu;
	}

	private JMenu createViewMenu() {
		viewMenu = new JMenu("Вид");

		RadioButtonsMenu changeViewMenu = new RadioButtonsMenu("Переключиться",
				ViewType.getNamesArray(),
				SettingsController.getViewType().ordinal());
		for (int i = 0; i < changeViewMenu.getBUTTONS_AMOUNT(); i++) {
			int finalI = i;
			changeViewMenu.getButton(i).addActionListener(e -> {
				controller.getViewController().saveWindowSize();
				controller.setView(ViewType.values()[finalI]);
				controller.refresh();
				changeViewMenu.setPopupMenuVisible(false);
				viewMenu.setPopupMenuVisible(false);
			});
		}
		changeViewMenu.getPopupMenu().addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				changeViewMenu.getPopupMenu().getComponent().contains(e.getPoint());
			}
		});

		RadioButtonsMenu colorThemeMenu = new RadioButtonsMenu("Цветовая тема",
				Themes.getThemeNames(),
				SettingsController.getThemeNo());
		for (int i = 0; i < Themes.getThemesLength(); i++) {
			int finalI = i;
			colorThemeMenu.getButton(i).addActionListener(e -> {
				SettingsController.setTheme(finalI);
				LafManager.installTheme(Themes.getTheme(finalI));
				controller.refresh();
			});
		}

		RadioButtonsMenu measureUnitMenu = new RadioButtonsMenu("Единица измерения",
				new String[] {"Знаки", "Алки"},
				(SettingsController.isChars() ? 0 : 1));
		for (int i = 0; i < 2; i++) {
			measureUnitMenu.getButton(i).addActionListener(e -> {
				SettingsController.switchMeasureUnit();
				mainFrame.repaint();
				measureUnitMenu.setPopupMenuVisible(false);
				viewMenu.setPopupMenuVisible(false);
			});
		}

		viewMenu.add(changeViewMenu);
		viewMenu.add(colorThemeMenu);
		viewMenu.addSeparator();
		viewMenu.add(measureUnitMenu);

		return viewMenu;
	}

	private JMenu createBehaviourMenu() {
		JMenu behaviorMenu = new JMenu("Поведение");
		behaviorMenu.addMouseListener(new MouseAdapter() {
			@SneakyThrows
			@Override
			public void mousePressed(MouseEvent e) {
				point = e.getPoint();
				SwingUtilities.convertPointToScreen(point, behaviorMenu);
			}
		});

		JMenu startConditionsMenu = new JMenu("Настройки запуска");

		JCheckBoxMenuItem autoStartCheckbox = new JCheckBoxMenuItem("Автозапуск", SettingsController.isAutoStart());
		JCheckBoxMenuItem trayCheckBox = new JCheckBoxMenuItem("Иконка в трее", SettingsController.isTrayEnabled());
		trayCheckBox.setEnabled(!SettingsController.isTrayExit());
		JCheckBoxMenuItem exitInTrayCheckBox = new JCheckBoxMenuItem("При выходе сворачивать в трей", SettingsController.isTrayExit());

		startConditionsMenu.add(autoStartCheckbox);
		startConditionsMenu.add(trayCheckBox);
		startConditionsMenu.add(exitInTrayCheckBox);

		JMenuItem prodVolumeMenuItem = new JMenuItem("Дневная норма знаков");
		JMenuItem prodDeadlineMenuItem = new JMenuItem("Сбрасывать таймер в");
		JMenuItem updateIntervalMenuItem = new JMenuItem("Автообновление");
		JMenuItem resetMenuItem = new JMenuItem("Сбросить настройки");

		behaviorMenu.add(startConditionsMenu);
		behaviorMenu.add(prodVolumeMenuItem);
		behaviorMenu.add(prodDeadlineMenuItem);
		behaviorMenu.add(updateIntervalMenuItem);
		behaviorMenu.addSeparator();
		behaviorMenu.add(resetMenuItem);

		autoStartCheckbox.addItemListener(e -> controller.setAutoStart(autoStartCheckbox.isSelected()));

		trayCheckBox.addActionListener(e -> {
			SettingsController.switchTray();
			controller.setTrayIcon(false);
		});

		exitInTrayCheckBox.addActionListener(e -> {
			SettingsController.switchTrayExit();
			controller.setTrayIcon(false);
			trayCheckBox.setEnabled(!SettingsController.isTrayExit());
		});

		prodVolumeMenuItem.addActionListener(e -> {
			TableDialog dialog = new TableDialog(point);
			dialog.createProdVolumeDialog();
		});
		prodDeadlineMenuItem.addActionListener(e -> TimeSpinner.createDialog(TimeSpinner.getInstance(), point));

		updateIntervalMenuItem.addActionListener(e -> {
			TableDialog update = new TableDialog(point);
			update.createUpdateIntervalDialog();
		});

		resetMenuItem.addActionListener(e -> controller.reset());

		return behaviorMenu;
	}

	private JMenu createTextBehaviourMenu() {
		JMenuItem prodNameLengthMenuItem = new JMenuItem("Длина названия проды");
		prodNameLengthMenuItem.addActionListener(e -> {
			TableDialog dialog = new TableDialog(point);
			dialog.createProdNameLengthDialog();
		});

		JMenu behaviorMenu = createRoutineBehaviourMenu();
		behaviorMenu.add(prodNameLengthMenuItem, 2);

		return behaviorMenu;
	}

	private JMenu createRoutineBehaviourMenu() {
		JMenuItem cutDate = new JMenuItem("Не показывать тексты до");
		cutDate.addActionListener(e -> {
			TableDialog dayChooser = new TableDialog(point);
			dayChooser.createDayChooserDialog();
		});

		JMenu behaviorMenu = createBehaviourMenu();
		behaviorMenu.add(cutDate, 1);

		return behaviorMenu;
	}

	private JMenu createSmallWindowBehaviourMenu() {

		JCheckBoxMenuItem showOnTop = new JCheckBoxMenuItem("Поверх остальных окон", SettingsController.isOnTop());
		showOnTop.addActionListener(e -> SettingsController.switchOnTop());

		JMenu behaviorMenu = createBehaviourMenu();
		((JMenu) behaviorMenu.getMenuComponent(0)).add(showOnTop);

		return behaviorMenu;
	}

	private JMenu createHelpMenu() {
		JMenu helpMenu = new JMenu("Помощь");

		JMenuItem showTip = new JMenuItem("Показать подсказку");
		JCheckBoxMenuItem showTips = new JCheckBoxMenuItem("Показывать подсказки", SettingsController.isTipsShow());
		JMenuItem about = new JMenuItem("О программе");

		helpMenu.add(showTip);
		helpMenu.add(showTips);
		helpMenu.add(about);

		showTip.addActionListener(e -> Tip.forceShowTip(TableMenuBar.this.mainFrame));

		showTips.addActionListener(e -> SettingsController.switchTipShow());

		about.addActionListener(e -> About.showAbout(TableMenuBar.this.mainFrame));
		return helpMenu;
	}

	public TableMenuBar createTextsMenu() {
		add(createFileMenu());
		add(createTableViewMenu());
		add(createTextTableMenu());
		add(createTextBehaviourMenu());
		add(createHelpMenu());

		return this;
	}

	public TableMenuBar createRoutineMenu() {
		add(createFileMenu());
		add(createTableViewMenu());
		add(createRoutineTableMenu());
		add(createRoutineBehaviourMenu());
		add(createHelpMenu());

		return this;
	}

	public TableMenuBar createSmallWindowMenu() {
		new TableMenuBar(controller);
		add(createFileMenu());
		add(createViewMenu());
		add(createSmallWindowBehaviourMenu());
		add(createHelpMenu());
		return this;
	}
}
