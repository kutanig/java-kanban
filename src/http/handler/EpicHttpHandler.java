package http.handler;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;
import task.Epic;

import java.io.IOException;

public class EpicHttpHandler extends BaseHttpHandler {
    public EpicHttpHandler(TaskManager taskManager) {
        super(taskManager);
    }

    enum Endpoint { GET_EPIC, GET_EPICS, GET_EPIC_SUBTASKS, POST_EPIC, DELETE_EPIC, UNKNOWN }

    private Endpoint getEndpoint(String requestPath, String requestMethod) {
        String[] pathParts = requestPath.split("/");
        if (pathParts.length == 2 && pathParts[1].equals("epics")) {
            if (requestMethod.equals("GET")) {
                return Endpoint.GET_EPICS;
            }
            if (requestMethod.equals("POST")) {
                return Endpoint.POST_EPIC;
            }
        }
        if (pathParts.length == 3 && pathParts[1].equals("epics")) {
            if (requestMethod.equals("GET")) {
                return Endpoint.GET_EPIC;
            }
            if (requestMethod.equals("DELETE")) {
                return Endpoint.DELETE_EPIC;
            }
        }
        if (pathParts.length == 4 && pathParts[1].equals("epics") && pathParts[3].equals("subtasks")) {
            if (requestMethod.equals("GET")) {
                return Endpoint.GET_EPIC_SUBTASKS;
            }
        }
        return Endpoint.UNKNOWN;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        Endpoint endpoint = getEndpoint(httpExchange.getRequestURI().getPath(), httpExchange.getRequestMethod());
        switch (endpoint) {
            case GET_EPIC -> getEpic(httpExchange);
            case GET_EPICS -> getEpics(httpExchange);
            case GET_EPIC_SUBTASKS -> getEpicSubtasks(httpExchange);
            case POST_EPIC -> postEpic(httpExchange);
            case DELETE_EPIC -> removeEpic(httpExchange);
            default -> sendText(httpExchange, "invalid url", 404);
        }
    }

    private void getEpics(HttpExchange httpExchange) throws IOException {
        response = gson.toJson(taskManager.getEpics());
        sendText(httpExchange, response, 200);
    }

    private void getEpic(HttpExchange httpExchange) throws IOException {
        String[] url = httpExchange.getRequestURI().getPath().split("/");
        try {
            int id = Integer.parseInt(url[2]);
            Epic epic = taskManager.getEpic(id);
            if (epic == null) {
                sendNotFound(httpExchange);
            } else {
                response = gson.toJson(epic);
                sendText(httpExchange, response, 200);
            }
        } catch (NumberFormatException | StringIndexOutOfBoundsException | NullPointerException | IOException e) {
            sendNotFound(httpExchange);
        }
    }

    private void removeEpic(HttpExchange httpExchange) throws IOException {
        String[] url = httpExchange.getRequestURI().getPath().split("/");
        try {
            int id = Integer.parseInt(url[2]);
            Epic epic = taskManager.getEpic(id);
            if (epic == null) {
                sendNotFound(httpExchange);
            } else {
                taskManager.removeTask(id);
                sendText(httpExchange, "epic deleted", 200);
            }
        } catch (NumberFormatException | StringIndexOutOfBoundsException | NullPointerException e) {
            sendNotFound(httpExchange);
        }
    }

    private void postEpic(HttpExchange httpExchange) throws IOException {
        String body = getBody(httpExchange);
        JsonElement element = JsonParser.parseString(body);
        if (!element.isJsonObject()) {
            sendText(httpExchange, "Internal Server Error", 500);
            return;
        }
        JsonObject jsonObject = element.getAsJsonObject();
        if (jsonObject.has("id")) {
            updateEpic(httpExchange);
        }
        createEpic(httpExchange);
    }

    private void updateEpic(HttpExchange httpExchange) throws IOException {
        try {
            String body = getBody(httpExchange);
            Epic newTask = gson.fromJson(body, Epic.class);
            taskManager.update(newTask);
            sendText(httpExchange, "The epic is update", 201);
        } catch (NumberFormatException | StringIndexOutOfBoundsException | NullPointerException e) {
            sendNotFound(httpExchange);
        }
    }

    private void createEpic(HttpExchange httpExchange) throws IOException {
        try {
            String body = getBody(httpExchange);
            Epic newTask = gson.fromJson(body, Epic.class);
            taskManager.add(newTask);
            sendText(httpExchange, "The epic is created", 201);
        } catch (NumberFormatException | StringIndexOutOfBoundsException | NullPointerException e) {
            sendNotFound(httpExchange);
        }
    }

    private void getEpicSubtasks(HttpExchange httpExchange) throws IOException {
        String[] url = httpExchange.getRequestURI().getPath().split("/");
        try {
            int id = Integer.parseInt(url[2]);
            Epic epic = taskManager.getEpic(id);
            if (epic == null) {
                sendNotFound(httpExchange);
            } else {
                response = gson.toJson(taskManager.getSubsOfEpic(id));
                sendText(httpExchange, response, 200);
            }
        } catch (NumberFormatException | StringIndexOutOfBoundsException | NullPointerException e) {
            sendNotFound(httpExchange);
        }
    }
}
