package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import repository.RefreshTokenRepository;
import utils.CookieUtil;
import utils.CorsUtil;

import java.io.IOException;
import java.io.OutputStream;

public class LogoutHandler implements HttpHandler {

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

        String refreshToken =
                CookieUtil.getCookie(
                        exchange,
                        "refreshToken"
                );

        if (refreshToken != null) {

            refreshTokenRepository
                    .deleteByToken(
                            refreshToken
                    );
        }

        CookieUtil.clearRefreshCookie(
                exchange
        );

        send(
                exchange,
                200,
                "{\"message\":\"Logged out\"}"
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