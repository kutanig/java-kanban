package task;

import java.time.Duration;
import java.time.LocalDateTime;

public class TaskParser {

    public static Task fromStringTask(String[] value) {
        if (value[5].equals("null") && value[6].equals("null")) {
            return new Task(Integer.parseInt(value[0]), value[2], value[4], Taskstatus.valueOf(value[3]));
        } else if (value[5].equals("null")) {
            return new Task(Integer.parseInt(value[0]), value[2], value[4], Taskstatus.valueOf(value[3]), Duration.parse(value[6]));
        } else if (value[6].equals("null")) {
            return new Task(Integer.parseInt(value[0]), value[2], value[4], Taskstatus.valueOf(value[3]), LocalDateTime.parse(value[5]));
        }
        return new Task(Integer.parseInt(value[0]), value[2], value[4], Taskstatus.valueOf(value[3]), LocalDateTime.parse(value[5]), Duration.parse(value[6]));
    }

    public static Subtask fromStringSubtask(String[] value) {
        if (value[5].equals("null") && value[6].equals("null")) {
            return new Subtask(Integer.parseInt(value[0]), value[2], value[4], Taskstatus.valueOf(value[3]), Integer.parseInt(value[7]));
        } else if (value[5].equals("null")) {
            return new Subtask(Integer.parseInt(value[0]), value[2], value[4], Taskstatus.valueOf(value[3]), Duration.parse(value[6]), Integer.parseInt(value[7]));
        } else if (value[6].equals("null")) {
            return new Subtask(Integer.parseInt(value[0]), value[2], value[4], Taskstatus.valueOf(value[3]), LocalDateTime.parse(value[5]), Integer.parseInt(value[7]));
        }
        return new Subtask(Integer.parseInt(value[0]), value[2], value[4], Taskstatus.valueOf(value[3]), LocalDateTime.parse(value[5]), Duration.parse(value[6]), Integer.parseInt(value[7]));
    }

    public static Epic fromStringEpic(String[] value) {
        if (value[5].equals("null") && value[6].equals("null") && value[7].equals("null")) {
            return new Epic(Integer.parseInt(value[0]), value[2], value[4], Taskstatus.valueOf(value[3]));
        } else if (value[5].equals("null") && value[7].equals("null")) {
            return new Epic(Integer.parseInt(value[0]), value[2], value[4], Taskstatus.valueOf(value[3]), Duration.parse(value[6]));
        }
        return new Epic(Integer.parseInt(value[0]), value[2], value[4], Taskstatus.valueOf(value[3]), LocalDateTime.parse(value[5]), Duration.parse(value[6]), LocalDateTime.parse(value[7]));
    }
}
