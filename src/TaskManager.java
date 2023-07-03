import Tasks.Epic;
import Tasks.SubTask;
import Tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {

    private int generatedId = 0;

    private HashMap<Integer, Task> taskStorage = new HashMap<>();
    private HashMap<Integer, Epic> epicStorage = new HashMap<>();
    private HashMap<Integer, SubTask> subTaskStorage = new HashMap<>();
    
    // Методы для задач

    public Task createTask(Task task) {
        int id = generateId();
        task.setId(id);
        taskStorage.put(id, task);
        return task;
    }

    public ArrayList<Task> getAllTasks() {
        return new ArrayList(taskStorage.values());
    }

    public void updateTask(Task task) {
        Task saved = taskStorage.get(task.getId());
        if (saved == null) {
            return;
        }
        taskStorage.put(task.getId(), task);
    }

    public void deleteAllTask() {
        taskStorage.clear();
    }

    public void deleteTask(int delete) {
        taskStorage.remove(delete);
    }

    public Task getTaskById(int id) {
        return taskStorage.get(id);
    }
    
    // Методы для Эпиков

    public Epic createEpic(Epic epic) {
        int id = generateId();
        epic.setId(id);
        epicStorage.put(id, epic);
        return epic;
    }

    public ArrayList<Epic> getAllEpic() {
        return new ArrayList(epicStorage.values());
    }
/*
    public void updateTask(Task task) {
        Task saved = taskStorage.get(task.getId());
        if (saved == null) {
            return;
        }
        taskStorage.put(task.getId(), task);
    }
*/
    public void deleteAllEpic() {
        epicStorage.clear();
    }

    public void deleteEpic(int delete) {
        epicStorage.remove(delete);
    }

    public Task getEpickById(int id) {
        return epicStorage.get(id);
    }
    
    // Методы для подзадач

    public SubTask createSubTask(SubTask subTask) {
        int id = generateId();
        subTask.setId(id);
        Epic epic = epicStorage.get(subTask.getEpicId());
        if (epic == null) {
            return subTask;
        }
        ArrayList<SubTask> subtaskIds = epic.getSubtaskIds();
        if (subtaskIds == null) {

            subTaskStorage.put(id, subTask);
            subtaskIds.add(subTask);
            epic.setSubtaskIds(subtaskIds);
            return subTask;
        }
        subTaskStorage.put(id, subTask);
        subtaskIds.add(subTask);
        return subTask;
    }

    public ArrayList<SubTask> getSubTaskForEpic(int id) {
        Epic epic = epicStorage.get(id);
        return epic.getSubtaskIds();
    }

    private int generateId() {
        return ++generatedId;
    }

}
