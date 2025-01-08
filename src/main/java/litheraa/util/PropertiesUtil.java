package litheraa.util;

import java.io.*;
import java.util.Properties;

public final class PropertiesUtil {

    private static final Properties properties = new Properties();

    static {
        loadConnectionProperties();
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }


    private static void loadConnectionProperties() {
        try (var resourceAsStream = PropertiesUtil.class.getClassLoader().getResourceAsStream("application.properties")) {
            properties.load(resourceAsStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

