import Tasks.Epic;
import Tasks.SubTask;
import Tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
// метод для изменения статуса Задачи, так как у нас конкретные статусы есть,
// я решил просто преключение последовательным сделать
// если не так сделал, распишите как лучше сделать =)
// например через if с передачей номера статуса, типа 1-NEW 2-INPROGRESS 3-DONE?
    public void taskStatus(int id) {
        Task change = taskStorage.get(id);
        if (change.getStatus() == "NEW") {
            change.setStatus("IN_PROGRESS");
            updateTask(change);
        }
        if (change.getStatus() == "IN_PROGRESS") {
            change.setStatus("DONE");
            updateTask(change);
        }
    }
    
    // Методы для Эпиков

    public Epic createEpic(Epic epic) {
        int id = generateId();
        epic.setId(id);
        epicStorage.put(id, epic);
        return epic;
    }

    public void updateEpic(Epic epic) {
        Epic saved = epicStorage.get(epic.getId());
        if (saved == null) {
            return;
        }
        epicStorage.put(epic.getId(), epic);
    }

    public ArrayList<Epic> getAllEpic() {
        return new ArrayList(epicStorage.values());
    }

       public void deleteAllEpic() {
        subTaskStorage.clear();
        epicStorage.clear();
    }

    public void deleteEpic(int id) {
        Epic epic = epicStorage.get(id);
        for (SubTask value : subTaskStorage.values()) {
            if (value.getEpicId() == id) {
                subTaskStorage.remove(value.getId());
            }
        }
        epicStorage.remove(id);
    }

    public Task getEpickById(int id) {
        return epicStorage.get(id);
    }

    private Epic checkEpicStatus(int id) { //честно не знаю как подругому сделать, так работает
        Epic epic = epicStorage.get(id);
        List<SubTask> list = epic.getSubtaskIds();
        if (epic.getStatus() == "NEW") {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getStatus() != "NEW") {
                    epic.setStatus("IN_PROGRESS");
                    updateEpic(epic);
                }
            }
        } else if (epic.getStatus() == "IN_PROGRESS") {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getStatus() != "DONE") {
                    epic.setStatus("IN_PROGRESS");
                    updateEpic(epic);
                    return epic;
                }
                epic.setStatus("DONE");
                updateEpic(epic);
            }
        }
        return epic;
    }

    // Методы для подзадач

    public SubTask createSubTask(SubTask subTask) {
        int id = generateId();
        subTask.setId(id);
        if (epicStorage.get(subTask.getEpicId()) == null) {
            return null;
        } else {
            subTaskStorage.put(id, subTask);
            epicStorage.get(subTask.getEpicId()).addSubtask(subTask);
            checkEpicStatus(subTask.getEpicId());
            return subTask;
        }
    }

    public void subTaskStatus(int id) {
        SubTask change = subTaskStorage.get(id);
        if (change.getStatus() == "NEW") {
            change.setStatus("IN_PROGRESS");
            updateSubTask(change);
            checkEpicStatus(change.getEpicId());
            return;
        }
        if (change.getStatus() == "IN_PROGRESS") {
            change.setStatus("DONE");
            updateSubTask(change);
            checkEpicStatus(change.getEpicId());
        }
    }

    public void updateSubTask(SubTask subTask) {
        SubTask saved = subTaskStorage.get(subTask.getId());
        if (saved == null) {
            return;
        }
        subTaskStorage.put(subTask.getId(), subTask);
        checkEpicStatus(saved.getEpicId());
    }

    public List<SubTask> getSubTaskForEpic(int id) {
        return epicStorage.get(id).getSubtaskIds();
    }

    public ArrayList<SubTask> getAllSubTask() {
        return new ArrayList(subTaskStorage.values());
    }

    public void deleteSubTask(int id) {
        SubTask subTask = subTaskStorage.get(id);
        Epic epic = epicStorage.get(subTask.getEpicId());
        epic.getSubtaskIds().remove(subTask);
        updateEpic(epicStorage.get(subTask.getEpicId()));
        subTaskStorage.remove(id);
        checkEpicStatus(subTask.getEpicId());
    }

    public void deleteAllSubTask() {
        for (int i = 0; i < epicStorage.size(); i++) {
            Epic epic = epicStorage.get(i);
            epic.getSubtaskIds().clear();
            checkEpicStatus(epic.getId());
        }
        subTaskStorage.clear();
    }

    public Task getSubTaskById(int id) {
        return subTaskStorage.get(id);
    }

    // Общий метод на id

    private int generateId() {
        return ++generatedId;
    }

}
