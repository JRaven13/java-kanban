package Tasks;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubTask extends Task {

    private int epicId;



    public SubTask(String name, String description, String startTime, Integer duration, int epicId) {
        super(name, description, startTime, duration);
        this.epicId = epicId;
        this.type = TypeTask.SUBTASK;
        this.startTime = LocalDateTime.parse(startTime, formatter);
        this.duration = Duration.ofMinutes(duration);
        this.endTime = endTimeForTask();
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return String.format("SubTask %d(%d): %s, %s. (%s) \n " +
                        "Начать: %s \n " +
                        "Время на выполнение: %s \n " +
                        "Закончить: %s",
                id,
                epicId,
                name,
                description,
                status,
                startTime.format(formatter),
                duration.toMinutes(),
                endTime.format(formatter));
    }
}
