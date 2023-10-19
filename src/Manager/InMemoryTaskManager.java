package Manager;

import Tasks.Epic;
import Tasks.SubTask;
import Tasks.Task;
import Tasks.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    private int generatedId = 0;

    protected final HashMap<Integer, Task> taskStorage;
    protected final HashMap<Integer, Epic> epicStorage;
    protected final HashMap<Integer, SubTask> subTaskStorage;

    protected final HistoryManager historyManager;


    public InMemoryTaskManager() {
        this.taskStorage = new HashMap<>();
        this.subTaskStorage = new HashMap<>();
        this.epicStorage = new HashMap<>();
        this.historyManager = Managers.getHistoryDefault();

    }

    public HistoryManager getHistoryManager() {
        return historyManager;
    }

// Методы для задач

    @Override
    public Task createTask(Task task) {
        if (!interSection(task)) {
            if (task != null && !this.taskStorage.containsKey(task.getId())) {
                task.setId(generateId());
                this.taskStorage.put(task.getId(), task);
            } else {
                return null;
            }
        }
        return task;
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        if (taskStorage.isEmpty()) {
            return null;
        }
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
        taskStorage.forEach(((integer, task) -> historyManager.remove(task.getId())));
        this.taskStorage.clear();
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
        if (epic != null && !this.epicStorage.containsKey(epic.getId())) {
            epic.setId(generateId());
            this.epicStorage.put(epic.getId(), epic);
        } else {
            return null;
        }
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
        if (epicStorage.isEmpty()) {
            return null;
        }
        return new ArrayList(epicStorage.values());
    }

    public void deleteAllEpic() {
        epicStorage.forEach((integer, epic) -> historyManager.remove(epic.getId()));
        for (Map.Entry<Integer, Epic> entry : epicStorage.entrySet()) {
            List<SubTask> subtasks = entry.getValue().getSubTaskIds();
            for (SubTask subTask : subtasks) {
                historyManager.remove(subTask.getId());
            }
        }
        this.epicStorage.clear();
        this.subTaskStorage.clear();
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
        List<SubTask> subs = epic.getSubTaskIds();
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

    public void setEpicTime(int id){
        Epic epic = epicStorage.get(id);
        List<SubTask> subtasksIds = epic.getSubTaskIds();
        LocalDateTime startTime = subtasksIds.get(0).getStartTime();
        LocalDateTime endTime = subtasksIds.get(0).getEndTime();
        for (SubTask subTask : subtasksIds) {
            if (subTask.getStartTime().isBefore(startTime))
                startTime = subTask.getStartTime();

            if (subTask.getEndTime().isAfter(endTime))
                endTime = subTask.getEndTime();
        }
        epic.setStartTime(startTime);
        epic.setEndTime(endTime);
        epic.setDuration(Duration.between(startTime,endTime));
    }

    // Методы для подзадач

    @Override
    public SubTask createSubTask(SubTask subTask) {
        if (!interSection(subTask)) {
            if (subTask != null && !this.subTaskStorage.containsKey(subTask.getId())) {
                subTask.setId(generateId());
                this.subTaskStorage.put(subTask.getId(), subTask);

                Epic epic = epicStorage.get(subTask.getEpicId());
                if (epic != null) {
                    this.epicStorage.get(subTask.getEpicId()).addSubtask(subTask);
                    checkEpicStatus(subTask.getEpicId());
                    setEpicTime(subTask.getEpicId());
                }
            } else {
                return null;
            }
        }
        return subTask;
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
        setEpicTime(subTask.getEpicId());
    }

    @Override
    public List<SubTask> getSubTaskForEpic(int id) {
        if (epicStorage.get(id) == null){
            return null;
        }
        return epicStorage.get(id).getSubTaskIds();
    }

    @Override
    public List<SubTask> getAllSubTask() {
        if (subTaskStorage.isEmpty()) {
            return null;
        }
        return new ArrayList(subTaskStorage.values());
    }

    @Override
    public void deleteSubTask(int id) {
        historyManager.remove(id);
        SubTask subTask = subTaskStorage.get(id);
        Epic epic = epicStorage.get(subTask.getEpicId());
        epic.getSubTaskIds().remove(subTask);
        updateEpic(epicStorage.get(subTask.getEpicId()));
        subTaskStorage.remove(id);
        checkEpicStatus(subTask.getEpicId());
        setEpicTime(subTask.getEpicId());
    }

    @Override
    public void deleteAllSubTask() {
        subTaskStorage.forEach((integer, subtask) -> historyManager.remove(subtask.getId()));
        this.subTaskStorage.clear();

        for (Epic epic : epicStorage.values()) {
            epic.setStatus(TaskStatus.NEW);
        }
        for (Epic epic : epicStorage.values()) {
            epic.getSubTaskIds().clear();
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

    public void setGeneratedId(int generatedId) {
        this.generatedId = generatedId;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public boolean interSection(Task task) {
        List<Task> prioritet = getPrioritizedTasks();
        boolean isInterSection = false;
        for (Task data : prioritet) {
            if (task.getStartTime().isBefore(data.getStartTime()) && task.getStartTime().isAfter(data.getEndTime())) {
                isInterSection = false;
            }
            else {
                isInterSection = true;
            }
        }
        return isInterSection;
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        List<Task> allTasks = new ArrayList<>();
        allTasks.addAll(this.taskStorage.values());
        allTasks.addAll(this.subTaskStorage.values());
        allTasks.sort(Comparator.comparing(Task::getStartTime));
        return allTasks;
    }

}


