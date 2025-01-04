package Test;

import Task.Task;
import Task.Taskstatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


class TaskTest {
    @Test
    void shouldTrueIfIdsAreEquals(){
        Task task1 = new Task(1,"test task1", "description1", Taskstatus.NEW);
        Task task2 = new Task(1,"test task2", "description2", Taskstatus.NEW);
        assertEquals(task2, task1);
    }
}