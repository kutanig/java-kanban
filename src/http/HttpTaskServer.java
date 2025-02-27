package http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import http.handler.*;
import manager.Managers;
import manager.TaskManager;
import task.Epic;
import task.Subtask;
import task.Task;
import task.Taskstatus;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {
    private static final int PORT = 8080;
    protected static TaskManager taskManager;
    private final HttpServer httpServer;

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        HttpTaskServer.taskManager = taskManager;
        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new TaskHttpHandler(taskManager));
        httpServer.createContext("/subtasks", new SubtaskHttpHandler(taskManager));
        httpServer.createContext("/epics", new EpicHttpHandler(taskManager));
        httpServer.createContext("/history", new HistoryHttpHandler(taskManager));
        httpServer.createContext("/prioritized", new PrioritizedHttpHandler(taskManager));
    }

    public static void main(String[] args) throws IOException {
        HttpTaskServer server = new HttpTaskServer(Managers.getBackedTMDefault());
        taskManager.add(new Task("task", "des", Taskstatus.NEW));
        taskManager.add(new Epic("epic", "des", Taskstatus.DONE));
        taskManager.add(new Subtask("sub", "dis", Taskstatus.NEW, LocalDateTime.now(), Duration.ofMinutes(5), 2));
        taskManager.add(new Task("task2", "dasd", Taskstatus.IN_PROGRESS,LocalDateTime.of(2025,5,5,5,5
        ),Duration.ofDays(1)));

        try {
            server.start();
        } catch (IOException e) {
            server.stop();
            System.out.println("Network error: " + e.getMessage());
        }
    }

    public static Gson getGson() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new BaseHttpHandler.LocalDateTimeAdapters())
                .registerTypeAdapter(Duration.class, new BaseHttpHandler.DurationAdapters())
                .create();
    }

    public void start() throws IOException {
        httpServer.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    public void stop() {
        httpServer.stop(0);
    }
}
