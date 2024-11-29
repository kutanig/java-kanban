import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final HashMap<Integer, Epiс> epiсs = new HashMap<>();
    private int nextId = 1;

    public void add(Task task) {
        task.setId(nextId++);
        tasks.put(task.getId(), task);
    }

    public void add(Epiс epiс) {
        epiс.setId(nextId++);
        epiсs.put(epiс.getId(), epiс);
    }

    public void add(Subtask subtask) {
        subtask.setId(nextId++);
        subtasks.put(subtask.getId(), subtask);
        Epiс epiс = epiсs.get(subtask.getEpiсId());
        epiс.getSubIds().add(subtask.getId());
        epicStatusUpdate(epiс);
    }

    public void update(Task task) {
        if (tasks.get(task.getId()) != null) {
            tasks.put(task.getId(), task);
        }
    }

    public void update(Epiс epic) {
        if (epiсs.get(epic.getId()) != null) {
            epiсs.put(epic.getId(), epic);
        }
    }

    public void update(Subtask subtask) {
        if (subtasks.get(subtask.getId()) != null) {
            subtasks.put(subtask.getId(), subtask);
            Epiс epiс = epiсs.get(subtask.getEpiсId());
            epicStatusUpdate(epiс);
        }
    }

    public void clearTasks() {
        tasks.clear();
    }

    public void clearEpiсs() {
        epiсs.clear();
        subtasks.clear();
    }

    public void clearSubtasks() {
        subtasks.clear();
        for (int idEpic : epiсs.keySet()) {
            Epiс epiс = epiсs.get(idEpic);
            epiс.getSubIds().clear();
            epiс.setStatus(Taskstatus.NEW);
        }
    }

    public Task getTasK(int id) {
        return tasks.get(id);
    }

    public Epiс getEpiс(int id) {
        return epiсs.get(id);
    }

    public Subtask getSubtask(int id) {
        return subtasks.get(id);
    }

    public void delTask(int id) {
        tasks.remove(id);
    }

    public void delEpiс(int id) {
        for (Integer subId : epiсs.get(id).getSubIds()) {
            subtasks.remove(subId);
        }
        epiсs.remove(id);
    }

    public void delSubtask(int id) {
        Epiс epiс = epiсs.get(subtasks.get(id).getEpiсId());
        epicStatusUpdate(epiс);
        subtasks.remove(id);

    }

    public ArrayList<Subtask> getSubsOfEpic(int id) {
        ArrayList<Subtask> epicSubtasc = new ArrayList<>();
        if (epiсs.get(id) != null) {
            Epiс epiс = epiсs.get(id);
            for (Integer subId : epiс.getSubIds()) {
                Subtask aSubtask = subtasks.get(subId);
                epicSubtasc.add(aSubtask);
            }
        }
        return epicSubtasc;
    }

    public void epicStatusUpdate(Epiс epiс) {
        boolean allN = false;
        boolean allD = false;
        for (Integer subId : epiс.getSubIds()) {
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
        for (Integer subId : epiс.getSubIds()) {
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
            epiс.setStatus(Taskstatus.NEW);
        }
        if (allD) {
            epiс.setStatus(Taskstatus.DONE);
        } else {
            epiс.setStatus(Taskstatus.IN_PROGRESS);
        }
    }
}