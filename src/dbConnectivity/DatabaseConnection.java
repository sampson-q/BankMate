package dbConnectivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
     static String dbIP = "0.4";
     private static final String JDBC_URL = "jdbc:mysql://192.168." + dbIP + ":3306/b_mate";
     private static final String DB_USER = "hash";
     private static final String DB_PASSWORD = "b_mateProduction";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
    }
}

