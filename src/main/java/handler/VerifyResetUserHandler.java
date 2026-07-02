package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dto.VerifyResetRequest;
import model.User;
import repository.UserRepository;
import utils.CorsUtil;
import utils.JsonUtil;
import utils.PasswordResetUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class VerifyResetUserHandler implements HttpHandler {

    private final UserRepository userRepository = new UserRepository();

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        // ✅ 1. APPLY CORS ALWAYS
        CorsUtil.apply(exchange);

        // ✅ 2. HANDLE PREFLIGHT REQUEST (IMPORTANT for frontend)
        if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
            try {
                CorsUtil.handleOptions(exchange);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return;
        }

        // ✅ 3. ONLY ALLOW POST
        if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            exchange.sendResponseHeaders(405, -1);
            return;
        }

        try {
            // ✅ 4. PARSE REQUEST BODY
            VerifyResetRequest req =
                    JsonUtil.fromJson(
                            exchange,
                            VerifyResetRequest.class
                    );

            if (req.email == null || req.phone == null) {
                String bad = "{\"verified\":false,\"message\":\"Missing fields\"}";
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.sendResponseHeaders(400, bad.getBytes().length);
                exchange.getResponseBody().write(bad.getBytes());
                return;
            }

            // ✅ 5. CHECK USER EXISTS
            User user = userRepository.findByEmailAndPhone(
                    req.email,
                    req.phone
            );

            boolean verified = (user != null);

            String response;

            if (verified) {

                String token = PasswordResetUtil.generateToken();

                // OPTIONAL BUT IMPORTANT: store token for reset
                userRepository.saveResetToken(
                        user.getId(),
                        token,
                        "datetime('now', '+30 minutes')"
                );

                response =
                        "{\"verified\":true,\"token\":\"" + token + "\"}";

            } else {

                response = "{\"verified\":false}";
            }

            exchange.getResponseHeaders().set(
                    "Content-Type",
                    "application/json"
            );

            byte[] bytes = response.getBytes(StandardCharsets.UTF_8);

            exchange.sendResponseHeaders(200, bytes.length);
            exchange.getResponseBody().write(bytes);

        } catch (Exception e) {

            e.printStackTrace();

            String error = "{\"verified\":false,\"message\":\"Server error\"}";

            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(500, error.getBytes().length);
            exchange.getResponseBody().write(error.getBytes());
        } finally {
            exchange.close();
        }
    }
}