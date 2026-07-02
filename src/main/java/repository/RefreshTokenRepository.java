package repository;

import config.DatabaseConfig;
import model.RefreshToken;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class RefreshTokenRepository {

    public void save(
            int userId,
            String token,
            String expiresAt
    ) {

        String sql = """
                INSERT INTO refresh_tokens
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
                Connection conn =
                        DatabaseConfig.connect();

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

    public RefreshToken findByToken(
            String token
    ) {

        String sql =
                "SELECT * FROM refresh_tokens WHERE token = ?";

        try (
                Connection conn =
                        DatabaseConfig.connect();

                PreparedStatement stmt =
                        conn.prepareStatement(sql)
        ) {

            stmt.setString(1, token);

            ResultSet rs =
                    stmt.executeQuery();

            if (rs.next()) {

                RefreshToken refreshToken =
                        new RefreshToken();

                refreshToken.setId(
                        rs.getInt("id")
                );

                refreshToken.setUserId(
                        rs.getInt("user_id")
                );

                refreshToken.setToken(
                        rs.getString("token")
                );

                refreshToken.setExpiresAt(
                        rs.getString("expires_at")
                );

                return refreshToken;
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return null;
    }

    public void deleteByToken(
            String token
    ) {

        String sql =
                "DELETE FROM refresh_tokens WHERE token = ?";

        try (
                Connection conn =
                        DatabaseConfig.connect();

                PreparedStatement stmt =
                        conn.prepareStatement(sql)
        ) {

            stmt.setString(1, token);

            stmt.executeUpdate();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void deleteAllForUser(
            int userId
    ) {

        String sql =
                "DELETE FROM refresh_tokens WHERE user_id = ?";

        try (
                Connection conn =
                        DatabaseConfig.connect();

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