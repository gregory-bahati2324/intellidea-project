package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dto.RefreshRequest;
import model.RefreshToken;
import repository.RefreshTokenRepository;
import utils.CookieUtil;
import utils.CorsUtil;
import utils.JsonUtil;
import utils.JwtUtil;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class RefreshHandler implements HttpHandler {

    private final RefreshTokenRepository
            refreshTokenRepository =
            new RefreshTokenRepository();

    @Override
    public void handle(
            HttpExchange exchange
    ) throws IOException {

        CorsUtil.apply(exchange);

        if (
                exchange.getRequestMethod()
                        .equalsIgnoreCase("OPTIONS")
        ) {

            exchange.sendResponseHeaders(
                    204,
                    -1
            );

            return;
        }

        if (
                !exchange.getRequestMethod()
                        .equalsIgnoreCase("POST")
        ) {

            send(
                    exchange,
                    405,
                    "Method Not Allowed"
            );

            return;
        }

        String body =
                new String(
                        exchange.getRequestBody()
                                .readAllBytes(),
                        StandardCharsets.UTF_8
                );

        String refreshTokenValue =
                CookieUtil.getCookie(
                        exchange,
                        "refreshToken"
                );

        RefreshToken refreshToken =
                refreshTokenRepository.findByToken(
                        refreshTokenValue
                );

        if (refreshToken == null) {

            send(
                    exchange,
                    401,
                    "Invalid refresh token"
            );

            return;
        }

        if (
                !JwtUtil.isValid(
                        refreshToken.getToken()
                )
        ) {

            send(
                    exchange,
                    401,
                    "Expired refresh token"
            );

            return;
        }

        String newAccessToken =
                JwtUtil.generateAccessToken(
                        refreshToken.getUserId()
                );

        send(
                exchange,
                200,
                "{\"accessToken\":\""
                        + newAccessToken
                        + "\"}"
        );
    }

    private void send(
            HttpExchange exchange,
            int status,
            String response
    ) throws IOException {

        byte[] bytes =
                response.getBytes();

        exchange.sendResponseHeaders(
                status,
                bytes.length
        );

        OutputStream os =
                exchange.getResponseBody();

        os.write(bytes);

        os.close();
    }
}