package Test;
import Manager.Managers;
import Manager.TaskManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    @Test
    public void getDefaultInitializedTaskManagers() {
        TaskManager taskManager1 = Managers.getDefault();
        TaskManager taskManager2 = Managers.getDefault();
        assertNotNull(taskManager1);
        assertNotNull(taskManager2);
        assertNotEquals(taskManager1, taskManager2);
    }
}