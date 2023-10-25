package dbConnectivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // uncomment likes 9 and 10
     String dbIP = "0.4";
     private static final String JDBC_URL = "jdbc:mysql://192.168.:3306/b_mate";

    // comment line 13
    // private static final String JDBC_URL = "jdbc:mysql://localhost:3306/b_mate";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
    }
}

