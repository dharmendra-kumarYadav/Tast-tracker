import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TaskRepository {
    private final Path tasksFile = Paths.get("tasks.json");

    public List<Task> load() throws IOException {
        if (!Files.exists(tasksFile)) {
            Files.writeString(tasksFile, "[]", StandardCharsets.UTF_8);
            return new ArrayList<>();
        }
        String content = Files.readString(tasksFile, StandardCharsets.UTF_8).trim();
        if (content.isEmpty()) return new ArrayList<>();
        return parseTasks(content);
    }

    public void save(List<Task> tasks) throws IOException {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (int i = 0; i < tasks.size(); i++) {
            builder.append(tasks.get(i).toJson());
            if (i < tasks.size() - 1) builder.append(",\n");
        }
        builder.append("]\n");
        Files.writeString(tasksFile, builder.toString(), StandardCharsets.UTF_8);
    }

    private List<Task> parseTasks(String json) {
        List<Task> tasks = new ArrayList<>();
        String trimmed = json.trim();
        if (trimmed.length() < 2 || trimmed.charAt(0) != '[' || trimmed.charAt(trimmed.length() - 1) != ']') {
            throw new IllegalArgumentException("Invalid JSON file format.");
        }
        String body = trimmed.substring(1, trimmed.length() - 1).trim();
        if (body.isEmpty()) return tasks;
        int depth = 0;
        StringBuilder objectBuilder = new StringBuilder();
        for (int i = 0; i < body.length(); i++) {
            char c = body.charAt(i);
            if (c == '{') depth++;
            if (depth > 0) objectBuilder.append(c);
            if (c == '}') {
                depth--;
                if (depth == 0) {
                    tasks.add(parseTaskObject(objectBuilder.toString()));
                    objectBuilder.setLength(0);
                }
            }
        }
        return tasks;
    }

    private Task parseTaskObject(String objectJson) {
        int id = parseIntField(objectJson, "id");
        String description = parseStringField(objectJson, "description");
        String status = parseStringField(objectJson, "status");
        String createdAt = parseStringField(objectJson, "createdAt");
        String updatedAt = parseStringField(objectJson, "updatedAt");
        return new Task(id, description, status, createdAt, updatedAt);
    }

    private int parseIntField(String json, String fieldName) {
        Pattern pattern = Pattern.compile("\"" + Pattern.quote(fieldName) + "\"\\s*:\\s*(\\d+)");
        Matcher matcher = pattern.matcher(json);
        if (!matcher.find()) throw new IllegalArgumentException("Missing field: " + fieldName);
        return Integer.parseInt(matcher.group(1));
    }

    private String parseStringField(String json, String fieldName) {
        Pattern pattern = Pattern.compile("\"" + Pattern.quote(fieldName) + "\"\\s*:\\s*\"(.*?)\"");
        Matcher matcher = pattern.matcher(json);
        if (!matcher.find()) throw new IllegalArgumentException("Missing field: " + fieldName);
        return unescapeJsonString(matcher.group(1));
    }

    private String unescapeJsonString(String value) {
        return value.replace("\\\"", "\"").replace("\\\\", "\\");
    }
}
