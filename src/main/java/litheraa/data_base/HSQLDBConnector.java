package litheraa.data_base;

import litheraa.util.ProjectFolderUtil;
import litheraa.util.PropertiesUtil;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class HSQLDBConnector extends ProjectFolderUtil {
    private static final String URL_KEY = "jdbc:hsqldb:file:" + getPROJECT_FOLDER() + "/DB/base";
    private static final String USER_KEY = "HSQLDBUser";
    private static final String PASSWORD_KEY = "HSQLDBPassword";
    @lombok.Getter
    private static Connection connection;

    @SneakyThrows
    public static PreparedStatement getPreparedStatement(String query) {
        connection = DriverManager.getConnection(
                URL_KEY,
                PropertiesUtil.get(USER_KEY),
                PropertiesUtil.get(PASSWORD_KEY));
        return connection.prepareStatement(query);
    }
}
