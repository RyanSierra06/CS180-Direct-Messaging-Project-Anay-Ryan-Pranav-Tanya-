/**
 * Group Project -- MessageInterface
 * <p>
 * This is the Message interface
 *
 * @author Pranav Neti, Ryan Sierra, Tanya Jain, Anay Misra - Lab Section 12
 * @version Nov 3, 2024
 */

public interface MessageInterface {
    boolean assignToUser(String message, String type, String userName);

    boolean checkMessageType(String type);

    String getMessageText();

    String getMessageImage();

    String getType();

    User getMainUser();
}