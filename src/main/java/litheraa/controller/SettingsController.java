package litheraa.controller;

import litheraa.util.ViewType;
import litheraa.view.util.ColumnKeysRecord;
import litheraa.util.KeysRecord;
import litheraa.util.SettingsUtil;

import javax.swing.*;
import java.io.File;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.stream.IntStream;

public class SettingsController extends SettingsUtil {
	private static final KeysRecord KEYS = new KeysRecord();
	private static final ColumnKeysRecord COLUMN_KEYS = new ColumnKeysRecord();

	public static void loadDefault() {
		SettingsUtil.loadDefault();
	}

	public static void setProdDirectory(File directory) {
		SettingsUtil.setPath(directory);
	}


	public static LinkedList<Path> collectTextPath() {
		return SettingsUtil.getPath();
	}

	public static String getPathToDirectories() {
		return SettingsUtil.getPATHS_FILE().toString();
	}

	public static void setDeadlineTime(String time) {
		SettingsUtil.set(KEYS.deadline(), time);
	}

	public static String getDeadLineTime() {
		String time = SettingsUtil.get(KEYS.deadline());
		String[] timeSplit = time.matches("^[0-9]{2}:[0-9]{2}$") ? time.split(":") : SettingsUtil.loadDefault(KEYS.deadline()).split(":");
		IntStream.range(0, timeSplit.length).filter(i -> timeSplit[i].startsWith("0")).forEach(i -> timeSplit[i] = timeSplit[i].substring(1));
		return timeSplit[0] + ":" + timeSplit[1];
	}

	public static void setProdVolume(String volume) {
		SettingsUtil.set(KEYS.prodVolume(), volume);
	}

	public static int getProdGoal() {
		String prodVolume = SettingsUtil.get(KEYS.prodVolume());
		if (prodVolume.matches("^[0-9]+[:/-]?")) {
			return Integer.parseInt(SettingsUtil.loadDefault(KEYS.prodVolume()));
		}
		return Integer.parseInt(prodVolume);
	}

	public static int getProdNameLength() {
		String nameLength = SettingsUtil.get(KEYS.prodNameLength());
		if (!nameLength.matches("^[0-9]+$") && Integer.parseInt(nameLength) > 50) {
			return Integer.parseInt(SettingsUtil.loadDefault(KEYS.prodNameLength()));
		}
		return Integer.parseInt(nameLength);
	}

	public static void setSortedColumn(int column) {
		switch (getViewType()) {
			case ViewType.TEXTS -> SettingsUtil.set(KEYS.sortTextColumn(), String.valueOf(column));
			case ViewType.TIME -> SettingsUtil.set(KEYS.sortRoutineColumn(), String.valueOf(column));
			default -> throw new IllegalStateException("Unexpected tableType: " + SettingsUtil.get(KEYS.tableType()));
		}
	}

	public static int getSortedColumn() {
		return switch (getViewType()) {
			case ViewType.TEXTS -> Integer.parseInt(SettingsUtil.get(KEYS.sortTextColumn()));
			case ViewType.TIME -> Integer.parseInt(SettingsUtil.get(KEYS.sortRoutineColumn()));
			default -> Integer.parseInt(SettingsUtil.loadDefault(KEYS.sortTextColumn()));
		};
	}

	public static void setSortOrder(SortOrder order) {
		switch (getViewType()) {
			case ViewType.TEXTS -> SettingsUtil.set(KEYS.sortTextOrder(), String.valueOf(order));
			case ViewType.TIME -> SettingsUtil.set(KEYS.sortRoutineOrder(), String.valueOf(order));
			default -> throw new IllegalStateException("Unexpected tableType: " + SettingsUtil.get(KEYS.tableType()));
		}
	}

	public static SortOrder getSortOrder() {
		return switch (getViewType()) {
			case ViewType.TEXTS -> SortOrder.valueOf(SettingsUtil.get(KEYS.sortTextOrder()));
			case ViewType.TIME -> SortOrder.valueOf(SettingsUtil.get(KEYS.sortRoutineOrder()));
			default -> SortOrder.valueOf(SettingsUtil.loadDefault(KEYS.sortTextOrder()));
		};
	}

