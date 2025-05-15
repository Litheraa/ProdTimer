package litheraa.settings;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import litheraa.util.ViewType;
import litheraa.view.MainFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Slf4j
@Component
public class SettingsManager {
	private static final ObjectMapper mapper;
	private static File windowSettingsFile;

	static {
		mapper  = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
	}

	public static void saveSettings(ViewType type, MainFrame frame, DBSettings dbSettings) {
		windowSettingsFile = new File(type + ".json");
		try {
			mapper.writeValue(new File("settings.json"), dbSettings);
			mapper.writeValue(windowSettingsFile, new WindowSettings(frame));
		} catch (IOException e) {
			log.error("Ошибка записи в файл: ", e);
		}
	}

	public static DBSettings loadDBSettings() {
		try (FileInputStream settingsFile = new FileInputStream("settings.json")) {
			return mapper.readValue(settingsFile, DBSettings.class);
		} catch (IOException e) {
			log.error("Ошибка записи в файл: ", e);
			return new DBSettings();
		}
	}

	public static void loadSettings(ViewType type, MainFrame frame) {
		WindowSettings settings;
		try (FileInputStream settingsFile = new FileInputStream(type + ".json")) {
			settings = mapper.readValue(settingsFile, WindowSettings.class);
			frame.setBounds(settings.getX(), settings.getY(), settings.getWidth(), settings.getHeight());
			frame.setMinimumSize(settings.getMinSize());
			frame.setAlwaysOnTop(settings.isOnTop());
			frame.setResizable(settings.isResizable());
			frame.setTray(settings.isTray());
		} catch (IOException e) {
			try {
				settings = mapper.readValue(loadDefault(type), WindowSettings.class);
				frame.setLocationRelativeTo(null);
				frame.setSize(settings.getWidth(), settings.getHeight());
				frame.setMinimumSize(settings.getMinSize());
				frame.setAlwaysOnTop(settings.isOnTop());
				frame.setResizable(settings.isResizable());
				frame.setTray(settings.isTray());
			} catch (IOException ex) {
				log.error("Ошибка записи в файл: ", e);
			}
		}
	}

	private static File loadDefault(ViewType type) throws IOException {
		return switch (type) {
			case TEXTS ->
					new File("C:\\Users\\Lithera\\JAVAT2\\ProdTimer\\src\\main\\resources\\setting\\TextTableSettings.json");
			case TIME ->
					new File("C:\\Users\\Lithera\\JAVAT2\\ProdTimer\\src\\main\\resources\\setting\\TimeTableSettings.json");
			case WEEKLY ->
					new File("C:\\Users\\Lithera\\JAVAT2\\ProdTimer\\src\\main\\resources\\setting\\WeeklyCalendarSettings.json");
			case DAILY ->
					new File("C:\\Users\\Lithera\\JAVAT2\\ProdTimer\\src\\main\\resources\\setting\\DailyCalendarSettings.json");
			default ->
					new File("C:\\Users\\Lithera\\JAVAT2\\ProdTimer\\src\\main\\resources\\setting\\MonthlyCalendarSettings.json");
		};
	}
}
