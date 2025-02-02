package litheraa.view;

import litheraa.ProdTimerController;
import litheraa.util.MeasureUnit;
import litheraa.util.ProjectFolderUtil;
import lombok.AccessLevel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class TrayView {
	@lombok.Getter(AccessLevel.PRIVATE)
	private BufferedImage bI;
	private TrayIcon trayIcon;
	private String todayChars;

	{
		if (!SystemTray.isSupported()) {
			throw new RuntimeException("SystemTray is not supported");
		} else {
			bI = ProjectFolderUtil.getBufferedImage("writer");
		}
	}

	public TrayView setValue(double value) {
		todayChars = MeasureUnit.inChars(value);
		Graphics graphics = bI.createGraphics();
		String s = MeasureUnit.inChars(value);
		int fontSize = switch (s.length()) {
			case 1 -> 125;
			case 2 -> 96;
			case 4 -> 90;
			default -> 75;
		};
		graphics.setFont(new Font("Arial", Font.BOLD, fontSize));
		graphics.setColor(Color.BLACK);
		graphics.drawString(MeasureUnit.getShortUnit(value), 0, 100);
		graphics.dispose();
		return this;
	}

	public void build(ProdTimerController controller) {
		if (trayIcon == null) {
			MenuItem toFull = new MenuItem("Развернуть");
			MenuItem refresh = new MenuItem("Обновить");
			MenuItem exit = new MenuItem("Выход");

			toFull.addActionListener(e -> controller.getViewController().getMainFrame().setVisible(true));
			refresh.addActionListener(e -> controller.setTrayIcon(true));
			exit.addActionListener(e -> {
				controller.fullSave();
				System.exit(0);
			});

			PopupMenu menu = new PopupMenu();
			menu.add(toFull);
			menu.add(refresh);
			menu.addSeparator();
			menu.add(exit);
			trayIcon = new TrayIcon(getBI(), "Сегодня написано: " + todayChars, menu);
			trayIcon.setImageAutoSize(true);
			SystemTray tray = SystemTray.getSystemTray();
			try {
				tray.add(trayIcon);
			} catch (AWTException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public void disable() {
		SystemTray tray = SystemTray.getSystemTray();
		tray.remove(trayIcon);
	}
}
