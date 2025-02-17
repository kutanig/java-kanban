package task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {
    protected TypesOfTasks type = TypesOfTasks.EPIC;
    private final ArrayList<Integer> subIds = new ArrayList<>();
    private LocalDateTime epicEndTime;

    public Epic(String name, String description, Taskstatus status) {
        super(name, description, status);
    }

    public Epic(Integer id, String name, String description, Taskstatus status, LocalDateTime startTime, Duration duration, LocalDateTime endTime) {
        super(id, name, description, status, startTime, duration);
        this.epicEndTime = endTime;
    }

    public Epic(Integer id, String name, String description, Taskstatus status, Duration duration) {
        super(id, name, description, status, duration);
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

    public LocalDateTime getEpicEndTime() {
        return epicEndTime;
    }

    public static Epic fromString(String[] value) {
        if (value[5].equals("null") && value[6].equals("null") && value[7].equals("null")) {
            return new Epic(Integer.parseInt(value[0]), value[2], value[4], Taskstatus.valueOf(value[3]));
        } else if (value[5].equals("null") && value[7].equals("null")) {
            return new Epic(Integer.parseInt(value[0]), value[2], value[4], Taskstatus.valueOf(value[3]), Duration.parse(value[6]));
        }
        return new Epic(Integer.parseInt(value[0]), value[2], value[4], Taskstatus.valueOf(value[3]), LocalDateTime.parse(value[5]), Duration.parse(value[6]), LocalDateTime.parse(value[7]));
    }

    @Override
    public TypesOfTasks getType() {
        return type;
    }

    @Override
    public void setType(TypesOfTasks type) {
        this.type = type;
    }

    public void setEpicEndTime(LocalDateTime epicEndTime) {
        this.epicEndTime = epicEndTime;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", type=" + type +
                ", startTime=" + startTime +
                ", duration=" + duration +
                ", endTime=" + epicEndTime +
                '}';
    }
}