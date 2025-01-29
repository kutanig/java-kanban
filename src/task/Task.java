package task;

import java.util.Objects;

public class Task {

    protected Integer id = 1;
    protected String name;
    protected String description;
    protected Taskstatus status;
    protected TypesOfTasks type = TypesOfTasks.TASK;


    public Task(String name, String description, Taskstatus status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Task(Integer id, String name, String description, Taskstatus status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Taskstatus getStatus() {
        return status;
    }

    public void setStatus(Taskstatus status) {
        this.status = status;
    }

    public TypesOfTasks getType() {
        return type;
    }

    public void setType(TypesOfTasks type) {
        this.type = type;
    }

    public static Task fromString(String[] value) {
        return new Task(Integer.parseInt(value[0]), value[2], value[4], Taskstatus.valueOf(value[3]));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        if (id == task.id) return true;
        return Objects.equals(id, task.id) && Objects.equals(name, task.name) && Objects.equals(description, task.description) && status == task.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, status);
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}