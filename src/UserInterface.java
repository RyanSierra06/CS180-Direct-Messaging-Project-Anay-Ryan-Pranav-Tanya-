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
    void setReciveAnyone(boolean reciveAnyone);
    boolean canReciveFrom(String senderUsername);
    void sendMessage(Message message, String reciver);
    void blockUser(String blockUser);
    void deleteMessage(String reciver, Message message);
    String getBlockedUsers();
    String getFriends();
    void unblockUser(String previouslyBlockedUser);
    String readMessages(String reciver);
    String findUser(String username);
    void addFriend(String newFriend);
    void removeFriend(String oldFriend);
}