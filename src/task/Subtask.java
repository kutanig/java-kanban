package task;

import com.google.gson.annotations.Expose;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    @Expose
    protected TypesOfTasks type = TypesOfTasks.SUBTASK;
    @Expose
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