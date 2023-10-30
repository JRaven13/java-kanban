package Tasks;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static java.time.LocalDateTime.parse;

public class Epic extends Task {

    public Epic(String name, String description, String startTime, Integer duration) {
        super(name, description, startTime, duration);
        this.subtaskIds = new ArrayList<>();
        this.type = TypeTask.EPIC;
        this.startTime = parse(startTime, formatter);
        this.duration = Duration.ofMinutes(duration);
    }

    private List<SubTask> subtaskIds;



    public List<SubTask> getSubTaskIds() {
        return subtaskIds;
    }

    public void setSubtaskIds(ArrayList<SubTask> subtaskIds) {
        this.subtaskIds = subtaskIds;
    }
    public void addSubtask(SubTask subTask){
        subtaskIds.add(subTask);
    }

    @Override
    public String toString() {
        return String.format("Epic %d: %s, %s. (%s) \n " +
                "Начать: %s \n " +
                "Время на выполнение: %s \n " +
                "Закончить: %s",
                id,
                name,
                description,
                status,
                startTime.format(formatter),
                duration.toMinutes(),
                endTime.format(formatter));
    }

}
