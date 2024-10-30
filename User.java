public class User {
    ArrayList<User> BlockedUsers;
    ArrayList<Message> thisUsersMessages;
    ArrayList<Message> otherUsersMessages;
    ArrayList<User> friends;

    public User() {
        blockedUsers = new ArrayList<User>();
        messages = new ArrayList<Message>();
        otherUsersMessages = new ArrayList<Message>();
        friends = new ArrayList<User>();
    }

    public void sendMessage(Message message, User reciver) {
        reciver.getOtherUsersMessages().add(message);
    }

    public ArrayList<Message> getOtherUsersMessages() {
        return otherUsersMessages;
    }

    public void blockUser(User blockedUser) {
        blockedUsers.add(blockedUser);
    }

    public void deleteMessage(User reciver, Message message) {
        reciever.getOtherUsersMessages().remove(message);
        this.thisUsersMessages.remove(message);

    }

    public ArrayList<Message> getThisUsersMessages() {
        return thisUsersMessages;
    }

    public ArrayList<User> getBlockedUsers() {
        return BlockedUsers;
    }

    public ArrayList<User> getFriends() {
        return friends;
    }

    public void unblockUser(User previouslyBlockedUser) {
        blockedUsers.remove(previouslyBlockedUser);

    }
    public void readMessages() {

    }
    public void displayProfile() {

    }
    public void modifyProfile() {

    }
    public User findUser(String userName) {

    }
    public void displayAnotherProfile(User otherUser) {

    }
    public void addFriend(User newFriend) {
        friends.add(newFriend);
    }
    public void removeFriend(User oldFriend) {
        friends.remove(oldFriend);
    }
    public void modifyMessageRecivingLimit() {

    }

}