package manager;

import task.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final Set<Task> sortedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));
    private int nextId = 1;
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    private void epicStatusUpdate(Epic epic) {
        if (epic.getSubIds().isEmpty()) {
            epic.setStatus(Taskstatus.NEW);
            return;
        }
        boolean allNew = epic.getSubIds().stream()
                .map(subId -> subtasks.get(subId).getStatus())
                .allMatch(Predicate.isEqual(Taskstatus.NEW));
        boolean allDone = epic.getSubIds().stream()
                .map(subId -> subtasks.get(subId).getStatus())
                .allMatch(Predicate.isEqual(Taskstatus.DONE));
        if (allNew) {
            epic.setStatus(Taskstatus.NEW);
        } else if (allDone) {
            epic.setStatus(Taskstatus.DONE);
        } else {
            epic.setStatus(Taskstatus.IN_PROGRESS);
        }
    }

    private void epicTimeCalculate(Epic epic) {
        LocalDateTime epicStartDateTime = epic.getSubIds().stream()
                .map(subId -> subtasks.get(subId).getStartTime())
                .filter(Objects::nonNull)
                .min(Comparator.naturalOrder())
                .orElse(null);
        epic.setStartTime(epicStartDateTime);
        LocalDateTime epicEndTime = epic.getSubIds().stream()
                .map(subId -> subtasks.get(subId).getEndTime())
                .filter(Objects::nonNull)
                .max(Comparator.naturalOrder())
                .orElse(null);
        epic.setEpicEndTime(epicEndTime);
        Duration epicDuration = epic.getSubIds().stream()
                .map(subId -> subtasks.get(subId).getDuration())
                .filter(Objects::nonNull)
                .reduce(Duration.ZERO, Duration::plus);
        epic.setDuration(epicDuration);
    }

    private boolean timeIntersection(Task task1, Task task2) {
        return task1.getStartTime().isBefore(task2.getEndTime()) && task1.getEndTime().isAfter(task2.getStartTime());
    }

    @Override
    public void add(Task task) {
        if (getPrioritizedTasks().stream().anyMatch(t -> timeIntersection(task, t))) {
            System.out.println(task.getName() + " overlaps in time with another task.");
        } else {
            if (task.getId() == 1) {
                task.setId(nextId++);
                tasks.put(task.getId(), task);
            } else {
                tasks.put(task.getId(), task);
                nextId = task.getId() + 1;
            }
            if (task.getStartTime() != null && task.getDuration() != null) {
                sortedTasks.add(task);
            }
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
        if (getPrioritizedTasks().stream().anyMatch(t -> timeIntersection(subtask, t))) {
            System.out.println(subtask.getName() + " overlaps in time with another task.");
        } else {
            if (subtask.getId() == 1) {
                subtask.setId(nextId++);
                subtasks.put(subtask.getId(), subtask);
                Epic epic = epics.get(subtask.getEpicId());
                epic.getSubIds().add(subtask.getId());
                epicStatusUpdate(epic);
                if (subtask.getStartTime() != null && subtask.getDuration() != null) {
                    sortedTasks.add(subtask);
                    epicTimeCalculate(epic);
                }
            } else {
                subtasks.put(subtask.getId(), subtask);
                Epic epic = epics.get(subtask.getEpicId());
                epic.getSubIds().add(subtask.getId());
                epicStatusUpdate(epic);
                nextId = subtask.getId() + 1;
                if (subtask.getStartTime() != null && subtask.getDuration() != null) {
                    sortedTasks.add(subtask);
                    epicTimeCalculate(epic);
                }
            }
        }
    }

    @Override
    public void update(Task task) {
        if (tasks.get(task.getId()) != null) {
            sortedTasks.remove(tasks.get(task.getId()));
            if (getPrioritizedTasks().stream().anyMatch(t -> timeIntersection(task, t))) {
                System.out.println(task.getName() + " update error: overlaps in time with another task.");
                sortedTasks.add(tasks.get(task.getId()));
            } else {
                tasks.put(task.getId(), task);
            }
            if (task.getStartTime() != null && task.getDuration() != null) {
                sortedTasks.add(task);
            }
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
            sortedTasks.remove(tasks.get(subtask.getId()));
            if (getPrioritizedTasks().stream().anyMatch(t -> timeIntersection(subtask, t))) {
                System.out.println(subtask.getName() + " update error: overlaps in time with another task.");
                sortedTasks.add(tasks.get(subtask.getId()));
            } else {
                subtasks.put(subtask.getId(), subtask);
                Epic epic = epics.get(subtask.getEpicId());
                epicStatusUpdate(epic);
                if (subtask.getStartTime() != null && subtask.getDuration() != null) {
                    sortedTasks.add(subtask);
                    epicTimeCalculate(epic);
                }
            }
        }
    }

    @Override
    public void clearTasks() {
        tasks.keySet().forEach(historyManager::remove);
        tasks.values().forEach(sortedTasks::remove);
        tasks.clear();
    }

    @Override
    public void clearEpics() {
        epics.keySet().forEach(historyManager::remove);
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void clearSubtasks() {
        subtasks.keySet().forEach(historyManager::remove);
        subtasks.values().forEach(sortedTasks::remove);
        subtasks.clear();
        epics.keySet().stream().map(this::getEpic).forEach(epic -> epic.getSubIds().clear());
        epics.keySet().stream().map(this::getEpic).forEach(epic -> epic.setStatus(Taskstatus.NEW));
        epics.keySet().stream().map(this::getEpic).forEach(this::epicTimeCalculate);
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
        sortedTasks.remove(tasks.get(id));
        historyManager.remove(id);
        tasks.remove(id);
    }

    @Override
    public void removeEpic(int id) {
        epics.get(id).getSubIds().forEach(historyManager::remove);
        epics.get(id).getSubIds().forEach(subtasks::remove);
        epics.get(id).getSubIds().forEach(idSub -> sortedTasks.remove(subtasks.get(idSub)));
        historyManager.remove(id);
        epics.remove(id);
    }

    @Override
    public void removeSubtask(int id) {
        Epic epic = epics.get(subtasks.get(id).getEpicId());
        epic.getSubIds().remove((Integer) id);
        sortedTasks.remove(subtasks.get(id));
        historyManager.remove(id);
        if (subtasks.get(id).getDuration() != null) {
            epicTimeCalculate(epic);
        }
        subtasks.remove(id);
        epicStatusUpdate(epic);
    }

    @Override
    public List<Subtask> getSubsOfEpic(int id) {
        return epics.get(id).getSubIds().stream()
                .map(subtasks::get)
                .collect(Collectors.toList());
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

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(sortedTasks);
    }
}