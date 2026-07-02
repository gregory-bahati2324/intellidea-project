package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dto.ChangePasswordRequest;
import middleware.AuthMiddleware;
import model.User;
import repository.UserRepository;
import utils.CorsUtil;
import utils.JsonUtil;
import utils.PasswordUtil;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class ChangePasswordHandler implements HttpHandler {

    private final UserRepository userRepository = new UserRepository();

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        CorsUtil.apply(exchange);

        if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        if (!exchange.getRequestMethod().equalsIgnoreCase("PUT")) {
            send(exchange, 405, "{\"message\":\"Method not allowed\"}");
            return;
        }

        Integer userId =
                AuthMiddleware.authenticate(
                        exchange
                );

        if (userId == null) {

            send(
                    exchange,
                    401,
                    "{\"message\":\"Unauthorized\"}"
            );

            return;
        }

        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);

        ChangePasswordRequest req =
                JsonUtil.fromJson(body, ChangePasswordRequest.class);

        User user = userRepository.findById(userId);

        // check current password
        if (PasswordUtil.verify(req.getCurrentPassword(), user.getPassword())) {
            send(exchange, 400, "{\"message\":\"Current password is wrong\"}");
            return;
        }

        // update password
        String hashed = PasswordUtil.hash(req.getNewPassword());
        user.setPassword(hashed);

        userRepository.updatePassword(userId, hashed);

        send(exchange, 200, "{\"message\":\"Password updated successfully\"}");
    }

    private void send(HttpExchange exchange, int status, String response) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        byte[] bytes = response.getBytes();
        exchange.sendResponseHeaders(status, bytes.length);
        OutputStream os = exchange.getResponseBody();
        os.write(bytes);
        os.close();
    }
}