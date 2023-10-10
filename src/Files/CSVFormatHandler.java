package Files;

import Manager.HistoryManager;
import Tasks.*;

import java.util.ArrayList;
import java.util.List;

public class CSVFormatHandler {

    private static final String DELIMETR = ",";

    public String toString(Task task) {
        String result = task.getId() + DELIMETR +
                task.getType() + DELIMETR +
                task.getName() + DELIMETR +
                task.getStatus() + DELIMETR +
                task.getDescription() + DELIMETR;
        if (task.getType() == TypeTask.SUBTASK) {
            result = result + ((SubTask) task).getEpicId();
        }
        return result;
    }

    public Task taskFromString(String value) {
        String[] parts = value.split(",");
        String id = String.valueOf(Integer.parseInt(parts[0]));
        String name = parts[2];
        String status = parts[3];
        String description = parts[4];

    /*    Task task = new Task(name, description);
        task.setId(Integer.parseInt(id));
        task.setDescription(description);
        task.setStatus(TaskStatus.valueOf(status));
        return task;*/
        return null;
    }

    public Epic epicFromString(String value) {
        String[] parts = value.split(",");
        String id = String.valueOf(Integer.parseInt(parts[0]));
        String name = parts[2];
        String status = parts[3];
        String description = parts[4];

    /*    Epic epic = new Epic(name, description);
        epic.setId(Integer.parseInt(id));
        epic.setDescription(description);
        epic.setStatus(TaskStatus.valueOf(status));
        return epic;*/
        return null;
    }

    public SubTask subTaskFromString(String value) {
        String[] parts = value.split(",");
        String id = String.valueOf(Integer.parseInt(parts[0]));
        String name = parts[2];
        String status = parts[3];
        String description = parts[4];
        String epicId = parts[5];

   /*     SubTask subTask = new SubTask(name, description, Integer.parseInt(epicId));
        subTask.setId(Integer.parseInt(id));
        subTask.setDescription(description);
        subTask.setStatus(TaskStatus.valueOf(status));
        return subTask;*/
        return null;
    }

    public String historyToString(HistoryManager manager) {
        List<String> result = new ArrayList<>();

        for (Task task : manager.getHistory()) {
            result.add(String.valueOf(task.getId()));
        }
        return String.join(DELIMETR, result);
    }

    public List<Integer> historyFromString(String value) {
        return null;
    }

    public String getHeader() {
        return "id,type,name,status,description,epic";
    }
}
