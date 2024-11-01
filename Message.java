import java.util.*;
import java.io.*;

public class Message implements MessageInterface {
    String type;
    User mainUser;
    String messageText;
    String messageImage;

    public Message(User user, String type, String message) {
        this.mainUser = user;
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
    }

    public boolean assignToUser(String message, String type, String userName) {
        // TODO change user parameter to username of type string and modify messages in that method
        try {
            PrintWriter pw = new PrintWriter(new File(userName + "Messages.txt"));
            //message
            //message
            //message
            //......
            if(checkMessageType(type)) {
                pw.println(message);
                return true;
            } else  {
                pw.println("Invalid Message Type");
                return false;
                //this should never actually run since were going to add JButtons, but for a command line test taking user text input, we need these
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //basically your addMessage class
        //since the index of both arrays will be the same, their user and message are stored at the same spot

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

    public User getMainUser() {
        return mainUser;
    }
}
