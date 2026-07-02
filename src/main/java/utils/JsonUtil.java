package utils;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class JsonUtil {

    private static final Gson gson = new Gson();

    public static <T> T fromJson(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }

    public static <T> T fromJson(
            HttpExchange exchange,
            Class<T> clazz
    ) throws IOException {

        String body =
                new String(
                        exchange
                                .getRequestBody()
                                .readAllBytes()
                );

        return gson.fromJson(
                body,
                clazz
        );
    }

    public static String toJson(Object obj) {
        return gson.toJson(obj);
    }
}