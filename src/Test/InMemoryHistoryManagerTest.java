package Test;

import Manager.HistoryManager;
import Manager.Managers;
import Manager.TaskManager;
import Tasc.Epic;
import Tasc.Task;
import Tasc.Taskstatus;
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
    void removeFirstTaskWhenAddNewTaskAndListAreFull() {
        for (int i = 0; i < 15; i++) {
            historyManager.add(new Task("task " + i, "description", Taskstatus.NEW));
        }
        Task testTask = new Task("test task1", "description1", Taskstatus.NEW);
        historyManager.add(testTask);
        assertEquals(10, historyManager.getHistory().size());
        assertEquals(testTask, historyManager.getHistory().getLast());
    }
}