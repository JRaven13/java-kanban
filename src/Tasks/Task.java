package Tasks;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static java.time.LocalDateTime.parse;

public class Task {

    protected Integer id;
    protected String name;
    protected String description;
    protected TaskStatus status;
    protected TypeTask type;
    protected LocalDateTime startTime;
    protected LocalDateTime endTime;
    protected Duration duration;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");

    public Task(String name, String description, String startTime, Integer duration) {
        this.name = name;
        this.description = description;
        this.status = TaskStatus.NEW;
        this.type = TypeTask.TASK;
        this.startTime = parse(startTime, formatter);
        this.duration = Duration.ofMinutes(duration);
    }

    public void getEndTime() {
        this.endTime = startTime.plusMinutes(duration.toMinutes());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format("Task %d: %s, %s. (%s) \n " +
                "Начать: %s \n " +
                "Время на выполнение: %s \n " +
                "Закончить: %s", id,
                name,
                description,
                status,
                startTime.format(formatter),
                duration.toMinutes(),
                endTime.format(formatter));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id)
                && Objects.equals(name, task.name)
                && Objects.equals(description, task.description)
                && Objects.equals(status, task.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, status);
    }

    public TypeTask getType() {
        return type;
    }
}