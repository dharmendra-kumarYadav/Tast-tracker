import java.time.format.DateTimeFormatter;

public class Task {
    private final int id;
    private String description;
    private String status;
    private final String createdAt;
    private String updatedAt;

    public Task(int id, String description, String status, String createdAt, String updatedAt) {
        this.id = id;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getId() { return id; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getCreatedAt() { return createdAt; }
    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }

    public String toJson() {
        return "{\n"
                + "  \"id\": " + id + ",\n"
                + "  \"description\": \"" + escapeJsonString(description) + "\",\n"
                + "  \"status\": \"" + escapeJsonString(status) + "\",\n"
                + "  \"createdAt\": \"" + escapeJsonString(createdAt) + "\",\n"
                + "  \"updatedAt\": \"" + escapeJsonString(updatedAt) + "\"\n"
                + "}";
    }

    private String escapeJsonString(String value) {
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
