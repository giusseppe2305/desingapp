package com.optic.projectofinal.modelsNotification;

import com.optic.projectofinal.channel.NotificationHelper;
import com.optic.projectofinal.models.Message;

import java.util.Arrays;

public class NotificationMessageDTO extends NotificationDTO{
    private Message[] messages;
    private String idUserToChat;
    private String nameUser;
    private String photoProfile;
    private String idChat;
    public NotificationMessageDTO(String title, NotificationHelper.TYPE_NOTIFICATION option, String body) {
        super(title, option.toString(), body);
    }

    public String getIdChat() {
        return idChat;
    }

    public void setIdChat(String idChat) {
        this.idChat = idChat;
    }

    public Message[] getMessages() {
        return messages;
    }

    public void setMessages(Message[] messages) {
        this.messages = messages;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public String getPhotoProfile() {
        return photoProfile;
    }

    public void setPhotoProfile(String photoProfile) {
        this.photoProfile = photoProfile;
    }

    @Override
    public String toString() {
        return "NotificationMessageDTO{" +
                "messages=" + Arrays.toString(messages) +
                ", nameUser='" + nameUser + '\'' +
                ", photoProfile='" + photoProfile + '\'' +
                '}'+super.toString();
    }

    public String getIdUserToChat() {
        return idUserToChat;
    }

    public void setIdUserToChat(String idUserToChat) {
        this.idUserToChat = idUserToChat;
    }
}
