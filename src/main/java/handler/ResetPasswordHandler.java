package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dto.ResetPasswordRequest;
import repository.UserRepository;
import utils.JsonUtil;
import utils.PasswordUtil;
import utils.CorsUtil;

import java.io.IOException;

public class ResetPasswordHandler implements HttpHandler {

    private final UserRepository userRepository = new UserRepository();

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        // ✅ CORS (VERY IMPORTANT)
        CorsUtil.apply(exchange);

        // ✅ Handle preflight request
        if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
            try {
                CorsUtil.handleOptions(exchange);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return;
        }

        if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            exchange.sendResponseHeaders(405, -1);
            return;
        }

        ResetPasswordRequest req =
                JsonUtil.fromJson(exchange, ResetPasswordRequest.class);

        Integer userId =
                userRepository.findUserIdByResetToken(req.token);

        if (userId == null) {
            exchange.sendResponseHeaders(400, -1);
            return;
        }

        String hashed =
                PasswordUtil.hash(req.newPassword);

        userRepository.updatePassword(userId, hashed);

        userRepository.deleteResetToken(req.token);

        String json = "{\"message\":\"Password reset successful\"}";

        exchange.getResponseHeaders().set("Content-Type", "application/json");

        exchange.sendResponseHeaders(200, json.getBytes().length);
        exchange.getResponseBody().write(json.getBytes());

        exchange.close();
    }
}