package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dto.RegisterRequest;
import model.User;
import repository.UserRepository;
import utils.JsonUtil;
import utils.CorsUtil;
import utils.PasswordUtil;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class RegisterHandler implements HttpHandler {

    private final UserRepository userRepository = new UserRepository();

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        // ALWAYS APPLY CORS FIRST
        CorsUtil.apply(exchange);

        // HANDLE PREFLIGHT
        if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
            exchange.sendResponseHeaders(204, -1);
            exchange.close();
            return;
        }

        if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            sendResponse(exchange, 405, "Method Not Allowed");
            return;
        }

        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);

        System.out.println("RAW BODY: " + body);

        RegisterRequest request = JsonUtil.fromJson(body, RegisterRequest.class);
        System.out.println("EMAIL: " + request.getEmail());
        System.out.println("USERNAME: " + request.getUsername());
        System.out.println("FULLNAME: " + request.getFullName());

        if (userRepository.emailExists(request.getEmail())) {
            sendResponse(exchange, 400, "Email already exists");
            return;
        }

        if (userRepository.usernameExists(request.getUsername())) {
            sendResponse(exchange, 400, "Username already exists");
            return;
        }

        User user = new User(
                request.getFullName(),
                request.getUsername(),
                request.getEmail(),
                request.getPhone(),
                PasswordUtil.hash(request.getPassword()),
                request.getAvatarUrl()
        );

        userRepository.save(user);

        sendResponse(exchange, 200, "User registered successfully");
    }

    private void sendResponse(HttpExchange exchange, int status, String response)
            throws IOException {

        CorsUtil.apply(exchange);

        byte[] bytes = response.getBytes();

        exchange.sendResponseHeaders(status, bytes.length);

        OutputStream os = exchange.getResponseBody();
        os.write(bytes);
        os.close();
    }
}