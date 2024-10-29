public class User {
    ArrayList<User> BlockedUsers;
    ArrayList<Message> thisUsersMessages;
    ArrayList<Message> othterUsersMessages;

    public User() {
        blockedUsers = new ArrayList<User>();
        messages = new ArrayList<Message>();
        othterUsersMessages = new ArrayList<Message>();
    }

    public void sendMessage(Message message, User reciver) {
        reciver.addOtherUsersMessages(message);
    }

    public void addOtherUsersMessages(Message message) {
        this.otherUsersMessages.add(message);
    }


    public void blockUser(User blockedUser) {
        blockedUsers.add(blockedUser);
    }
    public void deleteMessage(User reciver) {

    }
    public void unblockUser(User previouslyBlockedUser) {

    }
    public void readMessages() {

    }
    public void displayProfile() {

    }
    public void modifyProfile() {

    }
    public User findUser(String userName) {

    }
    public void displayAnotherProfile(User person) {

    }
    public void addFriend(User newFriend) {

    }
    public void removeFriend(User oldFriend) {

    }
    public void modifyMessageRecivingLimit() {

    }

}