package manager;

import exceptions.ManagerLoadException;
import exceptions.ManagerSaveException;
import task.*;

import java.io.*;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File savedFile;

    public FileBackedTaskManager(File savedFile) {
        this.savedFile = savedFile;
    }

    public void save() throws ManagerSaveException {
        try (Writer writer = new FileWriter(savedFile)) {
            for (Task task : super.getTasks()) {
                writer.write(convertToString(task) + "\n");
            }
            for (Epic epic : super.getEpics()) {
                writer.write(convertToString(epic) + "\n");
            }
            for (Subtask subtask : super.getSubtasks()) {
                writer.write(convertToString(subtask) + "\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Saving error: " + e.getMessage());
        }
    }

    private String convertToString(Task task) {
        return String.format("%s,%s,%s,%s,%s", task.getId(), task.getType(), task.getName(), task.getStatus(), task.getDescription());
    }

    private String convertToString(Subtask subtask) {
        return String.format("%s,%s,%s,%s,%s,%s", subtask.getId(), subtask.getType(), subtask.getName(), subtask.getStatus(), subtask.getDescription(), subtask.getEpicId());
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager fileBackedTM = Managers.getBackedTMDefault();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while (reader.ready()) {
                String[] taskData = reader.readLine().split(",");
                switch (TypesOfTasks.valueOf(taskData[1])) {
                    case TASK:
                        fileBackedTM.add(Task.fromString(taskData));
                        break;
                    case EPIC:
                        fileBackedTM.add(Epic.fromString(taskData));
                        break;
                    case SUBTASK:
                        fileBackedTM.add(Subtask.fromString(taskData));
                        break;
                }
            }
        } catch (IOException e) {
            throw new ManagerLoadException("Error reading file: " + e.getMessage());
        }
        return fileBackedTM;
    }

    @Override
    public void add(Task task) {
        super.add(task);
        save();
    }

    @Override
    public void add(Epic epic) {
        super.add(epic);
        save();
    }

    @Override
    public void add(Subtask subtask) {
        super.add(subtask);
        save();
    }

    @Override
    public void update(Task task) {
        super.update(task);
        save();
    }

    @Override
    public void update(Epic epic) {
        super.update(epic);
        save();
    }

    @Override
    public void update(Subtask subtask) {
        super.update(subtask);
        save();
    }

    @Override
    public void clearTasks() {
        super.clearTasks();
        save();
    }

    @Override
    public void clearEpics() {
        super.clearEpics();
        save();
    }

    @Override
    public void clearSubtasks() {
        super.clearSubtasks();
        save();
    }

    @Override
    public void removeTask(int id) {
        super.removeTask(id);
        save();
    }

    @Override
    public void removeEpic(int id) {
        super.removeEpic(id);
        save();
    }

    @Override
    public void removeSubtask(int id) {
        super.removeSubtask(id);
        save();
    }
}
