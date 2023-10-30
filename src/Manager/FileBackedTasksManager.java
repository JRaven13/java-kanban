package Manager;

import Files.CSVFormatHandler;
import Tasks.Epic;
import Tasks.SubTask;
import Tasks.Task;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


public class FileBackedTasksManager extends InMemoryTaskManager {

    private final File file;



    public static void main(String[] args) {
//        FileBackedTasksManager fil = new FileBackedTasksManager();
//
//     /*   Task task1 = fil.createTask(new Task("Задача №1", "Описание задачи №1", "15:00 10.10.2023", 30));
//        Task task2 = fil.createTask(new Task("Задача №2", "Описание задачи №2", "15:30 10.10.2023", 45));
//        Task task3 = fil.createTask(new Task("Задача №2", "Описание задачи №2", "17:30 10.10.2023", 45));
//        Task task4 = fil.createTask(new Task("Задача №2", "Описание задачи №2", "19:30 10.10.2023", 45));
//
//        fil.getTaskById(1);
//        fil.getTaskById(3);*/
//
//             FileBackedTasksManager files = fil.loadFromFile(file);
//            System.out.println(files.getAllTasks());
//        Task task5 = files.createTask(new Task("Задача №5", "Описание задачи №2", "19:30 10.10.2023", 45));
    }

    private static CSVFormatHandler handler = new CSVFormatHandler();

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    public FileBackedTasksManager() {
        this.file = new File("file.csv");
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
            if (handler.historyToString(getHistoryManager()).isEmpty()){
                writer.newLine();
            } else
                writer.write(handler.historyToString(getHistoryManager()));

        } catch (IOException exception) {
            throw new IllegalArgumentException("Невозможно прочитать файл!");
        }

    }

    public FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager manager = new FileBackedTasksManager(file);
        ArrayList<String> lines = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String content = reader.readLine();
            while (reader.ready()) {
                lines.add(content);
                content = reader.readLine();
            }
            reader.close();
            lines.add(content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            int maxId = 0;
            for (int i = 1; i < (lines.size() - 2); i++) {
                String[] parts = lines.get(i).split(",");
                if (parts[1].equals("TASK")) {
                    Task task = handler.taskFromString(lines.get(i));
                    manager.taskStorage.put(task.getId(), task);
                    if (maxId < task.getId()) {
                        maxId = task.getId();
                    }
                }
                if (parts[1].equals("EPIC")) {
                    Epic epic = handler.epicFromString(lines.get(i));
                    manager.epicStorage.put(epic.getId(), epic);
                    if (maxId < epic.getId()) {
                        maxId = epic.getId();
                    }
                }
                if (parts[1].equals("SUBTASK")) {
                    SubTask subTask = handler.subTaskFromString(lines.get(i));
                    int ide = subTask.getEpicId();
                    manager.subTaskStorage.put(subTask.getId(), subTask);
                    if (manager.epicStorage.containsKey(ide)) {
                        Epic epic = manager.epicStorage.get(ide);
                        epic.addSubtask(subTask);
                        if (maxId < epic.getId()) {
                            maxId = epic.getId();
                        }
                    } else {
                        System.out.println("Файл повреждён! Не возможно найти Эпик!");
                        break;
                    }
                }
            }
            manager.setGeneratedId(maxId);
            int lineWithHistory = lines.size() - 1;
            String[] ids = lines.get(lineWithHistory).split(",");
            boolean checkHistory = lines.get(lineWithHistory).isEmpty();
            if (!checkHistory) {
                for (String id : ids) {
                    if (manager.taskStorage.containsKey(Integer.parseInt(id))) {
                        manager.getHistoryManager().addTask(manager.taskStorage.get(Integer.parseInt(id)));
                    }
                    if (manager.epicStorage.containsKey(Integer.parseInt(id))) {
                        manager.getHistoryManager().addTask(manager.epicStorage.get(Integer.parseInt(id)));
                    }
                    if (manager.subTaskStorage.containsKey(Integer.parseInt(id))) {
                        manager.getHistoryManager().addTask(manager.subTaskStorage.get(Integer.parseInt(id)));
                    }
                }
            }
            return manager;
        } catch (IndexOutOfBoundsException | NullPointerException e) {
            return manager;
        }
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

