package Manager;

import Tasks.Epic;
import Tasks.SubTask;
import Tasks.Task;
import Tasks.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {

    private int generatedId = 0;

    protected final HashMap<Integer, Task> taskStorage = new HashMap<>();
    protected final HashMap<Integer, Epic> epicStorage = new HashMap<>();
    protected final HashMap<Integer, SubTask> subTaskStorage = new HashMap<>();

    protected final HistoryManager historyManager = Managers.getHistoryDefault();

    // Методы для задач

    @Override
    public Task createTask(Task task) {
        int id = generateId();
        task.setId(id);
        taskStorage.put(id, task);
        return task;
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        return new ArrayList(taskStorage.values());
    }

    @Override
    public void updateTask(Task task) {
        Task saved = taskStorage.get(task.getId());
        if (saved == null) {
            return;
        }
        taskStorage.put(task.getId(), task);
    }

    @Override
    public void deleteAllTask() {
        for (Task task : taskStorage.values()) {
            deleteTask(task.getId());
        }
    }

    @Override
    public void deleteTask(int id) {
        historyManager.remove(id);
        taskStorage.remove(id);
    }

    @Override
    public Task getTaskById(int id) {
        historyManager.addTask(taskStorage.get(id));
        return taskStorage.get(id);
    }

    @Override
    public void taskStatus(int id) {
        Task change = taskStorage.get(id);
        if (change.getStatus() == TaskStatus.NEW) {
            change.setStatus(TaskStatus.IN_PROGRESS);
            updateTask(change);
        }
        if (change.getStatus() == TaskStatus.IN_PROGRESS) {
            change.setStatus(TaskStatus.DONE);
            updateTask(change);
        }
    }

    // Методы для Эпиков

    @Override
    public Epic createEpic(Epic epic) {
        int id = generateId();
        epic.setId(id);
        epicStorage.put(id, epic);
        return epic;
    }

    @Override
    public void updateEpic(Epic epic) {
        Epic saved = epicStorage.get(epic.getId());
        if (saved == null) {
            return;
        }
        epicStorage.put(epic.getId(), epic);
    }

    @Override
    public List<Epic> getAllEpic() {
        return new ArrayList(epicStorage.values());
    }

    public void deleteAllEpic() {
        for (Epic epic : epicStorage.values()) {
            deleteEpic(epic.getId());
        }
    }

    @Override
    public void deleteEpic(int id) {
        ArrayList<Integer> subTaskRemove = new ArrayList<>();
        for (SubTask value : subTaskStorage.values()) {
            if (value.getEpicId() == id) {
                subTaskRemove.add(value.getId());
            }
        }
        for (Integer i : subTaskRemove) {
            deleteSubTask(i);
        }
        historyManager.remove(id);
        epicStorage.remove(id);
    }

    @Override
    public Task getEpickById(int id) {
        historyManager.addTask(epicStorage.get(id));
        return epicStorage.get(id);
    }

    @Override
    public void checkEpicStatus(int epicId) {
        Epic epic = epicStorage.get(epicId);
        List<SubTask> subs = epic.getSubtaskIds();
        if (subs.isEmpty()) {
            epic.setStatus(TaskStatus.NEW);
            return;
        }
        TaskStatus status = null;
        for (int i = 0; i < subs.size(); i++) {
            int j = subs.get(i).getId();
            final SubTask subTaskStatus = subTaskStorage.get(j);
            if (status == null) {
                status = subTaskStatus.getStatus();
                continue;
            }
            if (status.equals(subTaskStatus.getStatus())
                    && !status.equals(TaskStatus.IN_PROGRESS)) {
                continue;
            }
            epic.setStatus(TaskStatus.IN_PROGRESS);
            return;
        }
        epic.setStatus(status);
    }

    // Методы для подзадач

    @Override
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

    @Override
    public void subTaskStatus(int id) {
        SubTask change = subTaskStorage.get(id);
        if (change.getStatus() == TaskStatus.NEW) {
            change.setStatus(TaskStatus.IN_PROGRESS);
            updateSubTask(change);
            checkEpicStatus(change.getEpicId());
            return;
        }
        if (change.getStatus() == TaskStatus.IN_PROGRESS) {
            change.setStatus(TaskStatus.DONE);
            updateSubTask(change);
            checkEpicStatus(change.getEpicId());
        }
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        SubTask saved = subTaskStorage.get(subTask.getId());
        if (saved == null) {
            return;
        }
        subTaskStorage.put(subTask.getId(), subTask);
        checkEpicStatus(saved.getEpicId());
    }

    @Override
    public List<SubTask> getSubTaskForEpic(int id) {
        return epicStorage.get(id).getSubtaskIds();
    }

    @Override
    public List<SubTask> getAllSubTask() {
        return new ArrayList(subTaskStorage.values());
    }

    @Override
    public void deleteSubTask(int id) {
        historyManager.remove(id);
        SubTask subTask = subTaskStorage.get(id);
        Epic epic = epicStorage.get(subTask.getEpicId());
        epic.getSubtaskIds().remove(subTask);
        updateEpic(epicStorage.get(subTask.getEpicId()));
        subTaskStorage.remove(id);
        checkEpicStatus(subTask.getEpicId());
    }

    @Override
    public void deleteAllSubTask() {
        for (int i = 0; i < epicStorage.size(); i++) {
            Epic epic = epicStorage.get(i);
            epic.getSubtaskIds().clear();
            checkEpicStatus(epic.getId());
        }
        for (SubTask subTask : subTaskStorage.values()) {
            historyManager.remove(subTask.getId());
            deleteSubTask(subTask.getId());
        }
    }

    @Override
    public Task getSubTaskById(int id) {
        historyManager.addTask(subTaskStorage.get(id));
        return subTaskStorage.get(id);
    }

    // Общие методы
    @Override
    public int generateId() {
        return ++generatedId;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

}
