package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dto.ForgetPasswordRequest;
import model.User;
import repository.UserRepository;
import utils.JsonUtil;
import utils.PasswordResetUtil;

import java.io.IOException;

public class ForgotPasswordHandler implements HttpHandler {

    private final UserRepository userRepository = new UserRepository();

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            exchange.sendResponseHeaders(405, -1);
            return;
        }

        ForgetPasswordRequest req =
                JsonUtil.fromJson(
                        exchange,
                        ForgetPasswordRequest.class
                );
        User user = userRepository.findByEmailOrUsername(req.email);

        // IMPORTANT: always return success (security best practice)
        if (user == null) {
            exchange.sendResponseHeaders(200, -1);
            return;
        }

        String token = PasswordResetUtil.generateToken();

        userRepository.saveResetToken(
                user.getId(),
                token,
                "datetime('now', '+30 minutes')"
        );

        System.out.println("RESET TOKEN: " + token);

        String json = "{\"message\":\"Reset email sent\"}";

        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, json.length());
        exchange.getResponseBody().write(json.getBytes());
        exchange.close();
    }
}