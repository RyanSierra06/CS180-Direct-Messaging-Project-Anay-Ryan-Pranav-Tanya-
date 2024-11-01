import java.util.ArrayList;

public interface DatabaseInterface {
    // User management
    boolean addUser(User user);
    User getUser(String username);
    boolean updateUser(User user);
    boolean deleteUser(String username);

    // Message management
    boolean addMessage(Message message);
    ArrayList<Message> getMessages(User user);
    boolean deleteMessage(Message message);

    // Friend and Block management
    boolean addFriend(User user, User friend);
    boolean removeFriend(User user, User friend);
    boolean blockUser(User user, User blockedUser);
    boolean unblockUser(User user, User blockedUser);
}