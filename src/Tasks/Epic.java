package Tasks;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    public Epic(String name, String description) {
        super(name, description);
        this.subtaskIds = new ArrayList<>();
        this.type = TypeTask.EPIC;
    }

    private List<SubTask> subtaskIds;

    public List<SubTask> getSubtaskIds() {
        return subtaskIds;
    }

    public void setSubtaskIds(ArrayList<SubTask> subtaskIds) {
        this.subtaskIds = subtaskIds;
    }
    public void addSubtask(SubTask subTask){
        this.subtaskIds.add(subTask);
    }

}
