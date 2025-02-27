package http.handler;

import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;
import java.io.IOException;

public class PrioritizedHttpHandler extends BaseHttpHandler {
    public PrioritizedHttpHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        String[] path = httpExchange.getRequestURI().getPath().split("/");
        if (method.equals("GET") && path.length == 2 && path[1].equals("prioritized")) {
            getPrioritized(httpExchange);
        } else {
            sendText(httpExchange, "invalid url", 404);
        }
    }

    private void getPrioritized(HttpExchange httpExchange) throws IOException {
        response = gson.toJson(taskManager.getPrioritizedTasks());
        sendText(httpExchange, response, 200);
    }
}
