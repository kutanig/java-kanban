public class Main {
    public static void main(String[] args) {
        TaskManager tm = new TaskManager();

        Task task1 = new Task("задача1", "описание", Taskstatus.NEW);
        tm.add(task1);
        Task task2 = new Task("задача2","онисание2",Taskstatus.DONE);
        tm.add(task2);

        Epiс epiс = new Epiс("эпик первый","утро",Taskstatus.DONE);
        tm.add(epiс);
        Subtask subtask1 = new Subtask("подзадача","умытся",Taskstatus.IN_PROGRESS, epiс.getId());
        tm.add(subtask1);
        Subtask subtask2 = new Subtask("подзадача2","побрится",Taskstatus.NEW, epiс.getId());
        tm.add(subtask2);

        Epiс epiс1 = new Epiс("эпик второй","сходить в магазин",Taskstatus.NEW);
        tm.add(epiс1);
        Subtask subtask = new Subtask("подзадача","встать с дивана",Taskstatus.DONE, epiс1.getId());
        tm.add(subtask);

        System.out.println("Задачи:");
        System.out.println(task1);
        System.out.println(task2);
        System.out.println();
        System.out.println("Эпики:");
        System.out.println(epiс);
        System.out.println(epiс1);
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

        epiс.setStatus(Taskstatus.NEW);
        tm.update(epiс);
        subtask1.setStatus(Taskstatus.DONE);
        tm.update(subtask1);
        subtask2.setStatus(Taskstatus.DONE);
        tm.update(subtask2);

        epiс1.setStatus(Taskstatus.DONE);
        tm.update(epiс1);
        subtask.setStatus(Taskstatus.IN_PROGRESS);
        tm.update(subtask);

        System.out.println("Задачи:");
        System.out.println(task1);
        System.out.println(task2);
        System.out.println();
        System.out.println("Эпики:");
        System.out.println(epiс);
        System.out.println(epiс1);
        System.out.println();
        System.out.println("Подзадачи:");
        System.out.println();
        System.out.println(subtask1);
        System.out.println(subtask2);
        System.out.println(subtask);
        System.out.println();

        tm.delTask(task1.getId());
        tm.delEpiс(epiс1.getId());
    }
}