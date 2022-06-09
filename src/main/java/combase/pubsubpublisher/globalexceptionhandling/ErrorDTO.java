package combase.pubsubpublisher.globalexceptionhandling;


import java.io.Serializable;

public class ErrorDTO implements Serializable {

    private String status;
    private String message;
    private String stack;

    public ErrorDTO(String message) {
        this.message = message;
        this.status = "error";
    }

    public ErrorDTO(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public ErrorDTO(String status, String message, String stack) {
        this.status = status;
        this.message = message;
        this.stack = stack;
    }

    public ErrorDTO() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStack() {
        return stack;
    }

    public void setStack(String stack) {
        this.stack = stack;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}