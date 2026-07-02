import com.sun.net.httpserver.HttpServer;
import handler.*;
import service.AuthService;

import java.net.InetSocketAddress;

public class Main {

    public static void main(String[] args) {

        try {

            AuthService authService =
                    new AuthService();

            authService.createTables();

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
                    "/api/logout",
                    new LogoutHandler()
            );

            server.createContext(
                    "/api/refresh",
                    new RefreshHandler()
            );

            server.createContext(
                    "/api/profile",
                    new ProfileHandler()
            );

            server.createContext(
                    "/api/change-password",
                    new ChangePasswordHandler()
            );

            server.createContext(
                    "/api/dashboard",
                    new DashboardHandler()
            );

            server.createContext(
                    "/api/forgot-password",
                    new ForgotPasswordHandler()
            );

            server.createContext(
                    "/api/reset-password",
                    new ResetPasswordHandler()
            );
            server.createContext(
                    "/api/verify-reset-user",
                    new VerifyResetUserHandler()
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