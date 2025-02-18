import manager.FileBackedTaskManager;
import manager.TaskManager;
import task.Epic;
import task.Subtask;
import task.Task;
import task.Taskstatus;
import java.io.File;

public class Main {

    public static void main(String[] args) {
        FileBackedTaskManager fileBackedTaskManager = FileBackedTaskManager.loadFromFile(new File("src/savedTM.txt"));
        System.out.printf("%-25s", "");
        System.out.println("***Загружено***");
        printAllTasks(fileBackedTaskManager);

        Task task1 = new Task("задача1", "описание", Taskstatus.NEW);
        fileBackedTaskManager.add(task1);
        Task task2 = new Task("задача2", "описание2", Taskstatus.DONE);
        fileBackedTaskManager.add(task2);

        Epic epic = new Epic("эпик первый", "утро", Taskstatus.DONE);
        fileBackedTaskManager.add(epic);
        Subtask subtask1 = new Subtask("подзадача1", "умыться", Taskstatus.IN_PROGRESS, epic.getId());
        fileBackedTaskManager.add(subtask1);
        Subtask subtask2 = new Subtask("подзадача2", "побриться", Taskstatus.NEW, epic.getId());
        fileBackedTaskManager.add(subtask2);

        Epic epic1 = new Epic("эпик второй", "работа", Taskstatus.NEW);
        fileBackedTaskManager.add(epic1);
        Subtask subtask = new Subtask("подзадача", "встать с дивана", Taskstatus.DONE, epic1.getId());
        fileBackedTaskManager.add(subtask);

        task1.setStatus(Taskstatus.IN_PROGRESS);
        fileBackedTaskManager.update(task1);
        task2.setStatus(Taskstatus.NEW);
        fileBackedTaskManager.update(task2);
        epic.setStatus(Taskstatus.NEW);
        fileBackedTaskManager.update(epic);
        subtask1.setStatus(Taskstatus.DONE);
        fileBackedTaskManager.update(subtask1);
        subtask2.setStatus(Taskstatus.DONE);
        fileBackedTaskManager.update(subtask2);
        epic1.setStatus(Taskstatus.DONE);
        fileBackedTaskManager.update(epic1);
        subtask.setStatus(Taskstatus.IN_PROGRESS);
        fileBackedTaskManager.update(subtask);

        fileBackedTaskManager.getTask(task1.getId());
        fileBackedTaskManager.getTask(task2.getId());
        fileBackedTaskManager.getTask(task1.getId());
        fileBackedTaskManager.getSubtask(subtask.getId());
        fileBackedTaskManager.getEpic(epic.getId());
        fileBackedTaskManager.getEpic(epic.getId());
        fileBackedTaskManager.getSubtask(subtask.getId());
        fileBackedTaskManager.getEpic(epic1.getId());
        fileBackedTaskManager.getSubtask(subtask2.getId());
        fileBackedTaskManager.getSubtask(subtask1.getId());
        fileBackedTaskManager.getTask(task1.getId());

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
    }
}