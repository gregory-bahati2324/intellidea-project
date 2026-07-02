package middleware;

import com.sun.net.httpserver.HttpExchange;
import utils.JwtUtil;

public class AuthMiddleware {

    public static Integer authenticate(
            HttpExchange exchange
    ) {


        String authHeader =
                exchange.getRequestHeaders()
                        .getFirst("Authorization");

        System.out.println(
                "AUTH HEADER: "
                        + authHeader
        );

        if (
                authHeader == null
                        ||
                        !authHeader.startsWith("Bearer ")
        ) {
            return null;
        }

        String token =
                authHeader.substring(7);

        if (!JwtUtil.isValid(token)) {
            return null;
        }

        return JwtUtil.extractUserId(token);
    }
}