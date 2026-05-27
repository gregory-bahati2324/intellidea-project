package utils;

import java.security.MessageDigest;

public class PasswordUtil {

    public static String hash(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());

            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }

            return hexString.toString();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean verify(String rawPassword, String hashedPassword) {
        return hash(rawPassword).equals(hashedPassword);
    }

}