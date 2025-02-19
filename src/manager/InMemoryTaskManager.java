package manager;

import task.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private int nextId = 1;
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public void add(Task task) {
        if (task.getId() == 1) {
            task.setId(nextId++);
            tasks.put(task.getId(), task);
        } else {
            tasks.put(task.getId(), task);
            nextId = task.getId() + 1;
        }
    }

    @Override
    public void add(Epic epic) {
        if (epic.getId() == 1) {
            epic.setId(nextId++);
            epics.put(epic.getId(), epic);
        } else {
            epics.put(epic.getId(), epic);
            nextId = epic.getId() + 1;
        }
    }

    @Override
    public void add(Subtask subtask) {
        if (subtask.getId() == 1) {
            subtask.setId(nextId++);
            subtasks.put(subtask.getId(), subtask);
            Epic epic = epics.get(subtask.getEpicId());
            epic.getSubIds().add(subtask.getId());
            epicStatusUpdate(epic);
        } else {
            subtasks.put(subtask.getId(), subtask);
            Epic epic = epics.get(subtask.getEpicId());
            epic.getSubIds().add(subtask.getId());
            epicStatusUpdate(epic);
            nextId = subtask.getId() + 1;
        }
    }

    @Override
    public void update(Task task) {
        if (tasks.get(task.getId()) != null) {
            tasks.put(task.getId(), task);
        }
    }

    @Override
    public void update(Epic epic) {
        if (epics.get(epic.getId()) != null) {
            epics.put(epic.getId(), epic);
        }
    }

    @Override
    public void update(Subtask subtask) {
        if (subtasks.get(subtask.getId()) != null) {
            subtasks.put(subtask.getId(), subtask);
            Epic epic = epics.get(subtask.getEpicId());
            epicStatusUpdate(epic);
        }
    }

    @Override
    public void clearTasks() {
        for (int idTask : tasks.keySet()) {
            historyManager.remove(idTask);
        }
        tasks.clear();
    }

    @Override
    public void clearEpics() {
        for (int idEpic : epics.keySet()) {
            historyManager.remove(idEpic);
        }
        for (int idSubtask : subtasks.keySet()) {
            historyManager.remove(idSubtask);
        }
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void clearSubtasks() {
        for (int idSubtask : subtasks.keySet()) {
            historyManager.remove(idSubtask);
        }
        subtasks.clear();
        for (int idEpic : epics.keySet()) {
            Epic epic = epics.get(idEpic);
            epic.getSubIds().clear();
            epic.setStatus(Taskstatus.NEW);
        }
    }

    @Override
    public Task getTask(int id) {
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Epic getEpic(int id) {
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public Subtask getSubtask(int id) {
        historyManager.add(subtasks.get(id));
        return subtasks.get(id);
    }

    @Override
    public void removeTask(int id) {
        historyManager.remove(id);
        tasks.remove(id);
    }

    @Override
    public void removeEpic(int id) {
        for (Integer subId : epics.get(id).getSubIds()) {
            historyManager.remove(subId);
            subtasks.remove(subId);
        }
        historyManager.remove(id);
        epics.remove(id);
    }

    @Override
    public void removeSubtask(int id) {
        Epic epic = epics.get(subtasks.get(id).getEpicId());
        epic.getSubIds().remove((Integer) id);
        epicStatusUpdate(epic);
        historyManager.remove(id);
        subtasks.remove(id);
    }

    @Override
    public ArrayList<Subtask> getSubsOfEpic(int id) {
        ArrayList<Subtask> epicSub = new ArrayList<>();
        if (epics.get(id) != null) {
            Epic epic = epics.get(id);
            for (Integer subId : epic.getSubIds()) {
                Subtask aSubtask = subtasks.get(subId);
                epicSub.add(aSubtask);
            }
        }
        return epicSub;
    }

    private void epicStatusUpdate(Epic epic) {
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

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}