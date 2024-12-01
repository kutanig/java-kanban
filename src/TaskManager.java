import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private int nextId = 1;
    private HashMap<Integer, Subtask> subtasks1;

    public void add(Task task) {
        task.setId(nextId++);
        tasks.put(task.getId(), task);
    }

    public void add(Epic epic) {
        epic.setId(nextId++);
        epics.put(epic.getId(), epic);
    }

    public void add(Subtask subtask) {
        subtask.setId(nextId++);
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        epic.getSubIds().add(subtask.getId());
        epicStatusUpdate(epic);
    }

    public void update(Task task) {
        if (tasks.get(task.getId()) != null) {
            tasks.put(task.getId(), task);
        }
    }

    public void update(Epic epic) {
        if (epics.get(epic.getId()) != null) {
            epics.put(epic.getId(), epic);
        }
    }

    public void update(Subtask subtask) {
        if (subtasks.get(subtask.getId()) != null) {
            subtasks.put(subtask.getId(), subtask);
            Epic epic = epics.get(subtask.getEpicId());
            epicStatusUpdate(epic);
        }
    }

    public void clearTasks() {
        tasks.clear();
    }

    public void clearEpics() {
        epics.clear();
        subtasks.clear();
    }

    public void clearSubtasks() {
        subtasks.clear();
        for (int idEpic : epics.keySet()) {
            Epic epic = epics.get(idEpic);
            epic.getSubIds().clear();
            epic.setStatus(Taskstatus.NEW);
        }
    }

    public Task getTasK(int id) {
        return tasks.get(id);
    }

    public Epic getEpic(int id) {
        return epics.get(id);
    }

    public Subtask getSubtask(int id) {
        return subtasks.get(id);
    }

    public void delTask(int id) {
        tasks.remove(id);
    }

    public void delEpic(int id) {
        for (Integer subId : epics.get(id).getSubIds()) {
            subtasks.remove(subId);
        }
        epics.remove(id);
    }

    public void delSubtask(Integer id) {
        Epic epic = epics.get(subtasks.get(id).getEpicId());
        epic.getSubIds().remove(id);
        epicStatusUpdate(epic);
        subtasks.remove(id);
    }

    public ArrayList<Subtask> getSubsOfEpic(int id) {
        ArrayList<Subtask> epicSubtasc = new ArrayList<>();
        if (epics.get(id) != null) {
            Epic epic = epics.get(id);
            for (Integer subId : epic.getSubIds()) {
                Subtask aSubtask = subtasks.get(subId);
                epicSubtasc.add(aSubtask);
            }
        }
        return epicSubtasc;
    }

    public void epicStatusUpdate(Epic epic) {
        if (epic.getSubIds().isEmpty()) {
            epic.setStatus(Taskstatus.NEW);
        } else {
            boolean allN = false;
            boolean allD = false;
            for (Integer subId : epic.getSubIds()) {
                if (subtasks.get(subId) != null) {
                    Subtask aSub = subtasks.get(subId);
                    if (aSub.getStatus() == Taskstatus.NEW) {
                        allN = true;
                    } else {
                        allN = false;
                        break;
                    }
                }
            }
            for (Integer subId : epic.getSubIds()) {
                if (subtasks.get(subId) != null) {
                    Subtask aSub = subtasks.get(subId);
                    if (aSub.getStatus() == Taskstatus.DONE) {
                        allD = true;
                    } else {
                        allD = false;
                        break;
                    }
                }
            }
            if (allN) {
                epic.setStatus(Taskstatus.NEW);
            }
            if (allD) {
                epic.setStatus(Taskstatus.DONE);
            } else {
                epic.setStatus(Taskstatus.IN_PROGRESS);
            }
        }
    }

    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }
}