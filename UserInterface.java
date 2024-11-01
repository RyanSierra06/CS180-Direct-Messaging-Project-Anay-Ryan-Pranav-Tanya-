public interface UserInterface {
    void setPassword(String password);
    String getPassword();
    void setUsername(String userName);
    String getUsername();
    void sendMessage(User reciver);
    void blockUser(User blockedUser);
    void deleteMessage(User reciver);
    void unblockUser(User previouslyBlockedUser);
    void readMessages();
    void displayProfile();
    void modifyProfile();
    User findUser(String userName);
    void displayAnotherProfile(User person);
    void addFriend(User newFriend);
    void removeFriend(User oldFriend);
    void modifyMessageRecivingLimit();   
}