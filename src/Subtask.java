public class Subtask extends Task{
    private int epicId;

    public Subtask(String name, String description, Taskstatus status, int epiсId) {
        super(name, description, status);
        this.epicId = epiсId;

    }

    public int getEpiсId() {
        return epicId;
    }

    public void setEpiсId(int epiсId) {
        this.epicId = epiсId;
    }
}