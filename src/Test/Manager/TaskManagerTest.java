package Manager;

import Tasks.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {
    protected T manager;

    public abstract T createManager() throws IOException, InterruptedException;

    @BeforeEach
    void getManager() throws IOException, InterruptedException {
        manager = createManager();
    }

    protected Task addTask() {
        return new Task("Name", "Description", "13:00 12.03.2023", 45);
    }

    protected Epic addEpic() {
        return new Epic("Name", "Description", "13:00 12.03.2023", 45);
    }

    protected SubTask addSubTask(Epic epic) {
        return new SubTask("Name", "Description", "13:00 12.03.2023", 45, epic.getId());
    }

    @Test
    void createNewTask() {
        Task task = addTask();
        manager.createTask(task);
        assertNotNull(task.getStatus());
        assertEquals(TaskStatus.NEW, task.getStatus());
    }
    @Test
    void createNullTask() {
        assertNull(manager.createTask(null));
    }

    @Test
    void getAllTasks() {
        Task task = addTask();
        manager.createTask(task);
        Task task2 = addTask();
        manager.createTask(task2);
        ArrayList<Task> tasks = new ArrayList<>(List.of(task, task2));
        assertEquals(tasks, manager.getAllTasks());
    }

    @Test
    void getAllTasksNull() {
        assertNull(manager.getAllTasks());
    }

    @Test
    void deleteAllTask() {
        Task task = addTask();
        manager.createTask(task);
        Task task2 = addTask();
        manager.createTask(task2);
        manager.deleteAllTask();
        assertNull(manager.getAllTasks());
    }

    @Test
    void deleteTask() {
        Task task = addTask();
        manager.createTask(task);
        Task task2 = addTask();
        manager.createTask(task2);
        manager.deleteTask(2);
        ArrayList<Task> tasks = new ArrayList<>(List.of(task));
        assertEquals(tasks, manager.getAllTasks());
    }

    @Test
    void getTaskById() {
        Task task = addTask();
        manager.createTask(task);
        assertEquals(task, manager.getTaskById(1));
    }

    @Test
    void getTaskByIdNull() {
        assertNull(manager.getTaskById(1));
    }

    @Test
    void getTaskByIdMinusOne() {
        Task task = addTask();
        manager.createTask(task);
        assertNull(manager.getTaskById(-1));
    }

    @Test
    void taskStatusNewInProgressDone() {
        Task task = addTask();
        manager.createTask(task);
        assertEquals(TaskStatus.NEW, manager.getTaskById(1).getStatus());
        task.setStatus(TaskStatus.IN_PROGRESS);
        assertEquals(TaskStatus.IN_PROGRESS, manager.getTaskById(1).getStatus());
        task.setStatus(TaskStatus.DONE);
        assertEquals(TaskStatus.DONE, manager.getTaskById(1).getStatus());
    }

    @Test
    void createNewEpic() {
        Epic epic = addEpic();
        manager.createEpic(epic);
        assertNotNull(epic.getStatus());
        assertEquals(TaskStatus.NEW, epic.getStatus());
        assertEquals(Collections.EMPTY_LIST, epic.getSubTaskIds());
    }

    @Test
    void createNullEpic() {
        assertNull(manager.createEpic(null));
    }

    @Test
    void getAllEpics() {
        Epic epic = addEpic();
        manager.createEpic(epic);
        Epic epic2 = addEpic();
        manager.createEpic(epic2);
        ArrayList<Task> epics = new ArrayList<>(List.of(epic, epic2));
        assertEquals(epics, manager.getAllEpic());
    }

    @Test
    void getAllEpicsNull() {
        assertNull(manager.getAllEpic());
    }

    @Test
    void deleteAllEpic() {
        Epic epic1 = addEpic();
        manager.createEpic(epic1);
        Epic epic2 = addEpic();
        manager.createEpic(epic2);
        manager.deleteAllEpic();
        assertNull(manager.getAllEpic());
    }

    @Test
    void deleteEpic() {
        Epic epic1 = addEpic();
        manager.createEpic(epic1);
        Epic epic2 = addEpic();
        manager.createEpic(epic2);
        manager.deleteEpic(2);
        ArrayList<Epic> tasks = new ArrayList<>(List.of(epic1));
        assertEquals(tasks, manager.getAllEpic());
    }

    @Test
    void getEpicById() {
        Epic epic = addEpic();
        manager.createEpic(epic);
        assertEquals(epic, manager.getEpickById(1));
    }

    @Test
    void getEpicByIdNull() {
        assertNull(manager.getEpickById(1));
    }

    @Test
    void getEpicByIdMinusOne() {
        Epic epic = addEpic();
        manager.createEpic(epic);
        assertNull(manager.getEpickById(-1));
    }

    @Test
    void checkEpicStatusNew() {
        Epic epic = addEpic();
        manager.createEpic(epic);
        SubTask subtask1 = addSubTask(epic);
        SubTask subtask2 = addSubTask(epic);
        manager.createSubTask(subtask1);
        manager.createSubTask(subtask2);
        assertEquals(TaskStatus.NEW, manager.getEpickById(1).getStatus());
    }

    @Test
    void checkEpicStatusDone() {
        Epic epic = addEpic();
        manager.createEpic(epic);
        SubTask subtask1 = addSubTask(epic);
        SubTask subtask2 = addSubTask(epic);
        manager.createSubTask(subtask1);
        manager.createSubTask(subtask2);
        manager.subTaskStatus(2);
        manager.subTaskStatus(2);
        manager.subTaskStatus(3);
        manager.subTaskStatus(3);
        assertEquals(TaskStatus.DONE, manager.getEpickById(1).getStatus());
    }

    @Test
    void checkEpicStatusNewAndDone() {
        Epic epic = addEpic();
        manager.createEpic(epic);
        SubTask subtask1 = addSubTask(epic);
        SubTask subtask2 = addSubTask(epic);
        manager.createSubTask(subtask1);
        manager.createSubTask(subtask2);
        manager.subTaskStatus(2);
        manager.subTaskStatus(2);
        assertEquals(TaskStatus.IN_PROGRESS, manager.getEpickById(1).getStatus());
    }

    @Test
    void checkEpicStatusInProgress() {
        Epic epic = addEpic();
        manager.createEpic(epic);
        SubTask subtask1 = addSubTask(epic);
        SubTask subtask2 = addSubTask(epic);
        manager.createSubTask(subtask1);
        manager.createSubTask(subtask2);
        manager.subTaskStatus(2);
        manager.subTaskStatus(3);
        assertEquals(TaskStatus.IN_PROGRESS, manager.getEpickById(1).getStatus());
    }

    @Test
    void checkEpicStatusNullSubTask() {
        Epic epic = addEpic();
        manager.createEpic(epic);
        assertEquals(TaskStatus.NEW, manager.getEpickById(1).getStatus());
    }

    @Test
    void createSubTask() {
        Epic epic = addEpic();
        manager.createEpic(epic);
        SubTask subTask = addSubTask(epic);
        manager.createSubTask(subTask);
        List<SubTask> SubTasks = manager.getSubTaskForEpic(epic.getId());
        assertNotNull(subTask.getStatus());
        assertEquals(epic.getId(), subTask.getEpicId());
        assertEquals(TaskStatus.NEW, subTask.getStatus());
        assertEquals(SubTasks, epic.getSubTaskIds());
    }

    @Test
    void createSubTaskNull() {
        assertNull(manager.createSubTask(null));
    }

    @Test
    void subTaskStatusNewInProgressDone() {
        Epic epic = addEpic();
        manager.createEpic(epic);
        SubTask subTask = addSubTask(epic);
        manager.createSubTask(subTask);
        assertEquals(TaskStatus.NEW, subTask.getStatus());
        subTask.setStatus(TaskStatus.IN_PROGRESS);
        assertEquals(TaskStatus.IN_PROGRESS, subTask.getStatus());
        subTask.setStatus(TaskStatus.DONE);
        assertEquals(TaskStatus.DONE, subTask.getStatus());
    }

    @Test
    void getSubTaskForEpic() {
        Epic epic = addEpic();
        manager.createEpic(epic);
        SubTask subTask = addSubTask(epic);
        manager.createSubTask(subTask);
        SubTask subTask2 = addSubTask(epic);
        manager.createSubTask(subTask2);
        ArrayList<SubTask> subTasks = new ArrayList<>(List.of(subTask, subTask2));
        assertEquals(subTasks, manager.getSubTaskForEpic(epic.getId()));
    }

    @Test
    void getSubTaskForEpicNull() {
        Epic epic = addEpic();
        manager.createEpic(epic);
        assertEquals(Collections.EMPTY_LIST, manager.getSubTaskForEpic(epic.getId()));
    }

    @Test
    void getSubTaskForEpicMinusOne() {
        Epic epic = addEpic();
        manager.createEpic(epic);
        assertNull(manager.getSubTaskForEpic(-1));
    }

    @Test
    void getAllSubTask() {
        Epic epic = addEpic();
        manager.createEpic(epic);
        SubTask subTask = addSubTask(epic);
        manager.createSubTask(subTask);
        SubTask subTask2 = addSubTask(epic);
        manager.createSubTask(subTask2);
        ArrayList<SubTask> subTasks = new ArrayList<>(List.of(subTask, subTask2));
        assertEquals(subTasks, manager.getAllSubTask());
    }

    @Test
    void getAllSubTaskNull() {
        assertNull(manager.getAllEpic());
    }

    @Test
    void deleteSubTask() {
        Epic epic = addEpic();
        manager.createEpic(epic);
        SubTask subTask = addSubTask(epic);
        manager.createSubTask(subTask);
        SubTask subTask2 = addSubTask(epic);
        manager.createSubTask(subTask2);
        manager.deleteSubTask(subTask.getId());
        ArrayList<SubTask> subTasks = new ArrayList<>(List.of(subTask2));
        assertEquals(subTasks, epic.getSubTaskIds());
    }

    @Test
    void deleteAllSubTask() {
        Epic epic = addEpic();
        manager.createEpic(epic);
        SubTask subTask = addSubTask(epic);
        manager.createSubTask(subTask);
        SubTask subTask2 = addSubTask(epic);
        manager.createSubTask(subTask2);
        manager.deleteAllSubTask();
        assertEquals(Collections.EMPTY_LIST, epic.getSubTaskIds());
    }

    @Test
    void getSubTaskByIdNull() {
        assertNull(manager.getSubTaskById(1));
    }

    @Test
    void getSubTaskById() {
        Epic epic = addEpic();
        manager.createEpic(epic);
        SubTask subtask = addSubTask(epic);
        manager.createSubTask(subtask);
        assertEquals(subtask, manager.getSubTaskById(2));
    }

    @Test
    void getSubTaskByIdMinusOne() {
        Epic epic = addEpic();
        manager.createEpic(epic);
        SubTask subtask = addSubTask(epic);
        manager.createSubTask(subtask);
        assertNull(manager.getTaskById(-1));
    }

    @Test
    void getHistory() {
        Epic epic = addEpic();
        manager.createEpic(epic);
        SubTask SubTask = addSubTask(epic);
        manager.createSubTask(SubTask);
        manager.getEpickById(epic.getId());
        manager.getSubTaskById(SubTask.getId());
        List<Task> list = manager.getHistory();
        assertEquals(2, list.size());
        assertTrue(list.contains(SubTask));
        assertTrue(list.contains(epic));
    }

    @Test
    void getHistoryNull(){
        assertEquals(Collections.EMPTY_LIST, manager.getHistory());
    }

    @Test
    void getHistoryNegative(){
        manager.getTaskById(-1);
        manager.getSubTaskById(-1);
        manager.getEpickById(-1);
        assertTrue(manager.getHistory().isEmpty());
    }

}