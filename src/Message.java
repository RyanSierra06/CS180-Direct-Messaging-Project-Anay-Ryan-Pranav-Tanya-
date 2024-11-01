import java.util.*;
import java.io.*;

//usernameMessages.txt
//message1
//message2
//message3


public class Message implements MessageInterface {
    String type;
    User mainUser;
    String messageText;
    String messageImage;

    public Message(User user, String type, String message) {
        this.mainUser = user;
        if (type.equalsIgnoreCase("text")) {
            this.messageText = message;
            this.messageImage = "";
            this.type = type;
        } else if (type.equalsIgnoreCase("image")) {
            this.messageImage = message;
            this.messageText = "";
            this.type = type;
        } else {
            this.messageImage = "";
            this.messageText = "";
            this.type = "Invalid Message Type";
        }
    }

    public boolean assignToUser(String message, String type, String userName) {
        try(PrintWriter pw = new PrintWriter(new File(userName + "Messages.txt"))) {
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
            }
        } catch (IOException e) {
            e.printStackTrace();
            return  false;
        }
    }

    public boolean checkMessageType(String type) {
        return type.equalsIgnoreCase("text") || type.equalsIgnoreCase("image");
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
