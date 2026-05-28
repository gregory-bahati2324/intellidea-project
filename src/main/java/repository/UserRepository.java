package repository;

import config.DatabaseConfig;
import model.User;
import utils.PasswordUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserRepository {

    public boolean emailExists(String email) {
        String sql = "SELECT id FROM users WHERE email = ?";

        try (Connection conn = DatabaseConfig.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean usernameExists(String username) {
        String sql = "SELECT id FROM users WHERE username = ?";

        try (Connection conn = DatabaseConfig.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public void save(User user) {

        String sql = """
            INSERT INTO users 
            (full_name, username, email, phone, password, avatar_url)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DatabaseConfig.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getFullName());
            stmt.setString(2, user.getUsername());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPhone());
            stmt.setString(5, user.getPassword());
            stmt.setString(6, user.getAvatarUrl());

            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public User findByEmailOrUsername(String identifier) {

        String sql = """
        SELECT * FROM users
        WHERE email = ? OR username = ?
    """;

        try (
                Connection conn = DatabaseConfig.connect();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setString(1, identifier);
            stmt.setString(2, identifier);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                User user = new User();

                user.setId(rs.getInt("id"));
                user.setFullName(rs.getString("full_name"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setPhone(rs.getString("phone"));
                user.setPassword(rs.getString("password"));
                user.setAvatarUrl(rs.getString("avatar_url"));
                user.setCreatedAt(rs.getString("created_at"));

                return user;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    public User findById(int id) {

        String sql = "SELECT * FROM users WHERE id = ?";

        try (
                Connection conn = DatabaseConfig.connect();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                User user = new User();

                user.setId(rs.getInt("id"));
                user.setFullName(rs.getString("full_name"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setPhone(rs.getString("phone"));
                user.setAvatarUrl(rs.getString("avatar_url"));
                user.setCreatedAt(rs.getString("created_at"));

                return user;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public void updateUser(int id, User user) {

        String sql = """
        UPDATE users
        SET full_name = ?,
            username = ?,
            email = ?,
            phone = ?,
            avatar_url = ?
        WHERE id = ?
    """;

        try (
                Connection conn = DatabaseConfig.connect();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setString(1, user.getFullName());
            stmt.setString(2, user.getUsername());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPhone());
            stmt.setString(5, user.getAvatarUrl());
            stmt.setInt(6, id);

            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public boolean emailExistsForAnotherUser(
            String email,
            int currentUserId
    ) {

        String sql = """
        SELECT id FROM users
        WHERE email = ?
        AND id != ?
    """;

        try (
                Connection conn = DatabaseConfig.connect();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setString(1, email);
            stmt.setInt(2, currentUserId);

            ResultSet rs = stmt.executeQuery();

            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean usernameExistsForAnotherUser(
            String username,
            int currentUserId
    ) {

        String sql = """
        SELECT id FROM users
        WHERE username = ?
        AND id != ?
    """;

        try (
                Connection conn = DatabaseConfig.connect();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setString(1, username);
            stmt.setInt(2, currentUserId);

            ResultSet rs = stmt.executeQuery();

            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public void updatePassword(int id, String password) {

        String sql = "UPDATE users SET password = ? WHERE id = ?";

        try (Connection conn = DatabaseConfig.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, password);
            stmt.setInt(2, id);

            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}