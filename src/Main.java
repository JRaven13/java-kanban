import Manager.Managers;
import Manager.TaskManager;
import Tasks.Epic;
import Tasks.SubTask;
import Tasks.Task;

public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = Managers.getDefault();




        Task task1 = taskManager.createTask(new Task("Задача №1", "Описание задачи №1", "15:00 10.10.2023", 30));
        Task task2 = taskManager.createTask(new Task("Задача №2", "Описание задачи №2", "15:30 10.10.2023", 45));
        Task epic1 = taskManager.createEpic(new Epic("Эпик №1", "Описание эпика №1","17:00 10.10.2023", 60));
        Task subTask1 = taskManager.createSubTask(new SubTask("SubTask1", "Priverka","17:00 10.10.2023", 90, 3));
        Task subTask2 = taskManager.createSubTask(new SubTask("SubTask2", "Priverka2","16:00 10.10.2023", 60, 3));
        Task epic2 = taskManager.createEpic(new Epic("Эпик №2", "Описание эпика №2","17:00 10.10.2023", 60));
        Task subTask3 = taskManager.createSubTask(new SubTask("SubTask3", "Priverka3","18:30 10.10.2023", 30, 6));

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

        System.out.println(taskManager.getSubTaskById(5));
        System.out.println(taskManager.getSubTaskById(4));
        System.out.println(taskManager.getEpickById(3));
        System.out.println(taskManager.getEpickById(6));

        System.out.println("Пробую список получить");

        System.out.println(taskManager.getHistory());
/*
        taskManager.deleteTask(2);
        taskManager.deleteTask(1);
        taskManager.deleteEpic(3);*/

        System.out.println(taskManager.getHistory());


    }


}
