package Test;

import Manager.*;
import Tasc.Epic;
import Tasc.Subtask;
import Tasc.Task;
import Tasc.Taskstatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    TaskManager taskManager;
    Task task1;
    Task task2;
    Epic epic1;
    Epic epic2;

    @BeforeEach
    void setUp() {
        taskManager = Managers.getDefault();
        task1 = new Task("test task1","description task1",Taskstatus.NEW);
        task2 = new Task("test task1","description task2",Taskstatus.NEW);
        epic1 = new Epic("Test epic1","description epic1", Taskstatus.NEW);
        epic2 = new Epic("Test epic2","description epic2", Taskstatus.NEW);
    }

    @Test
    void getAllTasks() {
        taskManager.add(task1);
        taskManager.add(task2);
        List<Task> tasks = taskManager.getTasks();
        assertNotNull(tasks);
        assertEquals(2, tasks.size());
        assertTrue(tasks.contains(task1));
        assertTrue(tasks.contains(task2));
    }

    @Test
    void removeAllTasks() {
        taskManager.add(task1);
        taskManager.add(task2);
        taskManager.clearTasks();
        List<Task> tasks = taskManager.getTasks();
        assertTrue(tasks.isEmpty());
    }

    @Test
    void removeTaskById() {
        taskManager.add(task1);
        taskManager.add(task2);
        taskManager.delTask(task1.getId());
        List<Task> tasks = taskManager.getTasks();
        assertFalse(tasks.contains(task1));
        assertTrue(tasks.contains(task2));
    }

    @Test
    void getAllEpics() {
        taskManager.add(epic1);
        taskManager.add(epic2);
        List<Epic> epics = taskManager.getEpics();
        assertNotNull(epics);
        assertEquals(2, epics.size());
        assertTrue(epics.contains(epic1));
        assertTrue(epics.contains(epic2));
    }

    @Test
    void removeAllEpicsAndRemoveAllSubtasks() {
        taskManager.add(epic1);
        taskManager.add(epic2);
        Subtask subtask1 = new Subtask("test sub1","description1",Taskstatus.NEW, epic1.getId());
        Subtask subtask2 = new Subtask("test sub2", "description2", Taskstatus.NEW, epic1.getId());
        taskManager.add(subtask1);
        taskManager.add(subtask2);
        taskManager.clearEpics();
        List<Epic> epics = taskManager.getEpics();
        List<Subtask> subtasks = taskManager.getSubtasks();
        assertTrue(epics.isEmpty());
        assertTrue(subtasks.isEmpty());
    }

    @Test
    void getAllSubtasks() {
        taskManager.add(epic1);
        Subtask subtask1 = new Subtask("test sub1","description1",Taskstatus.NEW, epic1.getId());
        Subtask subtask2 = new Subtask("test sub2", "description2", Taskstatus.NEW, epic1.getId());
        taskManager.add(subtask1);
        taskManager.add(subtask2);
        List<Subtask> subtasks = taskManager.getSubtasks();
        assertNotNull(subtasks);
        assertEquals(2, subtasks.size());
        assertTrue(subtasks.contains(subtask1));
        assertTrue(subtasks.contains(subtask2));
    }

    @Test
    void updatingTheSubtaskShouldChangeTheStatusOfEpic() {
        taskManager.add(epic1);
        Subtask subtask1 = new Subtask("test sub1","description1",Taskstatus.NEW, epic1.getId());
        Subtask subtask2 = new Subtask("test sub2", "description2", Taskstatus.NEW, epic1.getId());
        taskManager.add(subtask1);
        taskManager.add(subtask2);
        subtask1.setStatus(Taskstatus.DONE);
        taskManager.update(subtask1);
        assertEquals(Taskstatus.IN_PROGRESS, epic1.getStatus());
    }


}