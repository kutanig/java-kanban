package test;

import manager.HistoryManager;
import manager.Managers;
import manager.TaskManager;
import task.Epic;
import task.Subtask;
import task.Task;
import task.Taskstatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    TaskManager taskManager;
    HistoryManager historyManager;

    @BeforeEach
    void setUp() {
        taskManager = Managers.getDefault();
        historyManager = Managers.getDefaultHistory();
    }

    @Test
    void addAndGetHistory() {
        Task task1 = new Task(1, "test task1", "description1", Taskstatus.NEW);
        historyManager.add(task1);
        Epic epic1 = new Epic(2, "Test epic1", "description epic1", Taskstatus.NEW);
        historyManager.add(epic1);
        assertTrue(historyManager.getHistory().contains(task1));
        assertTrue(historyManager.getHistory().contains(epic1));
        assertEquals(2, historyManager.getHistory().size());
    }

    @Test
    void removeTaskInHistory() {
        for (int i = 0; i < 15; i++) {
            taskManager.add(new Task("task " + i, "description", Taskstatus.NEW));
            taskManager.getTask(i + 1);
        }
        for (int i = 0; i < 5; i++) {
            taskManager.removeTask(i +1);
        }
        assertEquals (10, taskManager.getHistory().size());
    }

    @Test
    void repeatsInHistory() {
        for (int i = 0; i < 20; i++) {
            taskManager.add(new Task("task " + i, "description", Taskstatus.NEW));
            taskManager.getTask(i + 1);
        }
        taskManager.getTask(1);
        taskManager.getTask(10);
        taskManager.getTask(20);

        int dublicate = 0;
        for (int i = 0; i < taskManager.getHistory().size() - 1; i++) {
            for (int j = i + 1; j < taskManager.getHistory().size(); j++) {
                if (i == j + 1)
                    dublicate++;
            }
        }
        assertEquals(0, dublicate);
        assertEquals(20, taskManager.getHistory().size());
    }

    @Test
    void removeEpicWithSubtasks() {
        Task task1 = new Task("test task1","description task1",Taskstatus.NEW);
        Task task2 = new Task("test task1","description task2",Taskstatus.NEW);
        taskManager.add(task1);
        taskManager.add(task2);
        Epic epic1 = new Epic("Test epic1","description epic1", Taskstatus.NEW);
        Epic epic2 = new Epic("Test epic2","description epic2", Taskstatus.DONE);
        taskManager.add(epic1);
        taskManager.add(epic2);
        Subtask subtask1 = new Subtask("test sub1","descroption1",Taskstatus.NEW,epic1.getId());
        Subtask subtask2 = new Subtask("test sub2", "description2", Taskstatus.NEW, epic1.getId());
        Subtask subtask3 = new Subtask("test sub3", "description3", Taskstatus.NEW, epic1.getId());
        taskManager.add(subtask1);
        taskManager.add(subtask2);
        taskManager.add(subtask3);

        taskManager.getTask(task1.getId());
        taskManager.getTask(task2.getId());
        taskManager.getEpic(epic1.getId());
        taskManager.getEpic(epic2.getId());
        taskManager.getSubsOfEpic(subtask1.getId());
        taskManager.getSubsOfEpic(subtask2.getId());
        taskManager.getSubsOfEpic(subtask3.getId());

        taskManager.removeEpic(epic1.getId());

        assertFalse(taskManager.getHistory().contains(epic1));
        assertFalse(taskManager.getHistory().contains(subtask1));
        assertFalse(taskManager.getHistory().contains(subtask2));
        assertFalse(taskManager.getHistory().contains(subtask3));
        assertEquals(3, taskManager.getHistory().size());
    }
}