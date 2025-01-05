package Test;

import task.Epic;
import task.Taskstatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    Epic epic1;
    Epic epic2;

    @BeforeEach
    void setUp() {
        epic1 = new Epic(1,"Test epic1","description epic1", Taskstatus.NEW);
        epic2 = new Epic(1,"Test epic2","description epic2", Taskstatus.NEW);
    }
    @Test
    void shouldTrueIfIdsAreEquals() {
        assertEquals(epic1, epic2);
    }


    @Test
    void epicCannotBeAddedToItselfAsASubtask () {
        int subtasksSize = epic1.getSubIds().size();
        epic1.addSubtask(1);
        assertEquals(subtasksSize, epic1.getSubIds().size());
    }

}