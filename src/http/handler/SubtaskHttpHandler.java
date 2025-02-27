package http.handler;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import exceptions.TimeIntersectionException;
import manager.TaskManager;
import task.Subtask;

import java.io.IOException;

public class SubtaskHttpHandler extends BaseHttpHandler {

    public SubtaskHttpHandler(TaskManager taskManager) {
        super(taskManager);
    }

    enum Endpoint {GET_SUBTASK, GET_SUBTASKS, POST_SUBTASK, DELETE_SUBTASK, UNKNOWN}

    private Endpoint getEndpoint(String requestPath, String requestMethod) {
        String[] pathParts = requestPath.split("/");
        if (pathParts.length == 2 && pathParts[1].equals("subtasks")) {
            if (requestMethod.equals("GET")) {
                return Endpoint.GET_SUBTASKS;
            }
            if (requestMethod.equals("POST")) {
                return Endpoint.POST_SUBTASK;
            }
        }
        if (pathParts.length == 3 && pathParts[1].equals("subtasks")) {
            if (requestMethod.equals("GET")) {
                return Endpoint.GET_SUBTASK;
            }
            if (requestMethod.equals("DELETE")) {
                return Endpoint.DELETE_SUBTASK;
            }
        }
        return Endpoint.UNKNOWN;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        Endpoint endpoint = getEndpoint(httpExchange.getRequestURI().getPath(), httpExchange.getRequestMethod());
        switch (endpoint) {
            case GET_SUBTASK -> getSubtask(httpExchange);
            case GET_SUBTASKS -> getSubtasks(httpExchange);
            case POST_SUBTASK -> postSubtask(httpExchange);
            case DELETE_SUBTASK -> removeSubtask(httpExchange);
            default -> sendText(httpExchange, "invalid url", 404);
        }
    }

    private void getSubtasks(HttpExchange httpExchange) throws IOException {
        try {
            response = gson.toJson(taskManager.getSubtasks());
            sendText(httpExchange, response, 200);
        } catch (NumberFormatException | StringIndexOutOfBoundsException | NullPointerException e) {
            sendNotFound(httpExchange);
        }
    }

    private void getSubtask(HttpExchange httpExchange) throws IOException {
        String[] url = httpExchange.getRequestURI().getPath().split("/");
        try {
            int id = Integer.parseInt(url[2]);
            Subtask subtask = taskManager.getSubtask(id);
            if (subtask == null) {
                sendNotFound(httpExchange);
            } else {
                response = gson.toJson(subtask);
                sendText(httpExchange, response, 200);
            }
        } catch (NumberFormatException | StringIndexOutOfBoundsException | NullPointerException e) {
            sendNotFound(httpExchange);
        }
    }

    private void removeSubtask(HttpExchange httpExchange) throws IOException {
        String[] url = httpExchange.getRequestURI().getPath().split("/");
        try {
            int id = Integer.parseInt(url[2]);
            Subtask subtask = taskManager.getSubtask(id);
            if (subtask == null) {
                sendNotFound(httpExchange);
            } else {
                taskManager.removeSubtask(id);
                sendText(httpExchange, "subtask deleted", 200);
            }
        } catch (NumberFormatException | StringIndexOutOfBoundsException | NullPointerException e) {
            sendNotFound(httpExchange);
        }
    }

    private void postSubtask(HttpExchange httpExchange) throws IOException {
        String body = getBody(httpExchange);
        JsonElement element = JsonParser.parseString(body);
        if (!element.isJsonObject()) {
            sendText(httpExchange, "Internal Server Error", 500);
            return;
        }
        JsonObject jsonObject = element.getAsJsonObject();
        if (jsonObject.has("id")) {
            updateSubtask(httpExchange);
        }
        createSubtask(httpExchange);
    }

    private void createSubtask(HttpExchange httpExchange) throws IOException {
        try {
            String body = getBody(httpExchange);
            Subtask newTask = gson.fromJson(body, Subtask.class);
            taskManager.add(newTask);
            sendText(httpExchange, "The subtask is create", 201);
        } catch (TimeIntersectionException e) {
            sendHasInteractions(httpExchange);
        } catch (NumberFormatException | StringIndexOutOfBoundsException | NullPointerException e) {
            sendNotFound(httpExchange);
        }
    }

    private void updateSubtask(HttpExchange httpExchange) throws IOException {
        try {
            String body = getBody(httpExchange);
            Subtask newTask = gson.fromJson(body, Subtask.class);
            taskManager.update(newTask);
            sendText(httpExchange, "The subtask is update", 201);
        } catch (TimeIntersectionException e) {
            sendHasInteractions(httpExchange);
        } catch (NumberFormatException | StringIndexOutOfBoundsException | NullPointerException e) {
            sendNotFound(httpExchange);
        }
    }
}