package manager;

import task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final HashMap<Integer, Node> tasksHistory = new HashMap<>();
    private Node head;
    private Node tail;

    private static class Node {
        private final Task task;
        private Node next;
        private Node prev;

        public Node(Node prev, Task task, Node next) {
            this.task = task;
            this.next = next;
            this.prev = prev;
        }
    }

    private Node linkLast(Task task) {
        Node oldTail = tail;
        Node newNode = new Node(oldTail, task, null);
        tail = newNode;
        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.next = newNode;
        }
        return newNode;
    }

    private void removeNode(int id) {
        if (tasksHistory.get(id) != null) {
            if (tasksHistory.get(id) == head) {
                head = head.next;
                head.prev = null;
            } else if (tasksHistory.get(id) == tail) {
                tail = tail.prev;
                tail.next = null;
            } else if (tasksHistory.get(id).next != null && tasksHistory.get(id).prev != null) {
                tasksHistory.get(id).prev.next = tasksHistory.get(id).next;
                tasksHistory.get(id).next.prev = tasksHistory.get(id).prev;
            }
        }
    }

    @Override
    public void add(Task task) {
        if (tasksHistory.containsKey(task.getId())) {
            remove(task.getId());
            tasksHistory.put(task.getId(), linkLast(task));
        } else {
            tasksHistory.put(task.getId(), linkLast(task));
        }
    }

    @Override
    public List<Task> getHistory() {
        List<Task> list = new ArrayList<>();
        Node node = head;
        for (int i = 0; i < tasksHistory.size(); i++) {
            list.add(node.task);
            node = node.next;
        }
        return list;
    }

    @Override
    public void remove(int id) {
        removeNode(id);
        tasksHistory.remove(id);

    }

    @Override
    public ArrayList<Object> phm1() {
        return new ArrayList<>(tasksHistory.values());
    }
}
