package Tasks;

import java.util.ArrayList;

public class Epic extends Task {

    private static ArrayList<SubTask> subtaskIds;

    public static ArrayList<SubTask> getSubtaskIds() {
        return subtaskIds;
    }

    public static void setSubtaskIds(ArrayList<SubTask> subtaskIds) {
        Epic.subtaskIds = subtaskIds;
    }

    public Epic(String name, String description) {
        super(name, description);
    }
}
