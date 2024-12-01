import java.util.ArrayList;

class Epic extends  Task {
    private final ArrayList<Integer> subIds = new ArrayList<>();

    public Epic(String name, String description, Taskstatus status) {
        super(name, description, status);
    }

    public ArrayList<Integer> getSubIds() {
        return subIds;
    }
}