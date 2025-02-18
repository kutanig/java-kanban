package task;

import java.util.ArrayList;

public class Epic extends Task {
    protected TypesOfTasks type = TypesOfTasks.EPIC;
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

    public static Epic fromString(String[] value) {
        return new Epic(Integer.parseInt(value[0]), value[2], value[4], Taskstatus.valueOf(value[3]));
    }

    @Override
    public TypesOfTasks getType() {
        return type;
    }

    @Override
    public void setType(TypesOfTasks type) {
        this.type = type;
    }
}