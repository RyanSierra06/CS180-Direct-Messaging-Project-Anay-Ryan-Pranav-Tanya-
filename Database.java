import java.util.ArrayList;

public class Database implements DatabaseInterface {
    private ArrayList<User> users; // List of all users
    private ArrayList<Message> messages; // List of all messages

    public Database() {
        this.users = new ArrayList<>();
        this.messages = new ArrayList<>();
    }

    // User Management
    @Override
    public boolean addUser(User user) {
        if (getUser(user.getUsername()) != null) return false;
        users.add(user);
        return true;
    }

    @Override
    public User getUser(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public boolean updateUser(User user) {
        int index = getUserIndex(user.getUsername());
        if (index == -1) return false;
        users.set(index, user);
        return true;
    }

    @Override
    public boolean deleteUser(String username) {
        int index = getUserIndex(username);
        if (index == -1) return false;
        users.remove(index);
        return true;
    }

    private int getUserIndex(String username) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(username)) {
                return i;
            }
        }
        return -1;
    }

    // Message Management
    @Override
    public boolean addMessage(Message message) {
        return messages.add(message);
    }

    @Override
    public ArrayList<Message> getMessages(User user) {
        ArrayList<Message> userMessages = new ArrayList<>();
        for (Message message : messages) {
            if (message.getUser().equals(user)) {
                userMessages.add(message);
            }
        }
        return userMessages;
    }

    @Override
    public boolean deleteMessage(Message message) {
        return messages.remove(message);
    }

    // Friend and Block Management
    @Override
    public boolean addFriend(User user, User friend) {
        user.addFriend(friend);
        return true;
    }

    @Override
    public boolean removeFriend(User user, User friend) {
        return user.getFriends().remove(friend);
    }

    @Override
    public boolean blockUser(User user, User blockedUser) {
        user.blockUser(blockedUser);
        return true;
    }

    @Override
    public boolean unblockUser(User user, User blockedUser) {
        user.unblockUser(blockedUser);
        return true;
    }
}