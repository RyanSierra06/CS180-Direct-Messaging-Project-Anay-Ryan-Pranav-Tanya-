/**
 * HW-09 -- UserInterface
 *
 * This is the User interface
 *
 * @author Pranav Neti, Ryan Sierra, Tanya Jain, Anay Misra - Lab Section 12
 *
 * @version Nov 3, 2024
 *
 */

public interface UserInterface {
    void createUser(String username, String password);

    void setName(String name);

    String getName();

    String getPassword();

    String getUsername();

    void setProfileDescription(String profileDescription);

    String getProfileDescription();

    void setProfilePicture(String setProfilePicture);

    String getProfilePicture();

    boolean getReceiveAnyone();

    void setReceiveAnyone(boolean receiveAnyone);

    boolean canReceiveFrom(String senderUsername);

    boolean sendMessage(Message message, String receiver);

    boolean blockUser(String blockUser);

    boolean deleteMessage(String receiver, Message message);

    String getBlockedUsers();

    String getFriends();

    boolean unblockUser(String previouslyBlockedUser);

    String readMessages(String reciver);

    String findUser(String username);

    boolean addFriend(String newFriend);

    boolean removeFriend(String oldFriend);
}