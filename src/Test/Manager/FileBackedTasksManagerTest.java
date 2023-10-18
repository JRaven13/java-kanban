package Manager;
import Tasks.Epic;
import Tasks.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    File file = new File("test_file.csv");

    @Override
    public FileBackedTasksManager createManager() {
        manager = Managers.BackedTasksManager(this.file);
        return manager;
    }
    @BeforeEach
    public void createFile(){
        try{
            Files.createFile(Path.of("test_file.csv"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @AfterEach
    public void afterEach() {
        try {
            Files.delete(Path.of("test_file.csv"));
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }

    @Test
    public void saveAndLoadTest() {
        Task task = new Task("Task 1","Descr", "12:00 12.11.2022",20);
        manager.createTask(task);
        Epic epic = new Epic("Epic 1","Descr", "12:00 12.11.2022",20);
        manager.createEpic(epic);
        FileBackedTasksManager fileManager = manager.loadFromFile(this.file);
        assertEquals(manager.getAllTasks(), fileManager.getAllTasks());
        assertEquals(manager.getAllEpic(), fileManager.getAllEpic());
    }

    @Test
    public void saveAndLoadEmptyTasksEpicsSubtasksTest() {
        FileBackedTasksManager fileManager = new FileBackedTasksManager(file);
        FileBackedTasksManager newManager = fileManager.loadFromFile(file);
        assertNull(newManager.getAllTasks());
        assertNull(newManager.getAllEpic());
        assertNull(newManager.getAllSubTask());
    }

    @Test
    public void saveAndLoadEmptyHistoryTest() {
        FileBackedTasksManager fileManager = new FileBackedTasksManager(file);
        fileManager.loadFromFile(file);
        Assertions.assertEquals(Collections.EMPTY_LIST, manager.getHistory());
    }
}