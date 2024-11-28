import java.util.HashMap;

public class TaskManager {
    final HashMap<Integer, Task> tasks = new HashMap<>();
    final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    final HashMap<Integer, Epik> epiks = new HashMap<>();
    int nextId = 1;

    public void add(Task task) {
        task.setId(nextId++);
        tasks.put(task.getId(), task);
    }

    public void add(Epik epik) {
        epik.setId(nextId++);
        epiks.put(epik.getId(), epik);
    }

    public void add(Subtask subtask) {
        subtask.setId(nextId++);
        subtasks.put(subtask.getId(), subtask);
        Epik epik = epiks.get(subtask.epikId);
        epik.subIds.add(subtask.getId());
        if (!subtasks.isEmpty()) {
            boolean allN = false;
            boolean allD = false;
            for (Integer subId : epik.subIds) {
                Subtask aSub = subtasks.get(subId);
                if (aSub.getStatus() == Taskstatus.NEW) {
                    allN = true;
                } else {
                    allN = false;
                    break;
                }
            }
            for (Integer subId : epik.subIds) {
                Subtask aSub = subtasks.get(subId);
                if (aSub.getStatus() == Taskstatus.DONE) {
                    allD = true;
                } else {
                    allD = false;
                    break;
                }
            }
            if (allN) {
                epik.setStatus(Taskstatus.NEW);
            }
            if (allD) {
                epik.setStatus(Taskstatus.DONE);
            } else {
                epik.setStatus(Taskstatus.IN_PROGRESS);
            }
        } else {
            epik.setStatus(Taskstatus.NEW);
        }

    }

    public void update(Task task) {
        tasks.put(task.getId(), task);
    }

    public void update(Epik epik) {
        epiks.put(epik.getId(), epik);
    }

    public void update(Subtask subtask) {
        Epik epik = epiks.get(subtask.epikId);
        epik.subIds.add(subtask.getId());
        if (!subtasks.isEmpty()) {
            boolean allN = false;
            boolean allD = false;
            for (Integer subId : epik.subIds) {
                Subtask aSub = subtasks.get(subId);
                if (aSub.getStatus() == Taskstatus.NEW) {
                    allN = true;
                } else {
                    allN = false;
                    break;
                }
            }
            for (Integer subId : epik.subIds) {
                Subtask aSub = subtasks.get(subId);
                if (aSub.getStatus() == Taskstatus.DONE) {
                    allD = true;
                } else {
                    allD = false;
                    break;
                }
            }
            if (allN) {
                epik.setStatus(Taskstatus.NEW);
            }
            if (allD) {
                epik.setStatus(Taskstatus.DONE);
            } else {
                epik.setStatus(Taskstatus.IN_PROGRESS);
            }
        } else {
            epik.setStatus(Taskstatus.NEW);
        }
    }

    public void clearTasks() {
        tasks.clear();
    }

    public void clearEpiks() {
        epiks.clear();
    }

    public void clearSubtasks() {
        subtasks.clear();
    }

    public void getTasks(int id) {
        tasks.get(id);
    }

    public void getEpiks(int id) {
        epiks.get(id);
    }

    public void getSubtasks(int id) {
        subtasks.get(id);
    }

    public void delTasks(int id) {
        tasks.remove(id);
    }

    public void delEpiks(int id) {
        epiks.remove(id);
    }

    public void delSubtasks(int id) {
        subtasks.remove(id);
    }

    public void getSubsOfEpik(int id) {
        Epik epik = epiks.get(id);
        for (Integer subId : epik.subIds) {
            Subtask aSubtask = subtasks.get(subId);
            System.out.println(aSubtask);
        }
    }

}