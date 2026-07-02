package utils;

import com.sun.net.httpserver.HttpExchange;

public class CorsUtil {

    public static void apply(HttpExchange exchange) {

        exchange.getResponseHeaders().set(
                "Access-Control-Allow-Origin",
                "http://localhost:8081"
        );
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type, Authorization, X-User-Id");
        exchange.getResponseHeaders().set("Access-Control-Allow-Credentials", "true");
    }

    public static void handleOptions(HttpExchange exchange) throws Exception {

        apply(exchange);
        exchange.sendResponseHeaders(204, -1);
        exchange.close();
    }
}