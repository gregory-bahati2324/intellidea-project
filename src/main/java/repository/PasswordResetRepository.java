package repository;

import config.DatabaseConfig;
import model.PasswordReset;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PasswordResetRepository {

    public void save(
            int userId,
            String token,
            String expiresAt
    ) {

        String sql = """
            INSERT INTO password_resets
            (
                user_id,
                token,
                expires_at
            )
            VALUES
            (
                ?,
                ?,
                ?
            )
        """;

        try (
                Connection conn = DatabaseConfig.connect();
                PreparedStatement stmt =
                        conn.prepareStatement(sql)
        ) {

            stmt.setInt(1, userId);
            stmt.setString(2, token);
            stmt.setString(3, expiresAt);

            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public PasswordReset findByToken(String token) {

        String sql =
                "SELECT * FROM password_resets WHERE token = ?";

        try (
                Connection conn = DatabaseConfig.connect();
                PreparedStatement stmt =
                        conn.prepareStatement(sql)
        ) {

            stmt.setString(1, token);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                PasswordReset reset =
                        new PasswordReset();

                reset.setId(rs.getInt("id"));
                reset.setUserId(rs.getInt("user_id"));
                reset.setToken(rs.getString("token"));
                reset.setExpiresAt(
                        rs.getString("expires_at")
                );

                return reset;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public void deleteByToken(String token) {

        String sql =
                "DELETE FROM password_resets WHERE token = ?";

        try (
                Connection conn = DatabaseConfig.connect();
                PreparedStatement stmt =
                        conn.prepareStatement(sql)
        ) {

            stmt.setString(1, token);

            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void deleteByUserId(int userId) {

        String sql =
                "DELETE FROM password_resets WHERE user_id = ?";

        try (
                Connection conn = DatabaseConfig.connect();
                PreparedStatement stmt =
                        conn.prepareStatement(sql)
        ) {

            stmt.setInt(1, userId);

            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}