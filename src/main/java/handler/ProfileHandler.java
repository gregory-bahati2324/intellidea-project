package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dto.UpdateProfileRequest;
import model.User;
import repository.UserRepository;
import utils.CorsUtil;
import utils.JsonUtil;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class ProfileHandler implements HttpHandler {

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

        // TEMPORARY USER ID
        int userId = Integer.parseInt(
                exchange.getRequestHeaders()
                        .getFirst("X-User-Id")
        );

        if (exchange.getRequestMethod().equalsIgnoreCase("GET")) {

            User user = userRepository.findById(userId);

            String json = JsonUtil.toJson(user);

            sendJson(exchange, 200, json);

            return;
        }

        if (exchange.getRequestMethod().equalsIgnoreCase("PUT")) {

            System.out.println("PROFILE UPDATE HIT");

            String userIdHeader =
                    exchange.getRequestHeaders()
                            .getFirst("X-User-Id");

            System.out.println("USER ID HEADER: " + userIdHeader);

            String body = new String(
                    exchange.getRequestBody().readAllBytes(),
                    StandardCharsets.UTF_8
            );

            System.out.println("BODY: " + body);

            UpdateProfileRequest request =
                    JsonUtil.fromJson(body, UpdateProfileRequest.class);

            System.out.println("FULL NAME: " + request.getFullName());

            User updatedUser = new User();

            updatedUser.setFullName(request.getFullName());
            updatedUser.setUsername(request.getUsername());
            updatedUser.setEmail(request.getEmail());
            updatedUser.setPhone(request.getPhone());
            updatedUser.setAvatarUrl(request.getAvatarUrl());

            int userId2 = Integer.parseInt(userIdHeader);

            if (
                    userRepository.emailExistsForAnotherUser(
                            request.getEmail(),
                            userId
                    )
            ) {

                sendJson(
                        exchange,
                        400,
                        "{\"message\":\"Email already exists\"}"
                );

                return;
            }

            if (
                    userRepository.usernameExistsForAnotherUser(
                            request.getUsername(),
                            userId
                    )
            ) {

                sendJson(
                        exchange,
                        400,
                        "{\"message\":\"Username already exists\"}"
                );

                return;
            }

            userRepository.updateUser(userId2, updatedUser);

            User savedUser = userRepository.findById(userId2);

            String json = JsonUtil.toJson(savedUser);

            sendJson(exchange, 200, json);

            return;
        }

        sendJson(exchange, 405, "Method Not Allowed");
    }

    private void sendJson(
            HttpExchange exchange,
            int status,
            String response
    ) throws IOException {

        exchange.getResponseHeaders()
                .set("Content-Type", "application/json");

        byte[] bytes = response.getBytes();

        exchange.sendResponseHeaders(status, bytes.length);

        OutputStream os = exchange.getResponseBody();

        os.write(bytes);

        os.close();
    }
}