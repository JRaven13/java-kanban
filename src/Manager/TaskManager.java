package Manager;

import Tasks.Epic;
import Tasks.SubTask;
import Tasks.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    // Методы для задач

    Task createTask(Task task);

    List<Task> getAllTasks();

    void updateTask(Task task);

    void deleteAllTask();

    void deleteTask(int delete);

    Task getTaskById(int id);

    void taskStatus(int id);

    // Методы для Эпиков

    Epic createEpic(Epic epic);

    void updateEpic(Epic epic);

    List<Epic> getAllEpic();

    void deleteAllEpic();

    void deleteEpic(int id);

    Task getEpickById(int id);

    void checkEpicStatus(int epicId);

    // Методы для подзадач

    SubTask createSubTask(SubTask subTask);

    void subTaskStatus(int id);

    void updateSubTask(SubTask subTask);

    List<SubTask> getSubTaskForEpic(int id);

    List<SubTask> getAllSubTask();

    void deleteSubTask(int id);

    void deleteAllSubTask();

    Task getSubTaskById(int id);

    // Общие методы

    int generateId();

    List<Task> getHistory();

}
