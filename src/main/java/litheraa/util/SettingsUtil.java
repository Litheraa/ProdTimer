package litheraa.util;

import java.io.*;
import java.nio.file.Path;
import java.util.*;

public class SettingsUtil {
	@lombok.Getter
	private static final File PATHS_FILE = new File(String.valueOf(ProjectFolderUtil.getPathsFile()));
	private static final File SETTINGS_FILE = new File(String.valueOf(ProjectFolderUtil.getSettingsFile()));
	private static final String SEPARATOR = "=";
	private static final String PATH_KEY = "directory";
	private static final Map<String, String> SETTINGS = new HashMap<>();
	private static final Map<String, String> DEFAULT = new HashMap<>();
	private static final LinkedList<Path> PATHS = new LinkedList<>();

	static {
		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(ProjectFolderUtil.getDEFAULT_SETTINGS()))) {
			String s;
			while ((s = bufferedReader.readLine()) != null) {
				if (s.isBlank() || s.endsWith(SEPARATOR)) continue;
				String[] line = s.split(SEPARATOR);
				DEFAULT.put(line[0], line[1]);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(SETTINGS_FILE))) {
			String s;
			while ((s = bufferedReader.readLine()) != null) {
				if (s.isBlank() || s.endsWith(SEPARATOR)) continue;
				String[] line = s.split(SEPARATOR);
				SETTINGS.put(line[0], line[1]);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(PATHS_FILE))) {
			String s;
			while ((s = bufferedReader.readLine()) != null) {
				if (s.isBlank() || s.endsWith(SEPARATOR)) continue;
				String[] line = s.split(SEPARATOR);
				Path value = Path.of(line[1]);
				if (!PATHS.contains(value)) {
					PATHS.add(value);
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	protected static void set(String key, String value) {
		SETTINGS.put(key, value);
	}

	protected static String get(String key) {
		return SETTINGS.get(key) == null ? loadDefault(key) : SETTINGS.get(key);
	}

	protected static void setPath(File value) {
		Path path = value.toPath();
		if (PATHS.stream().noneMatch(path::startsWith)) {
			ArrayList<Path> temp = new ArrayList<>(PATHS.stream().filter(p -> !p.startsWith(path)).toList());
			temp.add(path);
			PATHS.clear();
			PATHS.addAll(temp);
		}
	}

	protected static LinkedList<Path> getPath() {
		return PATHS;
	}

	protected static void saveToFile() {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(SETTINGS_FILE))) {
			for (Map.Entry<String, String> entry : SETTINGS.entrySet()) {
				writer.write(entry.getKey() + SEPARATOR + entry.getValue());
				writer.newLine();
			}
			try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(PATHS_FILE))) {
				for (Path path : PATHS) {
					bufferedWriter.write(PATH_KEY + SEPARATOR + path.toString());
					bufferedWriter.newLine();
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	protected static void loadDefault() {
		SETTINGS.clear();
		SETTINGS.putAll(DEFAULT);
	}

	protected static String loadDefault(String settingKey) {
		String defaultValue = DEFAULT.get(settingKey);
		SETTINGS.put(settingKey, defaultValue);
		return defaultValue;
	}
}
