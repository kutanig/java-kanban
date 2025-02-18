import manager.FileBackedTaskManager;
import manager.Managers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;
import task.Task;
import task.Taskstatus;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;
class FileBackedTaskManagerTest {
    FileBackedTaskManager fileBackedTaskManager;
    Task task1;
    Task task2;
    Epic epic1;

    @BeforeEach
    void setUp() {
        fileBackedTaskManager = Managers.getBackedTMDefault();
        task1 = new Task("test task1", "description task1", Taskstatus.NEW);
        task2 = new Task("test task1", "description task2", Taskstatus.NEW);
        epic1 = new Epic("Test epic1", "description epic1", Taskstatus.NEW);
    }

    @Test
    void savingAndUploadingAnEmptyFile() {
        fileBackedTaskManager.add(task1);
        fileBackedTaskManager.add(task2);
        fileBackedTaskManager.add(epic1);
        Subtask subtask = new Subtask("sub1","description",Taskstatus.IN_PROGRESS,epic1.getId());
        fileBackedTaskManager.add(subtask);
        fileBackedTaskManager.clearTasks();
        fileBackedTaskManager.clearEpics();
        fileBackedTaskManager = FileBackedTaskManager.loadFromFile(new  File("src/savedTM.txt"));
        assertTrue(fileBackedTaskManager.getTasks().isEmpty());
        assertTrue(fileBackedTaskManager.getEpics().isEmpty());
        assertTrue(fileBackedTaskManager.getSubtasks().isEmpty());
    }

    @Test
    void savingAndLoadingTasks() {
        fileBackedTaskManager.add(task1);
        fileBackedTaskManager.add(task2);
        fileBackedTaskManager.add(epic1);
        Subtask subtask = new Subtask("sub1","description",Taskstatus.IN_PROGRESS,epic1.getId());
        fileBackedTaskManager.add(subtask);
        fileBackedTaskManager = FileBackedTaskManager.loadFromFile(new  File("src/savedTM.txt"));
        assertNotNull(fileBackedTaskManager.getTasks());
        assertEquals(2, fileBackedTaskManager.getTasks().size());
        assertEquals(1, fileBackedTaskManager.getEpics().size());
        assertEquals(1, fileBackedTaskManager.getSubtasks().size());
        assertTrue(fileBackedTaskManager.getTasks().contains(task1));
        assertTrue(fileBackedTaskManager.getTasks().contains(task2));
        assertTrue(fileBackedTaskManager.getEpics().contains(epic1));
        assertTrue(fileBackedTaskManager.getSubtasks().contains(subtask));
    }
}