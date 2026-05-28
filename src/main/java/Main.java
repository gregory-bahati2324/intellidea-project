import com.sun.net.httpserver.HttpServer;
import handler.RegisterHandler;
import service.AuthService;

import java.net.InetSocketAddress;

import handler.LoginHandler;
import handler.ProfileHandler;
import handler.ChangePasswordHandler;

public class Main {

    public static void main(String[] args) {

        try {

            AuthService authService =
                    new AuthService();

            authService.createTable();

            HttpServer server =
                    HttpServer.create(
                            new InetSocketAddress(8080),
                            0
                    );

            server.createContext(
                    "/api/register",
                    new RegisterHandler()
            );

            server.createContext(
                    "/api/login",
                    new LoginHandler()
            );

            server.createContext(
                    "/api/profile",
                    new ProfileHandler()
            );

            server.createContext(
                    "/api/change-password",
                    new ChangePasswordHandler()
            );


            server.setExecutor(null);

            server.start();

            System.out.println(
                    "Server running on port 8080"
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}