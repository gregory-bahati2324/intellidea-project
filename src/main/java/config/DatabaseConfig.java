package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {

    private static final String URL =
            "jdbc:sqlite:src/main/java/database/app.db";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}