package Manager;

public final class Managers {

    private Managers() {}

    public static InMemoryTaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getHistoryDefault() {
        return new InMemoryHistoryManager();
    }
}
