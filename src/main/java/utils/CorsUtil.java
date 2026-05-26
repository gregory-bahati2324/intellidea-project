package utils;

import com.sun.net.httpserver.HttpExchange;

public class CorsUtil {

    public static void apply(HttpExchange exchange) {

        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type, Authorization");
    }

    public static void handleOptions(HttpExchange exchange) throws Exception {

        apply(exchange);
        exchange.sendResponseHeaders(204, -1);
        exchange.close();
    }
}