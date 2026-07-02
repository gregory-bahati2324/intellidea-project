package utils;

import java.util.UUID;

public class PasswordResetUtil {

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public static String expiryMinutes(int minutes) {
        return "datetime('now', '+"
                + minutes + " minutes')";
    }
}