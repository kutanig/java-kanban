package task;

import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<Integer> subIds = new ArrayList<>();

    public Epic(String name, String description, Taskstatus status) {
        super(name, description, status);
    }

    public Epic(Integer id, String name, String description, Taskstatus status) {
        super(id, name, description, status);
    }

    public ArrayList<Integer> getSubIds() {
        return subIds;
    }

    public void addSubtask(int subtaskId) {
        if (subtaskId != this.getId()) {
            subIds.add(subtaskId);
        }
    }
}