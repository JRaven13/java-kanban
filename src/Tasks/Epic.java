package Tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    public Epic(String name, String description, String startTime, Integer duration) {
        super(name, description, startTime, duration);
        this.subtaskIds = new ArrayList<>();
        this.type = TypeTask.EPIC;
        getDurationSubTask();
    }

    private List<SubTask> subtaskIds;

    @Override
    public void getEndTime() {
        LocalDateTime end = this.endTime;
        for (SubTask dur : subtaskIds){
            end = dur.endTime;
            if (end.isAfter(dur.endTime)) {
                end = dur.endTime;
            }
        }
        this.endTime = end;
    }

    public void getStartTime() {
        LocalDateTime start = this.startTime;
        for (SubTask dur : subtaskIds){
            start = dur.startTime;
            if (start.isBefore(dur.startTime)) {
                start = dur.startTime;
            }
        }
        this.startTime = start;
    }

    public void getDurationSubTask() {
        for (SubTask dur : subtaskIds){
            this.duration = this.duration.plusMinutes(dur.duration.toMinutes());
        }
    }

    public List<SubTask> getSubtaskIds() {
        return subtaskIds;
    }

    public void setSubtaskIds(ArrayList<SubTask> subtaskIds) {
        this.subtaskIds = subtaskIds;
    }
    public void addSubtask(SubTask subTask){
        this.subtaskIds.add(subTask);
        getStartTime();
        getEndTime();
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
