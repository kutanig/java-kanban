package http.handler;

import com.sun.net.httpserver.HttpExchange;
import exceptions.TimeIntersectionException;
import manager.TaskManager;
import task.Subtask;

import java.io.IOException;

public class SubtaskHttpHandler extends BaseHttpHandler {

    public SubtaskHttpHandler(TaskManager taskManager) {
        super(taskManager);
    }

    enum Endpoint { GET_SUBTASK, GET_SUBTASKS, CREATE_SUBTASK, UPDATE_SUBTASK, DELETE_SUBTASK, UNKNOWN }

    private Endpoint getEndpoint(String requestPath, String requestMethod) {
        String[] pathParts = requestPath.split("/");
        if (pathParts.length == 2 && pathParts[1].equals("subtasks")) {
            if (requestMethod.equals("GET")) {
                return Endpoint.GET_SUBTASKS;
            }
            if (requestMethod.equals("POST")) {
                return Endpoint.CREATE_SUBTASK;
            }
        }
        if (pathParts.length == 3 && pathParts[1].equals("subtasks")) {
            if (requestMethod.equals("GET")) {
                return Endpoint.GET_SUBTASK;
            }
            if (requestMethod.equals("DELETE")) {
                return Endpoint.DELETE_SUBTASK;
            }
            if (requestMethod.equals("POST")) {
                return Endpoint.UPDATE_SUBTASK;
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
            case CREATE_SUBTASK -> createSubtask(httpExchange);
            case UPDATE_SUBTASK -> updateSubtask(httpExchange);
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

    private void createSubtask(HttpExchange httpExchange) throws IOException {
        try {
            Subtask subtask = gson.fromJson(getBody(httpExchange), Subtask.class);
            taskManager.add(subtask);
            sendText(httpExchange, "The subtask is create", 201);
        } catch (TimeIntersectionException e) {
            sendHasInteractions(httpExchange);
        } catch (NumberFormatException | StringIndexOutOfBoundsException | NullPointerException e) {
            sendNotFound(httpExchange);
        }
    }

    private void updateSubtask(HttpExchange httpExchange) throws IOException {
        try {
            Subtask subtask = gson.fromJson(getBody(httpExchange), Subtask.class);
            taskManager.update(subtask);
            sendText(httpExchange, "The subtask is update", 201);
        } catch (TimeIntersectionException e) {
            sendHasInteractions(httpExchange);
        } catch (NumberFormatException | StringIndexOutOfBoundsException | NullPointerException e) {
            sendNotFound(httpExchange);
        }
    }
}