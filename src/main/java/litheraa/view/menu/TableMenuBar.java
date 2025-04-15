package litheraa.view.menu;

import com.github.weisj.darklaf.LafManager;
import litheraa.controller.ProdTimerController;
import litheraa.controller.SettingsController;
import litheraa.util.ViewType;
import litheraa.view.TableDialog;
import litheraa.view.message.About;
import litheraa.view.message.Tip;
import litheraa.view.time_spinner.TimeSpinner;
import litheraa.view.util.ThemeSupplier;
import lombok.SneakyThrows;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.stream.IntStream;

public class TableMenuBar extends JMenuBar {

	private final ProdTimerController controller;
	private Point point;
	private JMenu viewMenu;

	public TableMenuBar(ProdTimerController controller) {
		this.controller = controller;
		add(createFileMenu());
		add(createViewMenu());
		add(createBehaviourMenu());
		add(createHelpMenu());
	}

	private JMenu createFileMenu() {
///		Spaces after menu names are added to make the gap between right edges of menu item and its containers
		JMenu fileMenu = new JMenu("Файл ");

		JMenuItem newProdMenuItem = new JMenuItem("Добавить проду  ");
		JMenuItem refresh = new JMenuItem("Обновить  ");
		JMenuItem exitMenuItem = new JMenuItem("Выход  ");

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
		int bound = viewMenu.getButtonAmount();
		IntStream.range(0, bound).forEachOrdered(i -> viewMenu.getButton(i).addActionListener(e -> {
			controller.saveWindowSize();
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

		RadioButtonsMenu colorThemeMenu = new RadioButtonsMenu("Цветовая тема",
				ThemeSupplier.getThemeNames(),
				SettingsController.getThemeNo());
		int bound1 = ThemeSupplier.getThemesLength();
		IntStream.range(0, bound1).forEachOrdered(i -> colorThemeMenu.getButton(i).addActionListener(e -> {
			SettingsController.setTheme(i);
			LafManager.installTheme(ThemeSupplier.getTheme(i));
			controller.refresh();
		}));

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
		this.viewMenu.add(colorThemeMenu);
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

		JCheckBoxMenuItem autoStartCheckbox = new JCheckBoxMenuItem("Автозапуск", SettingsController.isAutoStart());
		JCheckBoxMenuItem trayCheckBox = new JCheckBoxMenuItem("Иконка в трее", SettingsController.isTrayEnabled());
		trayCheckBox.setEnabled(!SettingsController.isTrayExit());
		JCheckBoxMenuItem exitInTrayCheckBox = new JCheckBoxMenuItem("При выходе сворачивать в трей", SettingsController.isTrayExit());
		JCheckBoxMenuItem showOnTop = new JCheckBoxMenuItem("Поверх остальных окон", SettingsController.isOnTop());

		startConditionsMenu.add(autoStartCheckbox);
		startConditionsMenu.add(trayCheckBox);
		startConditionsMenu.add(exitInTrayCheckBox);
		startConditionsMenu.add(showOnTop);

		JMenuItem cutDate = new JMenuItem("Не показывать тексты до");
		JMenuItem prodNameLengthMenuItem = new JMenuItem("Длина названия проды");
		JMenuItem prodVolumeMenuItem = new JMenuItem("Дневная норма знаков");
		JMenuItem prodDeadlineMenuItem = new JMenuItem("Сбрасывать таймер в");
		JMenuItem updateIntervalMenuItem = new JMenuItem("Автообновление");
		JMenuItem resetMenuItem = new JMenuItem("Сбросить настройки");


		behaviorMenu.add(startConditionsMenu);
		behaviorMenu.add(cutDate);
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

		cutDate.addActionListener(e -> {
			TableDialog dayChooser = new TableDialog(point);
			dayChooser.createDayChooserDialog("Не показывать тексты до",
					SettingsController.getCutDate(),
					new SettingsController(),
					(sC, string) -> SettingsController.setCutDate(string));
		});

		prodNameLengthMenuItem.addActionListener(e -> {
			TableDialog dialog = new TableDialog(point);
			dialog.createProdNameLengthDialog();
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

	private JMenu createHelpMenu() {
		JMenu helpMenu = new JMenu("Помощь");

		JMenuItem showTip = new JMenuItem("Показать подсказку");
		JCheckBoxMenuItem showTips = new JCheckBoxMenuItem("Показывать подсказки", SettingsController.isTipsShow());
		JMenuItem about = new JMenuItem("О программе");

		helpMenu.add(showTip);
		helpMenu.add(showTips);
		helpMenu.add(about);

		showTip.addActionListener(e -> Tip.forceShowTip(controller.getFrame()));
		showTips.addActionListener(e -> SettingsController.switchTipShow());
		about.addActionListener(e -> About.showAbout(controller.getFrame()));
		return helpMenu;
	}
}
