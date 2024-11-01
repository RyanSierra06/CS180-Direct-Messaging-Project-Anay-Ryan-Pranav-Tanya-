import java.util.*;

public class Message implements MessageInterface {
    String type;
    User user;
    String message;
    ArrayList<User> userList; // switch these to a hashMap if theyre allowed
    ArrayList<String> messageList; //switch these to a hashMap if they're allowed

    public Message(User user, String type, String message) {
        this.user = user;
        this.type = type;
        this.message = message;
        userList = new ArrayList<User>();
        messageList = new ArrayList<String>();
    }

    public boolean validateMessageAndType(String type) {
        if (type.equals("text")) {
            this.message = message; // will be the actual text message to be sent
        } else if (type.equalsIgnoreCase("image")) {
            this.message = message; // will be the jpg or img file in a string fom (basically the directory location)
        } else {
            this.message = "Invalid Message Type";
        }
    }


    public boolean assignToUser(String message, User user) {
        //since the index of both arrays will be the same, they're user and message are stored at the same spot
        userList.add(user);
        messageList.add(message);
    }

    public String getMessage() {
        return message;
    }

    public String getType() {
        return type;
    }

    public User getUser() {
        return user;
    }

    public ArrayList<String> getMessageList() {
        return messageList;
    }

    public ArrayList<User> getUserList() {
        return userList;
    }
}
