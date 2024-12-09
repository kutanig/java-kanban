import Manager.HistoryManager;
import Manager.Managers;
import Manager.TaskManager;
import Tasc.Epic;
import Tasc.Subtask;
import Tasc.Task;
import Tasc.Taskstatus;

public class Main {

    public static void main(String[] args) {
        HistoryManager hM = Managers.getDefaultHistory();
        TaskManager tM = Managers.getDefault();

        Task task1 = new Task("задача1", "описание", Taskstatus.NEW);
        tM.add(task1);
        Task task2 = new Task("задача2", "онисание2", Taskstatus.DONE);
        tM.add(task2);

        Epic epic = new Epic("эпик первый", "утро", Taskstatus.DONE);
        tM.add(epic);
        Subtask subtask1 = new Subtask("подзадача", "умытся", Taskstatus.IN_PROGRESS, epic.getId());
        tM.add(subtask1);
        Subtask subtask2 = new Subtask("подзадача2", "побрится", Taskstatus.NEW, epic.getId());
        tM.add(subtask2);

        Epic epic1 = new Epic("эпик второй", "сходить в магазин", Taskstatus.NEW);
        tM.add(epic1);
        Subtask subtask = new Subtask("подзадача", "встать с дивана", Taskstatus.DONE, epic1.getId());
        tM.add(subtask);

        task1.setStatus(Taskstatus.IN_PROGRESS);
        tM.update(task1);
        task2.setStatus(Taskstatus.NEW);
        tM.update(task2);
        epic.setStatus(Taskstatus.NEW);
        tM.update(epic);
        subtask1.setStatus(Taskstatus.DONE);
        tM.update(subtask1);
        subtask2.setStatus(Taskstatus.DONE);
        tM.update(subtask2);
        epic1.setStatus(Taskstatus.DONE);
        tM.update(epic1);
        subtask.setStatus(Taskstatus.IN_PROGRESS);
        tM.update(subtask);

        tM.getTask(task1.getId());
        tM.getTask(task2.getId());
        tM.getTask(task1.getId());
        tM.getSubtask(subtask.getId());
        tM.getEpic(epic.getId());
        tM.getEpic(epic.getId());
        tM.getSubtask(subtask.getId());
        tM.getEpic(epic1.getId());
        tM.getSubtask(subtask2.getId());
        tM.getSubtask(subtask1.getId());
        tM.getTask(task1.getId());

        printAllTasks(tM, hM);

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
        for (Task task : manager.getHistoryManager().getHistory()) {
            System.out.println(task);
        }
    }
}