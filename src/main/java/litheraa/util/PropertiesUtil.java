package litheraa.util;

import java.util.Properties;

public final class PropertiesUtil {
    private static final Properties properties = new Properties();

    public static void setConnectionProperties() {
        properties.put("spring.datasource.url", ProjectFolderUtil.getPROJECT_FOLDER() + "\\DB\\base");
    }
}

