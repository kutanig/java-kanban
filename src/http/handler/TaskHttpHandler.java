package http.handler;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import exceptions.TimeIntersectionException;
import manager.TaskManager;
import task.Task;

import java.io.IOException;

public class TaskHttpHandler extends BaseHttpHandler {

    public TaskHttpHandler(TaskManager taskManager) {
        super(taskManager);
    }

    enum Endpoint {GET_EPIC, GET_TASKS, POST_TASK, DELETE_TASK, UNKNOWN}

    private Endpoint getEndpoint(String requestPath, String requestMethod) {
        String[] pathParts = requestPath.split("/");
        if (pathParts.length == 2 && pathParts[1].equals("tasks")) {
            if (requestMethod.equals("GET")) {
                return Endpoint.GET_TASKS;
            }
            if (requestMethod.equals("POST")) {
                return Endpoint.POST_TASK;
            }
        }
        if (pathParts.length == 3 && pathParts[1].equals("tasks")) {
            if (requestMethod.equals("GET")) {
                return Endpoint.GET_EPIC;
            }
            if (requestMethod.equals("DELETE")) {
                return Endpoint.DELETE_TASK;
            }
        }
        return Endpoint.UNKNOWN;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        Endpoint endpoint = getEndpoint(httpExchange.getRequestURI().getPath(), httpExchange.getRequestMethod());
        switch (endpoint) {
            case GET_EPIC -> getTask(httpExchange);
            case GET_TASKS -> getTasks(httpExchange);
            case POST_TASK -> postTask(httpExchange);
            case DELETE_TASK -> removeTask(httpExchange);
            default -> sendText(httpExchange, "invalid url", 404);
        }
    }

    private void getTasks(HttpExchange httpExchange) throws IOException {
        response = gson.toJson(taskManager.getTasks());
        sendText(httpExchange, response, 200);
    }

    private void getTask(HttpExchange httpExchange) throws IOException {
        String[] url = httpExchange.getRequestURI().getPath().split("/");
        try {
            int id = Integer.parseInt(url[2]);
            Task task = taskManager.getTask(id);
            if (task == null) {
                sendNotFound(httpExchange);
            } else {
                response = gson.toJson(task);
                sendText(httpExchange, response, 200);
            }
        } catch (NumberFormatException | StringIndexOutOfBoundsException | NullPointerException e) {
            sendNotFound(httpExchange);
        }
    }

    private void removeTask(HttpExchange httpExchange) throws IOException {
        String[] url = httpExchange.getRequestURI().getPath().split("/");
        try {
            int id = Integer.parseInt(url[2]);
            Task task = taskManager.getTask(id);
            if (task == null) {
                sendNotFound(httpExchange);
            } else {
                taskManager.removeTask(id);
                sendText(httpExchange, "task deleted", 200);
            }
        } catch (NumberFormatException | StringIndexOutOfBoundsException | NullPointerException e) {
            sendNotFound(httpExchange);
        }
    }

    private void postTask(HttpExchange httpExchange) throws IOException {
        String body = getBody(httpExchange);
        JsonElement element = JsonParser.parseString(body);
        if (!element.isJsonObject()) {
            sendText(httpExchange, "Internal Server Error", 500);
            return;
        }
        JsonObject jsonObject = element.getAsJsonObject();
        if (jsonObject.has("id")) {
            updateTask(httpExchange);
        }
        createTask(httpExchange);
    }

    private void createTask(HttpExchange httpExchange) throws IOException {
        try {
            String body = getBody(httpExchange);
            Task newTask = gson.fromJson(body, Task.class);
            taskManager.add(newTask);
            sendText(httpExchange, "The task is create", 201);
        } catch (TimeIntersectionException e) {
            sendHasInteractions(httpExchange);
        } catch (NumberFormatException | StringIndexOutOfBoundsException | NullPointerException e) {
            sendNotFound(httpExchange);
        }
    }

    private void updateTask(HttpExchange httpExchange) throws IOException {
        try {
            String body = getBody(httpExchange);
            Task newTask = gson.fromJson(body, Task.class);
            taskManager.update(newTask);
            sendText(httpExchange, "The task is update", 201);
        } catch (TimeIntersectionException e) {
            sendHasInteractions(httpExchange);
        } catch (NumberFormatException | StringIndexOutOfBoundsException | NullPointerException e) {
            sendNotFound(httpExchange);
        }
    }
}

