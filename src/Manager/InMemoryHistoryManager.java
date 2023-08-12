package Manager;

import Tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    Node first;
    Node last;

    Map<Integer, Node> map = new HashMap<>();

    @Override
    public List<Task> getHistory() {
        List<Task> result = new ArrayList<>();
        if (first == null) {
            System.out.println("Список пуст!");
            return result;
        }
        Node current = first;

        while (current != null) {
            result.add(current.getValue());
            current = current.next;
        }
        return result;
    }

    @Override
    public void addTask(Task task) {
        if (task == null) {
            return;
        }
        Node node = new Node(task);

        if (last != null) {
            last.next = node;
            node.prev = last;
            last = node;
            map.put(map.size()+1, node);
        } else {
            first = node;
            last = node;
            map.put(1, node);
        }
    }

    @Override
    public void remove(int id) {
        Node current = map.get(id);
        map.remove(id);

        if (current == last && current == first) {
            last = null;
            first = null;
        } else if (current == last) {
            current.prev.next = null;
            last = current.prev;
            current.next = null;
            current.prev = null;
        } else if (current == first) {
            current.next.prev = null;
            first = current.next;
            current.next = null;
            current.prev = null;
        } else {
            current.prev.next = current.next;
            current.next.prev = current.prev;
        }
    }


    public static class Node {

        Task value;
        Node next;
        Node prev;

        public Node(Task value) {
            this.value = value;
        }

        public Task getValue() {
            return value;
        }

        public Node getNext() {
            return next;
        }

        public void setNext(Node next) {
            this.next = next;
        }

        public Node getPrev() {
            return prev;
        }

        public void setPrev(Node prev) {
            this.prev = prev;
        }
    }
}
