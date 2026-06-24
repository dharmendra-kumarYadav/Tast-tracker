import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TaskService {
    private final TaskRepository repository;
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    public int addTask(String description) throws IOException {
        List<Task> tasks = repository.load();
        int nextId = tasks.stream().mapToInt(Task::getId).max().orElse(0) + 1;
        String now = currentTimestamp();
        Task task = new Task(nextId, description, "todo", now, now);
        tasks.add(task);
        repository.save(tasks);
        return nextId;
    }

    public void updateTask(int id, String description) throws IOException {
        List<Task> tasks = repository.load();
        Task task = findTask(tasks, id);
        task.setDescription(description);
        task.setUpdatedAt(currentTimestamp());
        repository.save(tasks);
    }

    public void deleteTask(int id) throws IOException {
        List<Task> tasks = repository.load();
        boolean removed = tasks.removeIf(t -> t.getId() == id);
        if (!removed) throw new IllegalArgumentException("id " + id + " not exist.");
        repository.save(tasks);
    }

    public void markTask(int id, String status) throws IOException {
        List<Task> tasks = repository.load();
        Task task = findTask(tasks, id);
        task.setStatus(status);
        task.setUpdatedAt(currentTimestamp());
        repository.save(tasks);
    }

    public List<Task> listTasks(Optional<String> statusFilter) throws IOException {
        List<Task> tasks = repository.load();
        if (statusFilter.isEmpty()) return tasks;
        String filter = statusFilter.get();
        List<Task> filtered = new ArrayList<>();
        for (Task task : tasks) if (task.getStatus().equals(filter)) filtered.add(task);
        return filtered;
    }

    private Task findTask(List<Task> tasks, int id) {
        return tasks.stream().filter(t -> t.getId() == id).findFirst().orElseThrow(() -> new IllegalArgumentException("id " + id + " not exist."));
    }

    private String currentTimestamp() {
        return LocalDateTime.now().format(TIMESTAMP_FORMAT);
    }
}
