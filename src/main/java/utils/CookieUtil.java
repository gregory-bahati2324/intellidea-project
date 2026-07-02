package utils;

import com.sun.net.httpserver.HttpExchange;

public class CookieUtil {

    public static void setRefreshCookie(
            HttpExchange exchange,
            String refreshToken
    ) {

        String cookie =
                "refreshToken="
                        + refreshToken
                        + "; HttpOnly"
                        + "; Path=/"
                        + "; Max-Age=604800";

        exchange.getResponseHeaders()
                .add(
                        "Set-Cookie",
                        cookie
                );
    }

    public static void clearRefreshCookie(
            HttpExchange exchange
    ) {

        exchange.getResponseHeaders()
                .add(
                        "Set-Cookie",
                        "refreshToken=; Path=/; Max-Age=0"
                );
    }

    public static String getCookie(
            HttpExchange exchange,
            String cookieName
    ) {

        String cookieHeader =
                exchange.getRequestHeaders()
                        .getFirst("Cookie");

        if (cookieHeader == null) {
            return null;
        }

        String[] cookies =
                cookieHeader.split(";");

        for (String cookie : cookies) {

            String[] pair =
                    cookie.trim().split("=");

            if (
                    pair.length == 2 &&
                            pair[0].equals(cookieName)
            ) {
                return pair[1];
            }
        }

        return null;
    }
}