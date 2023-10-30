package Manager;

import HTTP.KVTaskClient;
import Tasks.Epic;
import Tasks.SubTask;
import Tasks.Task;
import com.google.gson.*;

import java.util.List;
import java.util.stream.Collectors;

public class HttpTaskManager extends FileBackedTasksManager {
    private final KVTaskClient taskClient;
    private static final Gson gson = new Gson();

    public HttpTaskManager(KVTaskClient taskClient) {
        this.taskClient = taskClient;
    }

    @Override
    public void save() {
        taskClient.put("task", gson.toJson(taskStorage.values()));
        taskClient.put("subtask", gson.toJson(subTaskStorage.values()));
        taskClient.put("epic", gson.toJson(epicStorage.values()));
        taskClient.put("tasks", gson.toJson(getPrioritet()));
        List<Integer> historyIds = getHistory()
                .stream()
                .map(Task::getId)
                .collect(Collectors.toList());
        taskClient.put("history", gson.toJson(historyIds));
    }

    public void loadFromServer() {
        loadTasks("task");
        loadTasks("subtask");
        loadTasks("epic");
        loadHistory();
    }

    private void loadTasks(String key) {
        JsonElement jsonElement = JsonParser.parseString(taskClient.load(key));
        JsonArray jsonTasksArray = jsonElement.getAsJsonArray();
        for (JsonElement element : jsonTasksArray) {
            Task task;
            Epic epic;
            SubTask subtask;
            switch (key) {
                case "task":
                    task = gson.fromJson(element.getAsJsonObject(), Task.class);
                    taskStorage.put(task.getId(), task);
                    prioritet.add(task);
                    break;
                case "subtask":
                    subtask = gson.fromJson(element.getAsJsonObject(), SubTask.class);
                    subTaskStorage.put(subtask.getId(), subtask);
                    prioritet.add(subtask);
                    break;
                case "epic":
                    epic = gson.fromJson(element.getAsJsonObject(), Epic.class);
                    epicStorage.put(epic.getId(), epic);
                    prioritet.add(epic);
                    break;
                default:
                    System.out.println("Не удается загрузить задачи");
                    return;
            }
        }
    }

    private void loadHistory() {
        JsonElement jsonElement = JsonParser.parseString(taskClient.load("history"));
        JsonArray jsonHistoryArray = jsonElement.getAsJsonArray();
        for (JsonElement element : jsonHistoryArray) {
            int id = element.getAsInt();
            if (taskStorage.containsKey(id)) {
                historyManager.addTask(taskStorage.get(id));
            } else if (epicStorage.containsKey(id)) {
                historyManager.addTask(epicStorage.get(id));
            } else if (subTaskStorage.containsKey(id)) {
                historyManager.addTask(subTaskStorage.get(id));
            }
        }
    }
}