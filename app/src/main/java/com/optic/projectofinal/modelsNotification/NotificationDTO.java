package com.optic.projectofinal.modelsNotification;

public abstract class NotificationDTO {
    private String title;
    private String option;
    private String body;
    private int idNotification;
    public NotificationDTO(String title, String option, String body) {
        this.title = title;
        this.option = option;
        this.body = body;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getIdNotification() {
        return idNotification;
    }

    public void setIdNotification(int idNotification) {
        this.idNotification = idNotification;
    }

    @Override
    public String toString() {
        return "NotificationDTO{" +
                "title='" + title + '\'' +
                ", option='" + option + '\'' +
                ", body='" + body + '\'' +
                ", idNotification='" + idNotification + '\'' +
                '}';
    }
}
