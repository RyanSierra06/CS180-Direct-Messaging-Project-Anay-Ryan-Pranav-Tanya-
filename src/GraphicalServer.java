import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;

import javax.imageio.ImageIO;

/**
 * Project 4 -- ApplicationServer
 * <p>
 * This is the ApplicationServer class which starts the server to house each of the threads of the different clients
 *
 * @author Pranav Neti, Ryan Sierra, Tanya Jain, Anay Misra - Lab Section 12
 * @version Nov 17, 2024
 */

public class GraphicalServer implements ApplicationServerInterface, Runnable {
    //TODO make the interface for GraphicalServer
    private static int portNumber = 4243;
    private final Socket cs;

    public GraphicalServer(Socket clientSocket) {
        this.cs = clientSocket;
    }

    public void run() {
        handleClient(cs);
    }

    public void handleClient(Socket clientSocket) {
        try (BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             BufferedWriter output = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))) {
            String choice = "";
            //user should never be null (outside declaration) since were either forcing the user to create or log in
            //and if its unsuccessful, it should kick them out
            User user = null;
            String username = "";
            String password = "";
            String name = "";
            String profileDescription = "";
            String profilePicture = "";
            String blockUser = "";
            String unblockUser = "";
            String blockedUsers = "";
            String addFriend = "";
            String removeFriend = "";
            String friends = "";

            StringBuilder messages = new StringBuilder();
            while (true) {
                choice = input.readLine();
                System.out.println(choice);

                if (choice.startsWith("Username Create: ")) {
                    username = choice.substring("Username Create: ".length());
                    File f = new File("files/" + username + ".txt");
                    if (f.exists()) {
                        output.write("Username Exists\n");
                        output.flush();
                    } else {
                        output.write("New User\n");
                        output.flush();
                    }
                } else if (choice.startsWith("Username Login: ")) {
                    username = choice.substring("Username Login: ".length());
                } else if (choice.startsWith("Password Create: ")) {
                    password = choice.substring("Password Create: ".length());
                    user = new User(username, password);
                } else if (choice.startsWith("Password Login: ")) {
                    password = choice.substring("Password Login: ".length());
                    boolean passed = false;
                    File f = new File("files/" + username + ".txt");
                    if (f.exists()) {
                        BufferedReader passwordsFile = new BufferedReader(new FileReader(
                                "files/usernamesAndPasswords.txt"));
                        String userDetails = passwordsFile.readLine();
                        while (userDetails != null) {
                            String[] details = userDetails.split("-");
                            if (details[0].equals(username) && details[1].equals(password)) {
                                output.write("Logged In!\n");
                                output.flush();
                                passed = true;
                                break;
                            } else if (details[0].equals(username) && !details[1].equals(password)) {
                                output.write("Wrong Password\n");
                                output.flush();
                                break;
                            } else {
                                userDetails = passwordsFile.readLine();
                            }
                        }
                        passwordsFile.close();
                    } else {
                        if (input.readLine().equals("yes"))  {
                            passed = true;
                            output.write("Created New User!\n");
                            output.flush();
                        }
                    }

                    if (passed) {
                        user = new User(username, password);
                    }
                } else if (choice.startsWith("Name: ")) {
                    name = choice.substring("Name: ".length());
                    user.setName(name);
                } else if (choice.startsWith("Profile Description: ")) {
                    profileDescription = choice.substring("Profile Description: ".length());
                    user.setProfileDescription(profileDescription);
                } else if (choice.startsWith("Profile Picture: ")) {
                    profilePicture = choice.substring("Profile Picture: ".length());
                    if (profilePicture != null && !profilePicture.isEmpty()) {
                        try {
                            File imageFile = new File(profilePicture);
                            BufferedImage image = ImageIO.read(imageFile);

                            if (image == null) {
                                output.write("Image file not found.\n");
                                output.flush();
                            } else {
                                user.setProfilePicture(profilePicture);
                                output.write("Image Found\n");
                                output.flush();
                            }
                        } catch (Exception e1) {
                            output.write("Failed to load the image\n");
                            output.flush();
                        }
                    } else {
                        output.write("No input provided.\n");
                        output.flush();
                    }
                } else if (choice.equals("Profile Information: ")) {
                    output.write(user.getName() + "\n");
                    output.flush();
                    output.write(user.getProfileDescription() + "\n");
                    output.flush();
                    output.write(user.getProfilePicture() + "\n");
                    output.flush();
                    //profile picture only returns the path right now since were in the terminal
                    //change to be a ImageIcon with the GUI
                } else if (choice.startsWith("Check Block/Friends: ")) {
                    String receiver = choice.substring("Check Block/Friends: ".length());
                    System.out.println(receiver);
                    if(!user.checkUserExists(receiver)) {
                        output.write("This user doesnt exist\n");
                        output.flush();
                    }
                    else if (user.isBlocked(receiver, user.getUsername())) {
                        output.write("One of you has blocked the other\n");
                        output.flush();
                    } else if (!User.checkCanReceiveAnyone(receiver) &&
                            !User.checkIsFriend(receiver, user.getUsername())) {
                        output.write("The reciever Doesnt Accept Messages from Non-Friends\n");
                        output.flush();
                    } else if (!User.checkCanReceiveAnyone(user.getUsername()) &&
                            !User.checkIsFriend(user.getUsername(), receiver)) {
                        output.write("You can't message Non-Friends, please friend this person first\n");
                        output.flush();
                    } else {
                        output.write("Can message\n");
                        System.out.println("Said theyre clear");
                        output.flush();
                    }

                } else if (choice.startsWith("Check Profile of: ")) {
                    String otherUsername = choice.substring("Check Profile of: ".length());
                    String[] parts = User.otherUserProfile(otherUsername);
                    System.out.println(parts.length);
                    if (parts.length <= 1) {
                        output.write("This User Doesnt Exist\n");
                        output.flush();
                    } else {
                        output.write(parts[0] + "\n");
                        output.write(parts[1] + "\n");
                        output.write(parts[2] + "\n");
                        output.flush();
                    }

                } else if (choice.startsWith("Message: ")) {
                    String otherUser = choice.substring("Message: ".length(), choice.indexOf("-"));
                    System.out.println(otherUser);
                    choice = choice.substring(choice.indexOf("-") + 1);

                    String thisMessage = choice.substring(0, choice.indexOf("-"));
                    System.out.println(thisMessage);
                    choice = choice.substring(choice.indexOf("-") + 1);

                    String type = choice;
                    System.out.println(type);

                    String first = (user.getUsername().compareTo(otherUser) > 0 ? otherUser : user.getUsername());
                    String second = (user.getUsername().equals(first) ? otherUser : user.getUsername());

                    System.out.println("we reached this point");
                    System.out.println("we reached this point");

                    //   if (Integer.parseInt(counter) == 0) {
                    //First time coming back so print the message history
                    System.out.println("before we send message");
                    boolean messageSent = user.sendMessage(new Message(user, type, thisMessage), otherUser);
                    System.out.println("we just sent a message" + messageSent);
                    String messageHistory = user.readMessages(otherUser);
                    output.write("Message: " + "\n" + messageHistory + "\n");
                    output.flush();
                    System.out.println("we just sent a message");
                    //   } else if (Integer.parseInt(counter) > 0) {
                    //       //Second time, so everything is already printed, we just want the new messages to start
                    //       //coming in
                    //       user.sendMessage(new Message(user, type, thisMessage), otherUser);
                    //       output.write("Message: " + "\n" + user.findMostRecentMessages(otherUser) + "\n");
                    //       output.flush();
                    //       System.out.println("we just sent a message");
                    //   }
                } else if (choice.startsWith("Block User: ")) {
                    blockUser = choice.substring("Block User: ".length());
                    boolean check = user.blockUser(blockUser);
                    if(check) {
                        output.write("Successful\n");
                        output.flush();
                    } else {
                        output.write("Failed\n");
                        output.flush();
                    }
                } else if (choice.startsWith("Unblock User: ")) {
                    unblockUser = choice.substring("Unblock User: ".length());
                    boolean check = user.unblockUser(unblockUser);
                    if(check) {
                        output.write("Successful\n");
                        output.flush();
                    } else{
                        output.write("Failed\n");
                        output.flush();
                    }
                } else if (choice.startsWith("Blocked Users: ")) {
                    output.write(user.getBlockedUsers() + "\n");
                    output.flush();
                } else if (choice.startsWith("Add Friend: ")) {
                    addFriend = choice.substring("Add Friend: ".length());
                    boolean check = user.addFriend(addFriend);
                    if(check) {
                        output.write("Successful\n");
                        output.flush();
                    } else {
                        output.write("Failed\n");
                        output.flush();
                    }
                } else if (choice.startsWith("Remove Friend: ")) {
                    removeFriend = choice.substring("Remove Friend: ".length());
                    boolean check = user.removeFriend(removeFriend);
                    if(check) {
                        output.write("Successful\n");
                        output.flush();
                    } else {
                        output.write("Failed\n");
                        output.flush();
                    }
                } else if (choice.startsWith("Friend List: ")) {
                    output.write(user.getFriends() + "\n");
                    output.flush();
                } else if (choice.startsWith("Change Can Receive Anyone: ")) {
                    boolean yesNo = Boolean.parseBoolean(choice.substring("Change Can Receive Anyone: ".length()));
                    user.setReceiveAnyone(yesNo);
                } else if (choice.startsWith("Exit")) {
                    clientSocket.close();
                    break;
                } else if (choice.equals("quit")) {
                    output.write("quit+\n");
                    output.flush();
                } else if (choice.startsWith("DELETE: ")) {
                    String receiver = choice.substring("DELETE: ".length(), choice.indexOf("-"));

                    choice = choice.substring(choice.indexOf("-") + 1);
                    String message = choice.substring(0, choice.indexOf("-"));

                    choice = choice.substring(choice.indexOf("-") + 1);
                    String type = choice;

                    String first = "";
                    String second = "";
                    StringBuilder sb = new StringBuilder();
                    if (receiver.compareTo(username) < 0) {
                        first = receiver;
                        second = user.getUsername();
                    } else if (receiver.compareTo(username) > 0) {
                        second = receiver;
                        first = user.getUsername();
                    }
                    BufferedReader br = new BufferedReader(new FileReader("files/" + first + "-" + second + ".txt"));
                    String line = "";
                    boolean condition = false;
                    while ((line = br.readLine()) != null) {
                        String[] parts = line.split("-");
                        if (parts[2].equals(message) && parts[0].equals(username)) {
                            user.deleteMessage(receiver, new Message(user, type, message));
                            output.write("Successful Delete Message\n");
                            output.flush();
                            condition = true;
                            System.out.println("Message Found");
                            break;
                        }
                    }
                    if (!condition) {
                        output.write("This Messages Does Not Exist\n");
                        output.flush();
                        System.out.println("Message Does Not Exist");
                    }


                } else if (choice.equals("Give username")) {
                    output.write(username + "\n");
                    output.flush();
                } else if(choice.startsWith("In The Thread: ")){
                    choice = choice.substring("In The Thread: ".length());
                    output.write(choice + "\n");
                    output.flush();
                    System.out.println(choice);

                } else {
                    System.out.println("None of the commands");
                }
            }

        } catch (IOException e) {
            System.err.println("SERVER: Error handling client: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("SERVER: Could not close socket: " + e.getMessage());
            }
        }
    }


}