public interface MessageInterface {
    boolean assignToUser(String message, String type, String userName);

    boolean checkMessageType(String type);

    String getMessageText();

    String getMessageImage();

    String getType();

    User getMainUser();
}