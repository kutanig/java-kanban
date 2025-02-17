package task;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    protected TypesOfTasks type = TypesOfTasks.SUBTASK;
    private int epicId;

    public Subtask(Integer id, String name, String description, Taskstatus status, LocalDateTime startTime, Duration duration, int epicId) {
        super(id, name, description, status, startTime, duration);
        this.epicId = epicId;
    }

    public Subtask(Integer id, String name, String description, Taskstatus status, int epicId) {
        super(id, name, description, status);
        this.epicId = epicId;
    }

    public Subtask(Integer id, String name, String description, Taskstatus status, Duration duration, int epicId) {
        super(id, name, description, status, duration);
        this.epicId = epicId;
    }

    public Subtask(Integer id, String name, String description, Taskstatus status, LocalDateTime startTime, int epicId) {
        super(id, name, description, status, startTime);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, Taskstatus status, LocalDateTime startTime, Duration duration, int epicId) {
        super(name, description, status, startTime, duration);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, Taskstatus status, int epicId) {
        super(name, description, status);
        this.epicId = epicId;
    }

    public static Subtask fromString(String[] value) {
        if (value[5].equals("null") && value[6].equals("null")) {
            return new Subtask(Integer.parseInt(value[0]), value[2], value[4], Taskstatus.valueOf(value[3]), Integer.parseInt(value[7]));
        } else if (value[5].equals("null")) {
            return new Subtask(Integer.parseInt(value[0]), value[2], value[4], Taskstatus.valueOf(value[3]), Duration.parse(value[6]), Integer.parseInt(value[7]));
        } else if (value[6].equals("null")) {
            return new Subtask(Integer.parseInt(value[0]), value[2], value[4], Taskstatus.valueOf(value[3]), LocalDateTime.parse(value[5]), Integer.parseInt(value[7]));
        }
        return new Subtask(Integer.parseInt(value[0]), value[2], value[4], Taskstatus.valueOf(value[3]), LocalDateTime.parse(value[5]), Duration.parse(value[6]), Integer.parseInt(value[7]));
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