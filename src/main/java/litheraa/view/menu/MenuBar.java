package litheraa.view.menu;

import litheraa.controller.ProdTimerController;
import litheraa.controller.SettingsController;
import litheraa.util.ViewType;
import litheraa.view.TableDialog;
import litheraa.view.message.About;
import litheraa.view.message.Tip;
import litheraa.view.time_spinner.TimeSpinner;
import lombok.SneakyThrows;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.stream.IntStream;

public class MenuBar extends JMenuBar {

	private final ProdTimerController controller;
	private Point point;
	private JMenu viewMenu;

	public MenuBar(ProdTimerController controller) {
		this.controller = controller;
		setLayout(new MultiRowLayout());

		add(createFileMenu());
		add(createViewMenu());
		add(createBehaviourMenu());
		add(createHelpMenu());
	}

	private JMenu createFileMenu() {
///		Spaces after menu names are added to make the gap between right edges of menu item and its containers
		JMenu fileMenu = new JMenu("Файл ");

		JMenuItem newProdMenuItem = new ColorfulMenuItem("Добавить проду  ");
		JMenuItem refresh = new ColorfulMenuItem("Обновить  ");
		JMenuItem exitMenuItem = new ColorfulMenuItem("Выход  ");

		fileMenu.add(newProdMenuItem);
		fileMenu.add(refresh);
		fileMenu.addSeparator();
		fileMenu.add(exitMenuItem);

		newProdMenuItem.addActionListener(e -> controller.chooseFile());
		refresh.addActionListener(e -> controller.refresh());
		exitMenuItem.addActionListener(e -> controller.exit());

		return fileMenu;
	}

	private JMenu createViewMenu() {
		viewMenu = new JMenu("Вид");

		RadioButtonsMenu viewMenu = new RadioButtonsMenu("Переключиться",
				Arrays.stream(ViewType.values()).map(ViewType::getLocale).toArray(String[]::new),
				SettingsController.getViewType().ordinal());
		IntStream.range(0, ViewType.values().length).forEachOrdered(i -> viewMenu.getButton(i).addActionListener(e -> {
			controller.setView(ViewType.values()[i]);
			controller.refresh();
			viewMenu.setPopupMenuVisible(false);
			this.viewMenu.setPopupMenuVisible(false);
		}));
		viewMenu.getPopupMenu().addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				viewMenu.getPopupMenu().getComponent().contains(e.getPoint());
			}
		});

//		RadioButtonsMenu colorThemeMenu = new RadioButtonsMenu("Цветовая тема",
//				ThemeSupplier.getThemeNames(),
//				SettingsController.getThemeNo());
//		IntStream.range(0, ThemeSupplier.getThemesLength()).forEachOrdered(i -> colorThemeMenu.getButton(i).addActionListener(e -> {
//			SettingsController.setTheme(i);
//			LafManager.installTheme(ThemeSupplier.getTheme(i));
//			controller.refresh();
//		}));

		RadioButtonsMenu measureUnitMenu = new RadioButtonsMenu("Единица измерения",
				new String[] {"Знаки", "Алки"},
				(SettingsController.isChars() ? 0 : 1));
		for (int i = 0; i < 2; i++) {
			measureUnitMenu.getButton(i).addActionListener(e -> {
				SettingsController.switchMeasureUnit();
				controller.repaint();
				measureUnitMenu.setPopupMenuVisible(false);
				this.viewMenu.setPopupMenuVisible(false);
			});
		}

		this.viewMenu.add(viewMenu);
//		this.viewMenu.add(colorThemeMenu);
		this.viewMenu.addSeparator();
		this.viewMenu.add(measureUnitMenu);

		return this.viewMenu;
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

		JCheckBoxMenuItem autoStartCheckbox = new ColorfulCheckBoxMenuItem("Автозапуск", SettingsController.isAutoStart());
		JCheckBoxMenuItem trayCheckBox = new ColorfulCheckBoxMenuItem("Иконка в трее", SettingsController.isTrayEnabled());
		trayCheckBox.setEnabled(!SettingsController.isTrayExit());
		JCheckBoxMenuItem exitInTrayCheckBox = new ColorfulCheckBoxMenuItem("При выходе сворачивать в трей", SettingsController.isTrayExit());
		JCheckBoxMenuItem showOnTop = new ColorfulCheckBoxMenuItem("Поверх остальных окон", SettingsController.isOnTop());

		startConditionsMenu.add(autoStartCheckbox);
		startConditionsMenu.add(trayCheckBox);
		startConditionsMenu.add(exitInTrayCheckBox);
		startConditionsMenu.add(showOnTop);
		/// подгоняем поведение под ColorfulMenuItem
//		startConditionsMenu.setOpaque(true);

		JMenuItem prodNameLengthMenuItem = new ColorfulMenuItem("Длина названия проды");
		JMenuItem prodVolumeMenuItem = new ColorfulMenuItem("Дневная норма знаков");
		JMenuItem prodDeadlineMenuItem = new ColorfulMenuItem("Сбрасывать таймер в");
		JMenuItem updateIntervalMenuItem = new ColorfulMenuItem("Автообновление");
		JMenuItem resetMenuItem = new ColorfulMenuItem("Сбросить настройки");

		behaviorMenu.add(startConditionsMenu);
		behaviorMenu.add(prodNameLengthMenuItem);
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
		showOnTop.addActionListener(e -> SettingsController.switchOnTop());

		prodNameLengthMenuItem.addActionListener(e -> {
			TableDialog dialog = new TableDialog(prodNameLengthMenuItem, point);
			dialog.createProdNameLengthDialog(controller);
		});

		prodVolumeMenuItem.addActionListener(e -> {
			TableDialog dialog = new TableDialog(prodVolumeMenuItem, point);
			dialog.createProdVolumeDialog();
		});
		prodDeadlineMenuItem.addActionListener(e -> TimeSpinner.createDialog(TimeSpinner.getInstance(), point));

		updateIntervalMenuItem.addActionListener(e -> {
			TableDialog update = new TableDialog(updateIntervalMenuItem, point);
			update.createUpdateIntervalDialog(controller);
		});

		resetMenuItem.addActionListener(e -> controller.reset());

		return behaviorMenu;
	}

	private JMenu createHelpMenu() {
		JMenu helpMenu = new JMenu("Помощь");

		JMenuItem showTip = new ColorfulMenuItem("Показать подсказку");
		JCheckBoxMenuItem showTips = new ColorfulCheckBoxMenuItem("Показывать подсказки", SettingsController.isTipsShow());
		JMenuItem about = new ColorfulMenuItem("О программе");

		helpMenu.add(showTip);
		helpMenu.add(showTips);
		helpMenu.add(about);

		showTip.addActionListener(e -> Tip.forceShowTip(controller.getFrame()));
		showTips.addActionListener(e -> SettingsController.switchTipShow());
		about.addActionListener(e -> About.showAbout(controller.getFrame()));
		return helpMenu;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(UIManager.getColor("Menu.background"));
		g.fillRect(0, 0, getWidth(), getHeight());
	}
}
