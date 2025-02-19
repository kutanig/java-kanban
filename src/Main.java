import manager.FileBackedTaskManager;
import manager.TaskManager;
import task.Epic;
import task.Subtask;
import task.Task;
import task.Taskstatus;
import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {
        FileBackedTaskManager fileBackedTaskManager = FileBackedTaskManager.loadFromFile(new File("src/savedTM.txt"));
        System.out.printf("%-25s", "");
        System.out.println("***Загружено***");
        printAllTasks(fileBackedTaskManager);

        Task task1 = new Task("задача1", "описание", Taskstatus.NEW, LocalDateTime.now(), Duration.ofHours(1));
        fileBackedTaskManager.add(task1);
        Task task2 = new Task("задача2", "описание2", Taskstatus.DONE, LocalDateTime.of(2025, 5, 1, 1, 1), Duration.ofMinutes(30));
        fileBackedTaskManager.add(task2);
        Task task3 = new Task("задача3", "описание4", Taskstatus.NEW, LocalDateTime.now(), Duration.ofMinutes(30));
        fileBackedTaskManager.add(task3);

        Epic epic = new Epic("эпик первый", "утро", Taskstatus.DONE);
        fileBackedTaskManager.add(epic);
        Subtask subtask1 = new Subtask("подзадача1", "умыться", Taskstatus.IN_PROGRESS, LocalDateTime.of(2025, 2, 1, 1, 2), Duration.ofMinutes(10), epic.getId());
        fileBackedTaskManager.add(subtask1);
        Subtask subtask2 = new Subtask("подзадача2", "побриться", Taskstatus.NEW, LocalDateTime.of(2025, 1, 1, 1, 1), Duration.ofHours(1), epic.getId());
        fileBackedTaskManager.add(subtask2);

        Epic epic1 = new Epic("эпик второй", "работа", Taskstatus.NEW);
        fileBackedTaskManager.add(epic1);
        Subtask subtask = new Subtask("подзадача", "встать с дивана", Taskstatus.DONE, LocalDateTime.of(2025, 4, 4, 4, 4), Duration.ofMinutes(22), epic1.getId());
        fileBackedTaskManager.add(subtask);

        System.out.printf("%-25s", "");
        System.out.println("***Добавлено***");

        printAllTasks(fileBackedTaskManager);

    }

    private static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : manager.getTasks()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Task epic : manager.getEpics()) {
            System.out.println(epic);

            for (Task task : manager.getSubsOfEpic(epic.getId())) {
                System.out.println("--> " + task);
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask : manager.getSubtasks()) {
            System.out.println(subtask);
        }

        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }

        System.out.println("сортиртаскс:");
        manager.getPrioritizedTasks().forEach(System.out::println);
    }
}