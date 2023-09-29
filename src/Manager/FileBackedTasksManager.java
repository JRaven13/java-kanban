package Manager;

import Tasks.Epic;
import Tasks.SubTask;
import Tasks.Task;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import Files.CSVFormatHandler;


public class FileBackedTasksManager extends InMemoryTaskManager {

    private final File file;



    public static void main(String[] args) {
        FileBackedTasksManager fil = new FileBackedTasksManager("TaskFile.csv");
        fil.loadFromFile(fil.getFile());
    /*    Task task1 = fil.createTask(new Task("Задача №1", "Описание задачи №1"));
        Task task2 = fil.createTask(new Task("Задача №2", "Описание задачи №2"));
        Task epic1 = fil.createEpic(new Epic("Эпик №1", "Описание эпика №1"));
        Task subTask1 = fil.createSubTask(new SubTask("SubTask1", "Priverka", 3));
        Task subTask2 = fil.createSubTask(new SubTask("SubTask2", "Priverka2", 3));
        Task epic2 = fil.createEpic(new Epic("Эпик №2", "Описание эпика №2"));
        Task subTask3 = fil.createSubTask(new SubTask("SubTask3", "Priverka3", 6));*/

        System.out.println(fil.getHistory());

        System.out.println(fil.getSubTaskById(5));
        System.out.println(fil.getSubTaskById(4));
        System.out.println(fil.getEpickById(3));
        System.out.println(fil.getEpickById(6));

    }

    private static CSVFormatHandler handler = new CSVFormatHandler();

    public FileBackedTasksManager(String fileName) {
        this.file = new File(fileName);
    }

    public void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file.getName()))){

            writer.write(handler.getHeader());
            writer.newLine();

            for (Task task : taskStorage.values()) {
                writer.write(handler.toString(task));
                writer.newLine();
            }

            for (Epic epic : epicStorage.values()) {
                writer.write(handler.toString(epic));
                writer.newLine();
            }

            for (SubTask subTask : subTaskStorage.values()) {
                writer.write(handler.toString(subTask));
                writer.newLine();
            }

            writer.newLine();
            writer.write(handler.historyToString(historyManager));

        } catch (IOException exception) {
            throw new IllegalArgumentException("Невозможно прочитать файл!");
        }

    }



    public FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager manager = new FileBackedTasksManager(file.getName());
        String content = readFileContents(file.getName());
        String[] lines = content.split("\r?\n");
        if (lines[1] != null) {     // Проверка на пустой файл
            for (int i = 1; i < (lines.length - 2); i++) {
                String[] parts = lines[i].split(",");
                if (parts[1].equals("TASK")) {
                    Task task = handler.taskFromString(lines[i]);
                    taskStorage.put(task.getId(), task);
                }
                if (parts[1].equals("EPIC")) {
                    Epic epic = handler.epicFromString(lines[i]);
                    epicStorage.put(epic.getId(), epic);
                }
                if (parts[1].equals("SUBTASK")) {
                    SubTask subTask = handler.subTaskFromString(lines[i]);
                    int ide = subTask.getEpicId();
                    subTaskStorage.put(subTask.getId(), subTask);
                    if (epicStorage.containsKey(ide)) {
                        Epic epic = epicStorage.get(ide);
                        epic.addSubtask(subTask);
                    } else {
                        System.out.println("Файл повреждён! Не возможно найти Эпик!");
                        break;
                    }
                }
            }
                    String[] ids = lines[lines.length-1].split(",");
                    for (int j = 0; j < ids.length; j++) {
                        if (taskStorage.containsKey(j)) {
                            Task task = taskStorage.get(j);
                            historyManager.addTask(task);
                        }
                        if (epicStorage.containsKey(j)) {
                            Task task = epicStorage.get(j);
                            historyManager.addTask(task);
                        }
                        if (subTaskStorage.containsKey(j)) {
                            Task task = subTaskStorage.get(j);
                            historyManager.addTask(task);
                        }
                    }


            } else {
            System.out.println("Файл пуст!");
            return manager;
        }
        return manager;
    }

    public String readFileContents(String file) {
        try {
            return Files.readString(Path.of(file));
        } catch (IOException e) {
            System.out.println("Невозможно прочитать файл с месячным отчётом. Возможно файл не находится в нужной директории.");
            return null;
        }
    }

    @Override
    public Task createTask(Task task) {
        Task wan = super.createTask(task);
        save();
        return wan;
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> task = super.getAllTasks();
        save();
        return task;
    }

    @Override
    public void updateTask(Task task) {
        save();
        super.updateTask(task);
    }

    @Override
    public void deleteAllTask() {
        save();
        super.deleteAllTask();
    }

    @Override
    public void deleteTask(int id) {
        save();
        super.deleteTask(id);
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public void taskStatus(int id) {
        save();
        super.taskStatus(id);
    }

    @Override
    public Epic createEpic(Epic epic) {
        Epic wan = super.createEpic(epic);
        save();
        return wan;
    }

    @Override
    public void updateEpic(Epic epic) {
        save();
        super.updateEpic(epic);
    }

    @Override
    public List<Epic> getAllEpic() {
        List<Epic> epic = super.getAllEpic();
        save();
        return epic;
    }

    @Override
    public void deleteAllEpic() {
        save();
        super.deleteAllEpic();
    }

    @Override
    public void deleteEpic(int id) {
        save();
        super.deleteEpic(id);
    }

    @Override
    public Task getEpickById(int id) {
        Task task = super.getEpickById(id);
        save();
        return task;
    }

    @Override
    public void checkEpicStatus(int epicId) {
        save();
        super.checkEpicStatus(epicId);
    }

    @Override
    public SubTask createSubTask(SubTask subTask) {
        SubTask wan = super.createSubTask(subTask);
        save();
        return wan;
    }

    @Override
    public void subTaskStatus(int id) {
        save();
        super.subTaskStatus(id);
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        save();
        super.updateSubTask(subTask);
    }

    @Override
    public List<SubTask> getSubTaskForEpic(int id) {
        List<SubTask> subTasks = super.getSubTaskForEpic(id);
        save();
        return subTasks;
    }

    @Override
    public List<SubTask> getAllSubTask() {
        List<SubTask> subTasks = super.getAllSubTask();
        save();
        return subTasks;
    }

    @Override
    public void deleteSubTask(int id) {
        save();
        super.deleteSubTask(id);
    }

    @Override
    public void deleteAllSubTask() {
        save();
        super.deleteAllSubTask();
    }

    @Override
    public Task getSubTaskById(int id) {
        Task task = super.getSubTaskById(id);
        save();
        return task;
    }

    @Override
    public List<Task> getHistory() {
        List<Task> task = super.getHistory();
        save();
        return task;
    }

    public File getFile() {
        return file;
    }
}

