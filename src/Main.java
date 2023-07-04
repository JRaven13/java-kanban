import Tasks.Epic;
import Tasks.SubTask;
import Tasks.Task;

import java.sql.SQLOutput;

public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = new TaskManager();

        Task task1 = new Task("Задача №1", "Описание задачи №1");
        Task task2 = new Task("Задача №2", "Описание задачи №2");

        Epic epic1 = new Epic("Эпик №1", "Описание эпика №1");
        SubTask subTask1 = new SubTask("SubTask1", "Priverka", 3);
        SubTask subTask2 = new SubTask("SubTask2", "Priverka2", 3);

        Epic epic2 = new Epic("Эпик №2", "Описание эпика №2");
        SubTask subTask3 = new SubTask("SubTask3", "Priverka3", 6);

        task1 = taskManager.createTask(task1);
        task2 = taskManager.createTask(task2);
        epic1 = taskManager.createEpic(epic1);
        subTask1 = taskManager.createSubTask(subTask1);
        subTask2 = taskManager.createSubTask(subTask2);
        epic2 = taskManager.createEpic(epic2);
        subTask3 = taskManager.createSubTask(subTask3);

        System.out.println(taskManager.getTaskById(task1.getId()));
        System.out.println(taskManager.getTaskById(task2.getId()));
        taskManager.taskStatus(1);
        System.out.println(taskManager.getTaskById(task1.getId()));

        System.out.println(taskManager.getAllTasks());

        System.out.println(epic1);
        System.out.println(taskManager.getSubTaskForEpic(3));
        taskManager.subTaskStatus(4);
        System.out.println(epic1);
        taskManager.subTaskStatus(4);
        taskManager.subTaskStatus(5);
        taskManager.subTaskStatus(5);
        System.out.println(epic1);
        System.out.println(taskManager.getSubTaskForEpic(6));

        System.out.println(epic2);


    }
}
