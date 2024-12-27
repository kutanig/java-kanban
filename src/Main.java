import Manager.HistoryManager;
import Manager.Managers;
import Manager.TaskManager;
import Tasc.Epic;
import Tasc.Subtask;
import Tasc.Task;
import Tasc.Taskstatus;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();
        HistoryManager historyManager = Managers.getDefaultHistory();

        Task task1 = new Task("задача1", "описание", Taskstatus.NEW);
        taskManager.add(task1);
        Task task2 = new Task("задача2", "онисание2", Taskstatus.DONE);
        taskManager.add(task2);

        Epic epic = new Epic("эпик первый", "утро", Taskstatus.DONE);
        taskManager.add(epic);
        Subtask subtask1 = new Subtask("подзадача1", "умытся", Taskstatus.IN_PROGRESS, epic.getId());
        taskManager.add(subtask1);
        Subtask subtask2 = new Subtask("подзадача2", "побрится", Taskstatus.NEW, epic.getId());
        taskManager.add(subtask2);

        Epic epic1 = new Epic("эпик второй", "сходить в магазин", Taskstatus.NEW);
        taskManager.add(epic1);
        Subtask subtask = new Subtask("подзадача", "встать с дивана", Taskstatus.DONE, epic1.getId());
        taskManager.add(subtask);

        task1.setStatus(Taskstatus.IN_PROGRESS);
        taskManager.update(task1);
        task2.setStatus(Taskstatus.NEW);
        taskManager.update(task2);
        epic.setStatus(Taskstatus.NEW);
        taskManager.update(epic);
        subtask1.setStatus(Taskstatus.DONE);
        taskManager.update(subtask1);
        subtask2.setStatus(Taskstatus.DONE);
        taskManager.update(subtask2);
        epic1.setStatus(Taskstatus.DONE);
        taskManager.update(epic1);
        subtask.setStatus(Taskstatus.IN_PROGRESS);
        taskManager.update(subtask);

        taskManager.getTask(task1.getId());
        taskManager.getTask(task2.getId());
        taskManager.getTask(task1.getId());
        taskManager.getSubtask(subtask.getId());
        taskManager.getEpic(epic.getId());
        taskManager.getEpic(epic.getId());
        taskManager.getSubtask(subtask.getId());
        taskManager.getEpic(epic1.getId());
        taskManager.getSubtask(subtask2.getId());
        taskManager.getSubtask(subtask1.getId());
        taskManager.getTask(task1.getId());

        printAllTasks(taskManager, historyManager);

    }
    private static void printAllTasks(TaskManager manager, HistoryManager historyManager) {
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
        /*for (Object id : manager.phm1()) {
            System.out.println(id);
        }*/
    }
}