import java.uitl.*;

public class Message implements MessageInterface {
    String type;
    User user;
    String messageText;
    String messageImage;
    ArrayList<User> userList; // switch these to a hashMap if theyre allowed
    ArrayList<String> messageList; //switch these to a hashMap if they're allowed

    public Message(User user, String type, String message) {
        this.user = user;
        if (type.equalsIgnoreCase("text")) {
            this.messageText = message; // will be the actual text message to be sent
            this.messageImage = "";
            this.type = type;
        } else if (type.equalsIgnoreCase("image")) {
            this.messageImage = message; // will be the jpg or img file in a string fom (basically the directory location)
            this.messageText = "";
            this.type = type;
        } else {
            this.messageImage = "";
            this.messageText = "";
            this.type = "Invalid Message Type";
        }
        userList = new ArrayList<User>();
        messageList = new ArrayList<String>();
    }

    public boolean assignToUser(String message, String type, User user) {
        //basically your addMessage class
        //since the index of both arrays will be the same, their user and message are stored at the same spot
        if(checkMessageType(type)) {
            messageList.add(message);
        } else  {
            messageList.add("Invalid Message Type");
        }
        userList.add(user);
    }

    public boolean checkMessageType(String type) {
        return type.equalsIgnoreCase("text") || type.equalsIgnoreCase("image"))
    }

    public String getMessageText() {
        return messageText;
    }

    public String getMessageImage() {
        return messageImage;
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
