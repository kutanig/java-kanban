package Manager;

import Tasc.Epic;
import Tasc.Subtask;
import Tasc.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    void add(Task task);

    void add(Epic epic);

    void add(Subtask subtask);

    void update(Task task);

    void update(Epic epic);

    void update(Subtask subtask);

    void clearTasks();

    void clearEpics();

    void clearSubtasks();

    Task getTask(int id);

    Epic getEpic(int id);

    Subtask getSubtask(int id);

    void delTask(int id);

    void delEpic(int id);

    void delSubtask(int id);

    ArrayList<Subtask> getSubsOfEpic(int id);

    List<Task> getTasks();

    List<Subtask> getSubtasks();

    List<Epic> getEpics();

    List<Task> getHistory();
}

