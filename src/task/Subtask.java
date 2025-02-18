package task;

public class Subtask extends Task {
    protected TypesOfTasks type = TypesOfTasks.SUBTASK;
    private int epicId;

    public Subtask(String name, String description, Taskstatus status, int epicId) {
        super(name, description, status);
        this.epicId = epicId;

    }

    public Subtask(Integer id, String name, String description, Taskstatus status, int epicId) {
        super(id, name, description, status);
        this.epicId = epicId;
    }

    public static Subtask fromString(String[] value) {
        return new Subtask(Integer.parseInt(value[0]), value[2], value[4], Taskstatus.valueOf(value[3]), Integer.parseInt(value[5]));
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
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