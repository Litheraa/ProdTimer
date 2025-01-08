package litheraa.data_base;

import litheraa.util.PropertiesUtil;

import java.sql.*;

public class PostgresSQLConnector {
    private static final String URL_KEY = "PostgresURL";
    private static final String USER_KEY = "PostgresUser";
    private static final String PASSWORD_KEY = "PostgresPassword";
    @lombok.Getter
    private static Connection connection;

    public static PreparedStatement getPreparedStatement(String query) throws SQLException {
        connection = DriverManager.getConnection(
                PropertiesUtil.get(URL_KEY),
                PropertiesUtil.get(USER_KEY),
                PropertiesUtil.get(PASSWORD_KEY));
        return connection.prepareStatement(query);
    }
}
