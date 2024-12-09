package Tasc;

public class Subtask extends Task {
    private int epicId;

    public Subtask(String name, String description, Taskstatus status, int epicId) {
        super(name, description, status);
        this.epicId = epicId;

    }

    public Subtask(Integer id, String name, String description, Taskstatus status, int epicId) {
        super(id, name, description, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }
}