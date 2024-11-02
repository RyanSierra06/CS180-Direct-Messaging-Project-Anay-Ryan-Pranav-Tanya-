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
    boolean canReceiveAnyone();
    void setReceiveAnyone(boolean receiveAnyone);
    boolean canReceiveFrom(String senderUsername);
    void sendMessage(Message message, String receiver);
    void blockUser(String blockUser);
    void deleteMessage(String receiver, Message message);
    String getBlockedUsers();
    String getFriends();
    void unblockUser(String previouslyBlockedUser);
    String readMessages(String reciver);
    String findUser(String username);
    void addFriend(String newFriend);
    void removeFriend(String oldFriend);
}