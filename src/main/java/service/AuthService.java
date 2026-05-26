package service;

import config.DatabaseConfig;

import java.sql.Connection;
import java.sql.Statement;

public class AuthService {

    public void createTable() {

        String sql = """
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                full_name TEXT NOT NULL,
                username TEXT UNIQUE NOT NULL,
                email TEXT UNIQUE NOT NULL,
                phone TEXT,
                password TEXT NOT NULL,
                avatar_url TEXT,
                created_at DATETIME DEFAULT CURRENT_TIMESTAMP
            )
        """;

        try (
                Connection connection =
                        DatabaseConfig.connect();

                Statement statement =
                        connection.createStatement()
        ) {

            statement.execute(sql);

            System.out.println("Users table created");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}