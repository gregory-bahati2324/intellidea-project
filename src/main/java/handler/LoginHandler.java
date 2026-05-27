package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dto.AuthResponse;
import dto.LoginRequest;
import model.User;
import repository.UserRepository;
import utils.CorsUtil;
import utils.JsonUtil;
import utils.PasswordUtil;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class LoginHandler implements HttpHandler {

    private final UserRepository userRepository =
            new UserRepository();

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        CorsUtil.apply(exchange);

        if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
            exchange.sendResponseHeaders(204, -1);
            exchange.close();
            return;
        }

        if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            sendResponse(exchange, 405, "Method Not Allowed");
            return;
        }

        String body = new String(
                exchange.getRequestBody().readAllBytes(),
                StandardCharsets.UTF_8
        );

        System.out.println("LOGIN BODY: " + body);

        LoginRequest request =
                JsonUtil.fromJson(body, LoginRequest.class);

        User user =
                userRepository.findByEmailOrUsername(
                        request.getIdentifier()
                );

        if (user == null) {
            sendResponse(exchange, 401, "Invalid credentials");
            return;
        }

        boolean validPassword =
                PasswordUtil.verify(
                        request.getPassword(),
                        user.getPassword()
                );

        if (!validPassword) {
            sendResponse(exchange, 401, "Invalid credentials");
            return;
        }

        // fake token for now
        String token = UUID.randomUUID().toString();

        AuthResponse response =
                new AuthResponse(token, user);

        String json =
                JsonUtil.toJson(response);

        exchange.getResponseHeaders().set(
                "Content-Type",
                "application/json"
        );

        sendResponse(exchange, 200, json);
    }

    private void sendResponse(
            HttpExchange exchange,
            int status,
            String response
    ) throws IOException {

        CorsUtil.apply(exchange);

        byte[] bytes =
                response.getBytes(StandardCharsets.UTF_8);

        exchange.sendResponseHeaders(status, bytes.length);

        OutputStream os =
                exchange.getResponseBody();

        os.write(bytes);

        os.close();
    }
}