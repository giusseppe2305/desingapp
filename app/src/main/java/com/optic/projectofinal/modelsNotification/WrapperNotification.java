package com.optic.projectofinal.modelsNotification;

public class WrapperNotification<T extends NotificationDTO> {
    private String to;
    private String priority;
    private T data;

    public  WrapperNotification(T data) {
        this.to = to;
        this.priority = "high";
        this.data=data;
    }

    public WrapperNotification(String to, String priority, T data) {
        this.to = to;
        this.priority = priority;
        this.data = data;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
