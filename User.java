import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.annotation.processing.Filer;

import java.io.File;
import java.io.FileReader;


public class User {
    // user informatuon
    ArrayList<String> BlockedUsers;
    ArrayList<Message> thisUsersMessages;
    ArrayList<Message> otherUsersMessages;
    ArrayList<String> friends;

    // profile information
    String name;
    String userName;
    String password;
    String profileDescription;
    String profilePicture;
    String userFileName;

    public User(String userName, String password) {
        try(BufferedReader br = new BufferedReader(new FileReader("userNameAndPasswords.txt"))) {
            String line = br.readLine();
            while(line != null) {
                String[] vars = line.split(" ");
                if(vars[0].equals(userName)) {
                    try(BufferedReader br2 = new BufferedReader(new FileReader(userName + ".txt"))) {
                        String personalIdentifiers = br2.readLine();
                        String[] personalArr = personalIdentifiers.split("-");
                        this.userName = personalArr[0];
                        this.name = personalArr[1];
                        this.profileDescription = personalArr[2];
                        this.profilePicture = personalArr[3];
                        String blocked = br2.readLine();
                        for(String s : blocked.split(" ")) {
                            BlockedUsers.add(s);
                        }
                        String friendStr = br2.readLine();
                        for(String s : friendStr.split(" ")) {
                            friends.add(s);
                        }
                        return;
                    } catch(IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            createUser(userName, password);

        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void createUser(String userName, String password) {
        this.userName = userName;
        this.password = password;
        this.name = null;
        this.profileDescription = null;
        this.profilePicture = null;
        this.userFileName = userName + ".txt";
        try(PrintWriter pw = new PrintWriter(new FileWriter(new File(this.userFileName)));
            PrintWriter pw2 = new PrintWriter(new FileWriter(new File("userNameAndPasswords.txt"), true))) {
            BlockedUsers = new ArrayList<String>();
            thisUsersMessages = new ArrayList<Message>();
            otherUsersMessages = new ArrayList<Message>();
            friends = new ArrayList<String>();
            pw.println(userName + "-" + name + "-" + profileDescription + "-" + profilePicture); // personal identifiers
            pw.println(); // blocked users
            pw.println(); // friends
            pw2.println(userName + " " + password);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setProfileDescription(String profileDescription) {
        this.profileDescription = profileDescription;
    }

    public String getProfileDescription() {
        return this.profileDescription;
    }

    public String getProfilePicture() {
        return this.profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public void sendMessage(Message message, String reciver) {
        // String


        // reciver.getOtherUsersMessages().add(message);
        // this.getThisUsersMessages().add(message);
    }

    public ArrayList<Message> getOtherUsersMessages() {
        return otherUsersMessages;
    }

    public void blockUser(User blockedUser) {
        BlockedUsers.add(blockedUser);
    }

    public void deleteMessage(User reciver, Message message) {
        reciver.getOtherUsersMessages().remove(message);
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
        BlockedUsers.remove(previouslyBlockedUser);

    }
    public void readMessages() {
        //go through the file and just print the messages sent per each person 
        //we probably need some implementation to print them in the correct order

    }
    public String displayProfile() {
        return profileDescription + " , " + profilePicture;
    }
    public void modifyProfile() {

    }
    public User findUser(String userName) {
        // ok
        return null;
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
        // if you can see messages from anybody or just messages from friends
    }

}