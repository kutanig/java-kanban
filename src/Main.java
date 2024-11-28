public class Main {
    public static void main(String[] args) {
        TaskManager tm = new TaskManager();

        Task task1 = new Task("задача1", "описание", Taskstatus.NEW);
        tm.add(task1);
        Task task2 = new Task("задача2","онисание2",Taskstatus.DONE);
        tm.add(task2);

        Epik epik = new Epik("эпик первый","утро",Taskstatus.DONE);
        tm.add(epik);
        Subtask subtask1 = new Subtask("подзадача","умытся",Taskstatus.IN_PROGRESS);
        subtask1.epikId = epik.getId();
        tm.add(subtask1);
        Subtask subtask2 = new Subtask("подзадача2","побрится",Taskstatus.NEW);
        subtask2.epikId = epik.getId();
        tm.add(subtask2);

        Epik epik1 = new Epik("эпик второй","сходить в магазин",Taskstatus.NEW);
        tm.add(epik1);
        Subtask subtask = new Subtask("подзадача","встать с дивана",Taskstatus.DONE);
        subtask.epikId = epik1.getId();
        tm.add(subtask);

        System.out.println("Задачи:");
        System.out.println(task1);
        System.out.println(task2);
        System.out.println();
        System.out.println("Эпики:");
        System.out.println(epik);
        System.out.println(epik1);
        System.out.println();
        System.out.println("Подзадачи:");
        System.out.println();
        System.out.println(subtask1);
        System.out.println(subtask2);
        System.out.println(subtask);

        task1.setStatus(Taskstatus.IN_PROGRESS);
        tm.update(task1);
        task2.setStatus(Taskstatus.NEW);
        tm.update(task2);

        epik.setStatus(Taskstatus.NEW);
        tm.update(epik);
        subtask1.setStatus(Taskstatus.DONE);
        tm.update(subtask1);
        subtask2.setStatus(Taskstatus.DONE);
        tm.update(subtask2);

        epik1.setStatus(Taskstatus.DONE);
        tm.update(epik1);
        subtask.setStatus(Taskstatus.IN_PROGRESS);
        tm.update(subtask);

        System.out.println("Задачи:");
        System.out.println(task1);
        System.out.println(task2);
        System.out.println();
        System.out.println("Эпики:");
        System.out.println(epik);
        System.out.println(epik1);
        System.out.println();
        System.out.println("Подзадачи:");
        System.out.println();
        System.out.println(subtask1);
        System.out.println(subtask2);
        System.out.println(subtask);

        tm.delTasks(task1.getId());
        tm.delEpiks(epik1.getId());

    }
}