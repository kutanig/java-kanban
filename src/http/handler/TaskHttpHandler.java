package http.handler;

import com.sun.net.httpserver.HttpExchange;
import exceptions.TimeIntersectionException;
import manager.TaskManager;
import task.Task;

import java.io.IOException;

public class TaskHttpHandler extends BaseHttpHandler {

    public TaskHttpHandler(TaskManager taskManager) {
        super(taskManager);
    }

    enum Endpoint { GET_TASK, GET_TASKS, CREATE_TASK, UPDATE_TASK, DELETE_TASK, UNKNOWN }

    private Endpoint getEndpoint(String requestPath, String requestMethod) {
        String[] pathParts = requestPath.split("/");
        if (pathParts.length == 2 && pathParts[1].equals("tasks")) {
            if (requestMethod.equals("GET")) {
                return Endpoint.GET_TASKS;
            }
            if (requestMethod.equals("POST")) {
                return Endpoint.CREATE_TASK;
            }
        }
        if (pathParts.length == 3 && pathParts[1].equals("tasks")) {
            switch (requestMethod) {
                case "GET" -> {
                    return Endpoint.GET_TASK;
                }
                case "DELETE" -> {
                    return Endpoint.DELETE_TASK;
                }
                case "POST" -> {
                    return Endpoint.UPDATE_TASK;
                }
            }
        }
        return Endpoint.UNKNOWN;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        Endpoint endpoint = getEndpoint(httpExchange.getRequestURI().getPath(), httpExchange.getRequestMethod());
        switch (endpoint) {
            case GET_TASK -> getTask(httpExchange);
            case GET_TASKS -> getTasks(httpExchange);
            case CREATE_TASK -> createTask(httpExchange);
            case UPDATE_TASK -> updateTask(httpExchange);
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

    private void createTask(HttpExchange httpExchange) throws IOException {
        try {
            Task task = gson.fromJson(getBody(httpExchange), Task.class);
            taskManager.add(task);
            sendText(httpExchange, "The task is create", 201);
        } catch (TimeIntersectionException e) {
            sendHasInteractions(httpExchange);
        } catch (NumberFormatException | StringIndexOutOfBoundsException | NullPointerException e) {
            sendNotFound(httpExchange);
        }
    }

    private void updateTask(HttpExchange httpExchange) throws IOException {
        try {
            Task task = gson.fromJson(getBody(httpExchange), Task.class);
            taskManager.update(task);
            sendText(httpExchange, "The task is update", 201);
        } catch (TimeIntersectionException e) {
            sendHasInteractions(httpExchange);
        } catch (NumberFormatException | StringIndexOutOfBoundsException | NullPointerException e) {
            sendNotFound(httpExchange);
        }
    }
}

