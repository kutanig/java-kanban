package Manager;

import Tasc.Task;


import java.util.ArrayList;
import java.util.List;

public interface HistoryManager {

    void add(Task task);

    List<Task> getHistory();

    void remove(int id);

    ArrayList<Object> phm1();
}
