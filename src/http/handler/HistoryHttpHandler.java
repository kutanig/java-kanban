package http.handler;

import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;

import java.io.IOException;

public class HistoryHttpHandler extends BaseHttpHandler {
    public HistoryHttpHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        String[] path = httpExchange.getRequestURI().getPath().split("/");
        if (method.equals("GET") && path[1].equals("history") && path.length == 2) {
            getHistory(httpExchange);
        } else {
            sendText(httpExchange, "invalid url", 404);
        }
    }

    private void getHistory(HttpExchange httpExchange) throws IOException {
        response = gson.toJson(taskManager.getHistory());
        sendText(httpExchange, response, 200);
    }
}
