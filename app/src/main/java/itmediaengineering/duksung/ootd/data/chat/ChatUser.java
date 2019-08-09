package itmediaengineering.duksung.ootd.data.chat;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class ChatUser {

    public String username;
    public String email;

    public ChatUser() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public ChatUser(String username, String email) {
        this.username = username;
        this.email = email;
    }

}