	public static boolean isChars() {
		return Integer.parseInt(SettingsUtil.get(KEYS.measureUnit())) == 1;
	}

	public static void switchMeasureUnit() {
		SettingsUtil.set(KEYS.measureUnit(), isChars() ? "0" : "1");
	}

	public static boolean isTablePinned() {
		return Integer.parseInt(SettingsUtil.get(KEYS.pinTable())) == 1;
	}

	public static void pinTable() {
		SettingsUtil.set(KEYS.pinTable(), isTablePinned() ? "0" : "1");
	}

	public static boolean isColumnHide(int columnIdentifier) {
		return Integer.parseInt(SettingsUtil.get(COLUMN_KEYS.getTextKey(columnIdentifier)).split("/")[0]) == -1;
	}

	public static boolean isAutoStart() {
		return Integer.parseInt(SettingsUtil.get(KEYS.autoRun())) == 1;
	}

	public static void switchAutoStart() {
		SettingsUtil.set(KEYS.autoRun(), isAutoStart() ? "0" : "1");
	}

	public static String getCutDate() {
		String cutDate = SettingsUtil.get(KEYS.cutDate());
		if (!cutDate.matches("^[0-9]{2}-[0-9]{2}-[0-9]{4}$")) {
			cutDate = SettingsUtil.loadDefault(KEYS.cutDate());
		}
		return cutDate;
	}

	public static boolean isTipsShow() {
		return Integer.parseInt(SettingsUtil.get(KEYS.showTips())) == 1;
	}

	public static void switchTipShow() {
		SettingsUtil.set(KEYS.showTips(), isTipsShow() ? "0" : "1");
	}

	public static boolean isDirectoriesEmpty() {
		boolean bool;
		try {
			bool = SettingsUtil.getPath().getFirst() == null;
		} catch (NoSuchElementException e) {
			return true;
		}
		return bool;
	}

	public static ViewType getViewType() {
		return switch (Integer.parseInt(SettingsUtil.get(KEYS.tableType()))) {
			case 0 -> ViewType.TEXTS;
			case 1 -> ViewType.TIME;
			case 3 -> ViewType.WEEKLY;
			case 4 -> ViewType.DAILY;
			default -> ViewType.MONTHLY;
		};
	}

	public static void setViewType(ViewType viewType) {
		SettingsUtil.set(KEYS.tableType(), Integer.toString(viewType.ordinal()));
	}

	public static void saveToFile() {
		SettingsUtil.saveToFile();
	}

	public static boolean isTrayEnabled() {
		return Integer.parseInt(SettingsUtil.get(KEYS.trayIcon())) >= 1;
	}

	public static boolean isTrayExit() {
		return Integer.parseInt(SettingsUtil.get(KEYS.trayIcon())) == 2;
	}

	public static void switchTray() {
		SettingsUtil.set(KEYS.trayIcon(), isTrayEnabled() ? "0" : "1");
	}

	public static void switchTrayExit() {
		SettingsUtil.set(KEYS.trayIcon(), !isTrayExit() ? "2" : "1");
	}

	public static boolean isOnTop() {
		return SettingsUtil.get(KEYS.onTop()).equals("1");
	}

	public static void switchOnTop() {
		SettingsUtil.set(KEYS.onTop(), isOnTop() ? "0" : "1");
	}

	public static int getThemeNo() {
		String themeNo = SettingsUtil.get(KEYS.theme());
		if (!themeNo.matches("^[0-3]$")) {
			return Integer.parseInt(SettingsUtil.loadDefault(KEYS.theme()));
		}
		return Integer.parseInt(themeNo);
	}

	public static void setTheme(int theme) {
		SettingsUtil.set(KEYS.theme(), String.valueOf(theme));
	}
}
