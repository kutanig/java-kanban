import java.util.ArrayList;

class Epiс extends  Task{
    private final ArrayList<Integer> subIds = new ArrayList<>();

    public Epiс(String name, String description, Taskstatus status) {
        super(name, description, status);
    }

    public ArrayList<Integer> getSubIds() {
        return subIds;
    }
}