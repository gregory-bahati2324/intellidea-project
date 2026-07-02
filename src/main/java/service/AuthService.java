package service;

import config.DatabaseConfig;

import java.sql.Connection;
import java.sql.Statement;

public class AuthService {

    public void createTables() {

        createUsersTable();
        createRefreshTokensTable();
        createEmailVerificationTable();
        createPasswordResetTable();

    }

    private void createUsersTable() {

        String sql = """
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                full_name TEXT NOT NULL,
                username TEXT UNIQUE NOT NULL,
                email TEXT UNIQUE NOT NULL,
                phone TEXT,
                password TEXT NOT NULL,
                email_verified INTEGER DEFAULT 0,
                avatar_url TEXT,
                created_at DATETIME DEFAULT CURRENT_TIMESTAMP
            )
        """;

        execute(sql, "Users table created");
    }

    private void createRefreshTokensTable() {

        String sql = """
            CREATE TABLE IF NOT EXISTS refresh_tokens (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id INTEGER NOT NULL,
                token TEXT NOT NULL,
                expires_at DATETIME NOT NULL,
                created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY(user_id) REFERENCES users(id)
            )
        """;

        execute(sql, "Refresh tokens table created");
    }

    private void createEmailVerificationTable() {

        String sql = """
            CREATE TABLE IF NOT EXISTS email_verifications (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id INTEGER NOT NULL,
                token TEXT NOT NULL,
                expires_at DATETIME NOT NULL,
                created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY(user_id) REFERENCES users(id)
            )
        """;

        execute(sql, "Email verifications table created");
    }

    private void createPasswordResetTable() {

        String sql = """
            CREATE TABLE IF NOT EXISTS password_resets (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id INTEGER NOT NULL,
                token TEXT NOT NULL,
                expires_at DATETIME NOT NULL,
                created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY(user_id) REFERENCES users(id)
            )
        """;

        execute(sql, "Password resets table created");
    }

    private void execute(String sql, String message) {

        try (
                Connection connection = DatabaseConfig.connect();
                Statement statement = connection.createStatement()
        ) {

            statement.execute(sql);

            System.out.println(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}