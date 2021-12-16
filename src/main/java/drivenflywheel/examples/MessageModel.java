package drivenflywheel.examples;

public class MessageModel {
    private String basePath;
    private String fileName;
    private String message;

    public MessageModel() {
    }

    public MessageModel(String basePath, String fileName, String message) {
        this.basePath = basePath;
        this.fileName = fileName;
        this.message = message;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
