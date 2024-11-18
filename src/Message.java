import java.util.*;
import java.io.*;

//usernameMessages.txt
//message1
//message2
//message3

/**
 * Group PJ -- Message
 *
 * This is the Message class
 * (See the ReadMe for more details)
 *
 * @author Pranav Neti, Ryan Sierra, Tanya Jain, Anay Misra - Lab Section 12
 *
 * @version Nov 3, 2024
 *
 */

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

    public boolean assignToUser(String message, String type1, String userName) {
        try (PrintWriter pw = new PrintWriter(new File("files/" + userName + "Messages.txt"))) {
            //message
            //message
            //message
            //......
            if (checkMessageType(type1)) {
                pw.println(message);
                pw.close();
                return true;
            } else {
                pw.println("Invalid Message Type");
                pw.close();
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean checkMessageType(String type2) {
        return type2.equalsIgnoreCase("text") || type2.equalsIgnoreCase("image");
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
