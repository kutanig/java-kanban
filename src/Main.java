public class Main {
    public static void main(String[] args) {
        TaskManager tm = new TaskManager();

        Task task1 = new Task("задача1", "описание", Taskstatus.NEW);
        tm.add(task1);
        Task task2 = new Task("задача2","онисание2",Taskstatus.DONE);
        tm.add(task2);

        Epic epic = new Epic("эпик первый","утро",Taskstatus.DONE);
        tm.add(epic);
        Subtask subtask1 = new Subtask("подзадача","умытся",Taskstatus.IN_PROGRESS, epic.getId());
        tm.add(subtask1);
        Subtask subtask2 = new Subtask("подзадача2","побрится",Taskstatus.NEW, epic.getId());
        tm.add(subtask2);

        Epic epic1 = new Epic("эпик второй","сходить в магазин",Taskstatus.NEW);
        tm.add(epic1);
        Subtask subtask = new Subtask("подзадача","встать с дивана",Taskstatus.DONE, epic1.getId());
        tm.add(subtask);

        System.out.println("Задачи:");
        System.out.println(task1);
        System.out.println(task2);
        System.out.println();
        System.out.println("Эпики:");
        System.out.println(epic);
        System.out.println(epic1);
        System.out.println();
        System.out.println("Подзадачи:");
        System.out.println();
        System.out.println(subtask1);
        System.out.println(subtask2);
        System.out.println(subtask);
        tm.delSubtask(4);
        task1.setStatus(Taskstatus.IN_PROGRESS);
        tm.update(task1);
        task2.setStatus(Taskstatus.NEW);
        tm.update(task2);

        epic.setStatus(Taskstatus.NEW);
        tm.update(epic);
        subtask1.setStatus(Taskstatus.DONE);
        tm.update(subtask1);
        subtask2.setStatus(Taskstatus.DONE);
        tm.update(subtask2);

        epic1.setStatus(Taskstatus.DONE);
        tm.update(epic1);
        subtask.setStatus(Taskstatus.IN_PROGRESS);
        tm.update(subtask);

        System.out.println("Задачи:");
        System.out.println(task1);
        System.out.println(task2);
        System.out.println();
        System.out.println("Эпики:");
        System.out.println(epic);
        System.out.println(epic1);
        System.out.println();
        System.out.println("Подзадачи:");
        System.out.println();
        System.out.println(subtask1);
        System.out.println(subtask2);
        System.out.println(subtask);
        System.out.println();

        tm.delTask(task1.getId());
        tm.delEpic(epic1.getId());
    }
}