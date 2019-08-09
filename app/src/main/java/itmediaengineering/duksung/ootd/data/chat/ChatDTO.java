package itmediaengineering.duksung.ootd.data.chat;

import java.util.Date;

public class ChatDTO {

    private String userName;
    private String message;

    private Date date;

    public ChatDTO() {}

    public ChatDTO(String userName, String message, Date date) {
        this.userName = userName;
        this.message = message;
        this.date = date;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserName() {
        return userName;
    }

    public String getMessage() {
        return message;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
