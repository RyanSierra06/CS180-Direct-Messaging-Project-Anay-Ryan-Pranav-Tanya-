public interface MessageInterface {
    boolean assignToUser(User user);
    boolean deleteMessage();
    boolean checkBlocked();
    String getMessage();
    String getUser();
    String getType();
    ArrayList<String> getMessageList();
    ArrayList<User> getUserList();

}