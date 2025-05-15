package litheraa.view.themes;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

public class ThemeUtil {
	private static final ObjectMapper mapper = new ObjectMapper();

	public static void loadThemeColors(String themeJson) {
		try (InputStream in = ThemeUtil.class.getClassLoader().getResourceAsStream(themeJson)) {
			if (in == null) {
				throw new IllegalArgumentException("Theme file not found: " + themeJson);
			}

			JsonNode root = mapper.readTree(in);
			JsonNode colors = root.get("colors");
			JsonNode ui = root.get("ui");

			if (ui != null && ui.isObject()) {
				for (Iterator<Map.Entry<String, JsonNode>> it = ui.fields(); it.hasNext(); ) {
					Map.Entry<String, JsonNode> entry = it.next();
					String component = entry.getKey();
					JsonNode props = entry.getValue();
					if (props.isObject()) {
						for (Iterator<Map.Entry<String, JsonNode>> propIt = props.fields(); propIt.hasNext(); ) {
							Map.Entry<String, JsonNode> prop = propIt.next();
							String value = prop.getValue().asText();
							if (value.startsWith("@")) {
								value = colors.get(value.substring(1)).asText();
							}
							if (value.startsWith("#")) {
								UIManager.put(component + "." + prop.getKey(), Color.decode(value));
							}
						}
					}
				}
			}

		} catch (IOException e) {
			throw new RuntimeException("Не могу прочитать тему: ", e);
		}
	}

	public static void applyTheme(String themeName) {
		try {
			FlatLaf.setup(new FlatDarkLaf());
			loadThemeColors("themes/" + themeName + ".theme.json");
			UIManager.put("Theme.name", themeName);
		} catch (Exception e) {
			throw new RuntimeException("Не могу загрузить тему: ", e);
		}
	}
}
