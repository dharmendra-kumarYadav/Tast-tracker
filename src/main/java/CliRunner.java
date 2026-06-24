import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class CliRunner {
    private final TaskService service;

    public CliRunner(TaskService service) {
        this.service = service;
    }

    public void run(String[] args) {
        if (args.length == 0) {
            printUsage();
            return;
        }
        String command = args[0].trim().toLowerCase();
        try {
            switch (command) {
                case "add" -> handleAdd(args);
                case "update" -> handleUpdate(args);
                case "delete" -> handleDelete(args);
                case "mark-in-progress" -> handleMark(args, "in-progress");
                case "mark-done" -> handleMark(args, "done");
                case "list" -> handleList(args);
                default -> {
                    System.out.println("Unknown command: " + command);
                    printUsage();
                }
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("I/O error: " + e.getMessage());
        }
    }

    private void handleAdd(String[] args) throws IOException {
        if (args.length < 2) throw new IllegalArgumentException("The add command requires a description.");
        String description = joinArguments(args, 1);
        int id = service.addTask(description);
        System.out.println("Task added successfully (ID: " + id + ")");
    }

    private void handleUpdate(String[] args) throws IOException {
        if (args.length < 3) throw new IllegalArgumentException("The update command requires an ID and a new description.");
        int id = parseId(args[1]);
        String description = joinArguments(args, 2);
        service.updateTask(id, description);
        System.out.println("Task updated successfully.");
    }

    private void handleDelete(String[] args) throws IOException {
        if (args.length != 2) throw new IllegalArgumentException("The delete command requires an ID.");
        int id = parseId(args[1]);
        service.deleteTask(id);
        System.out.println("Task deleted successfully.");
    }

    private void handleMark(String[] args, String status) throws IOException {
        if (args.length != 2) throw new IllegalArgumentException("The " + args[0] + " command requires an ID.");
        int id = parseId(args[1]);
        service.markTask(id, status);
        System.out.println("Task marked as " + status + " successfully.");
    }

    private void handleList(String[] args) throws IOException {
        Optional<String> filter = Optional.empty();
        if (args.length > 1) filter = Optional.of(args[1].trim().toLowerCase());
        if (filter.isPresent()) {
            String f = filter.get();
            if (!f.equals("todo") && !f.equals("done") && !f.equals("in-progress"))
                throw new IllegalArgumentException("Unsupported status for list: " + f + ". Use todo, in-progress, or done.");
        }
        List<Task> tasks = service.listTasks(filter);
        printTasks(tasks);
    }

    private void printTasks(List<Task> tasks) {
        if (tasks.isEmpty()) { System.out.println("No tasks found."); return; }
        for (Task task : tasks) {
            System.out.println("ID: " + task.getId());
            System.out.println("Description: " + task.getDescription());
            System.out.println("Status: " + task.getStatus());
            System.out.println("Created At: " + task.getCreatedAt());
            System.out.println("Updated At: " + task.getUpdatedAt());
            System.out.println("----------------------------------------");
        }
    }

    private int parseId(String value) {
        try { return Integer.parseInt(value); } catch (NumberFormatException e) { throw new IllegalArgumentException("Invalid task ID: " + value); }
    }

    private String joinArguments(String[] args, int startIndex) {
        StringBuilder builder = new StringBuilder();
        for (int i = startIndex; i < args.length; i++) { if (i > startIndex) builder.append(' '); builder.append(args[i]); }
        return builder.toString();
    }

    private void printUsage() {
        System.out.println("TaskTracker CLI commands:");
        System.out.println("  add \"description\"");
        System.out.println("  update <id> \"new description\"");
        System.out.println("  delete <id>");
        System.out.println("  mark-in-progress <id>");
        System.out.println("  mark-done <id>");
        System.out.println("  list");
        System.out.println("  list todo");
        System.out.println("  list in-progress");
        System.out.println("  list done");
    }
}
