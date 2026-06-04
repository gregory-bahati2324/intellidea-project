package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dto.DashboardResponse;
import repository.UserRepository;
import utils.CorsUtil;
import utils.JsonUtil;

import java.io.IOException;
import java.util.List;

public class DashboardHandler implements HttpHandler {

    private final UserRepository userRepository = new UserRepository();

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        CorsUtil.apply(exchange);

        if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
            exchange.sendResponseHeaders(204, -1);
            exchange.close();
            return;
        }

        if (!exchange.getRequestMethod().equalsIgnoreCase("GET")) {
            exchange.sendResponseHeaders(405, -1);
            return;
        }

        DashboardResponse res = new DashboardResponse();

        // ===== MOCK LOGIC FOR NOW (we will replace later with DB) =====

        DashboardResponse.Stat s1 = new DashboardResponse.Stat();
        s1.label = "Total users";
        s1.value = String.valueOf(userRepository.countUsers());
        s1.change = "+4.2%";

        DashboardResponse.Stat s2 = new DashboardResponse.Stat();
        s2.label = "Active sessions";
        s2.value = "12";
        s2.change = "+1.8%";

        DashboardResponse.Stat s3 = new DashboardResponse.Stat();
        s3.label = "Logins today";
        s3.value = "45";
        s3.change = "+12%";

        DashboardResponse.Stat s4 = new DashboardResponse.Stat();
        s4.label = "Security score";
        s4.value = "9/100";
        s4.change = "+2";

        res.stats = List.of(s1, s2, s3, s4);

        DashboardResponse.Activity a1 = new DashboardResponse.Activity();
        a1.user = "System";
        a1.action = "Dashboard loaded";
        a1.time = "now";

        res.activity = List.of(a1);

        res.profileCompletion = 60;

        String json = JsonUtil.toJson(res);

        exchange.getResponseHeaders().set("Content-Type", "application/json");
        byte[] bytes = json.getBytes();

        exchange.sendResponseHeaders(200, bytes.length);
        exchange.getResponseBody().write(bytes);
        exchange.getResponseBody().close();
    }
}