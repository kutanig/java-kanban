import manager.*;
import task.Epic;
import task.Subtask;
import task.Task;
import task.Taskstatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

 class InMemoryTaskManagerTest {
    TaskManager taskManager;
    Task task1;
    Task task2;
    Epic epic1;
    Epic epic2;
    Epic epic3;


    @BeforeEach
    void setUp() {
        taskManager = Managers.getDefault();
        task1 = new Task("test task1", "description task1", Taskstatus.NEW);
        task2 = new Task("test task1", "description task2", Taskstatus.NEW);
        epic1 = new Epic("Test epic1", "description epic1", Taskstatus.NEW);
        epic2 = new Epic("Test epic2", "description epic2", Taskstatus.DONE);
        epic3 = new Epic("Test epic3", "description epic3", Taskstatus.IN_PROGRESS);
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
        taskManager.removeTask(task1.getId());
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
        Subtask subtask1 = new Subtask("test sub1", "description1", Taskstatus.NEW, epic1.getId());
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
        Subtask subtask1 = new Subtask("test sub1", "description1", Taskstatus.NEW, epic1.getId());
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
        taskManager.add(epic2);
        Subtask subtask1 = new Subtask("test sub1", "description1", Taskstatus.NEW, epic1.getId());
        Subtask subtask2 = new Subtask("test sub2", "description2", Taskstatus.NEW, epic1.getId());
        Subtask subtask3 = new Subtask("test sub3", "description3", Taskstatus.DONE, epic2.getId());
        Subtask subtask4 = new Subtask("test sub4", "description4", Taskstatus.DONE, epic2.getId());
        taskManager.add(subtask1);
        taskManager.add(subtask2);
        taskManager.add(subtask3);
        taskManager.add(subtask4);
        subtask1.setStatus(Taskstatus.DONE);
        subtask4.setStatus(Taskstatus.NEW);
        taskManager.update(subtask1);
        taskManager.update(subtask4);
        assertEquals(Taskstatus.IN_PROGRESS, epic1.getStatus());
        assertEquals(Taskstatus.IN_PROGRESS, epic2.getStatus());
    }

    @Test
    void addTheSubtaskShouldChangeTheStatusOfEpic() {
        taskManager.add(epic2);
        Subtask subtask1 = new Subtask("test sub1", "description1", Taskstatus.DONE, epic2.getId());
        Subtask subtask2 = new Subtask("test sub2", "description2", Taskstatus.DONE, epic2.getId());
        Subtask subtask3 = new Subtask("test sub3", "description3", Taskstatus.NEW, epic2.getId());
        taskManager.add(subtask1);
        taskManager.add(subtask2);
        taskManager.add(subtask3);
        assertEquals(Taskstatus.IN_PROGRESS, epic2.getStatus());
    }

    @Test
    void removeTheSubtaskShouldChangeTheStatusOfEpic() {
        taskManager.add(epic3);
        Subtask subtask1 = new Subtask("test sub1", "description1", Taskstatus.DONE, epic3.getId());
        Subtask subtask2 = new Subtask("test sub2", "description2", Taskstatus.DONE, epic3.getId());
        Subtask subtask3 = new Subtask("test sub3", "description3", Taskstatus.NEW, epic3.getId());
        taskManager.add(subtask1);
        taskManager.add(subtask2);
        taskManager.add(subtask3);
        taskManager.removeSubtask(subtask3.getId());
        assertEquals(Taskstatus.DONE, epic3.getStatus());
    }

    @Test
    void removeTurnTheSubtaskShouldChangeTheStatusOfEpic() {
        taskManager.add(epic2);
        Subtask subtask1 = new Subtask("test sub1", "description1", Taskstatus.DONE, epic2.getId());
        Subtask subtask2 = new Subtask("test sub2", "description2", Taskstatus.IN_PROGRESS, epic2.getId());
        Subtask subtask3 = new Subtask("test sub3", "description3", Taskstatus.NEW, epic2.getId());
        taskManager.add(subtask1);
        taskManager.add(subtask2);
        taskManager.add(subtask3);
        taskManager.removeSubtask(subtask1.getId());
        taskManager.removeSubtask(subtask2.getId());
        taskManager.removeSubtask(subtask3.getId());
        assertEquals(Taskstatus.NEW, epic2.getStatus());
    }

    @Test
    void removeAllTheSubtaskShouldChangeTheStatusOfEpic() {
        taskManager.add(epic2);
        taskManager.add(epic3);
        Subtask subtask1 = new Subtask("test sub1", "description1", Taskstatus.DONE, epic2.getId());
        Subtask subtask2 = new Subtask("test sub2", "description2", Taskstatus.IN_PROGRESS, epic2.getId());
        Subtask subtask3 = new Subtask("test sub3", "description3", Taskstatus.IN_PROGRESS, epic3.getId());
        taskManager.add(subtask1);
        taskManager.add(subtask2);
        taskManager.add(subtask3);
        taskManager.clearSubtasks();
        assertEquals(Taskstatus.NEW, epic2.getStatus());
        assertEquals(Taskstatus.NEW, epic3.getStatus());
    }

    @Test
    void taskIntersections() {
        Task t1 = new Task("t1","description1",Taskstatus.NEW,LocalDateTime.of(2025,2,2,1,1),Duration.ofDays(1));
        taskManager.add(t1);
        Task t2 = new Task("task2","description2",Taskstatus.NEW,LocalDateTime.of(2025,2,1,1,2),Duration.ofDays(1));
        taskManager.add(t2);
        Task t3 = new Task("task3","description3",Taskstatus.NEW,LocalDateTime.of(2025,2,3,1,1),Duration.ofDays(1));
        taskManager.add(t3);
        Task t4 = new Task("task4","description4",Taskstatus.NEW,LocalDateTime.of(2025,1,1,1,1),Duration.ofDays(25));
        taskManager.add(t4);
        Task t5 = new Task("task5","description5",Taskstatus.NEW,LocalDateTime.of(2025,2,2,2,2),Duration.ofDays(1));
        taskManager.add(t5);
        Task t6 = new Task("task6","description6",Taskstatus.NEW,LocalDateTime.of(2025,2,2,2,2),Duration.ofMinutes(1));
        taskManager.add(t6);
        Task t7 = new Task("t7","description7",Taskstatus.NEW,LocalDateTime.of(2025,2,3,1,2),Duration.ofDays(1));
        taskManager.add(t7);
        assertEquals(3, taskManager.getPrioritizedTasks().size());
        assertTrue(taskManager.getPrioritizedTasks().contains(t1));
        assertTrue(taskManager.getPrioritizedTasks().contains(t7));
        assertTrue(taskManager.getPrioritizedTasks().contains(t4));
    }
}