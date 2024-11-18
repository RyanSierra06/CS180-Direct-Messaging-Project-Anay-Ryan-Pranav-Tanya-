import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.text.html.StyleSheet;

/**
 * Project 4 -- ApplicationServer
 *
 * This is the ApplicationServer class which starts the server to house each of the threads of the different clients
 *
 * @author Pranav Neti, Ryan Sierra, Tanya Jain, Anay Misra - Lab Section 12
 *
 * @version Nov 17, 2024
 *
 */

public class ApplicationServer implements ApplicationServerInterface, Runnable {
   private static final int portNumber = 4242;
   private final Socket clientSocket;

   public ApplicationServer(Socket clientSocket) {
      this.clientSocket = clientSocket;
   }

   public void run() {
      handleClient(clientSocket);
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

         StringBuilder messages  = new StringBuilder();
         while(true) {
            choice = input.readLine();
            System.out.println(choice);

            if (choice.startsWith("Username Create: ")) {
               username = choice.substring("Username Create: ".length());
               File f = new File("files/" + username + ".txt");
               if(f.exists()) {
                  output.write("Username Exists\n");
                  output.flush();
               } else {
                  output.write("New User\n");
                  output.flush();
               }
            }

            else if (choice.startsWith("Username Login: ")) {
               username = choice.substring("Username Login: ".length());
            }

            else if (choice.startsWith("Password Create: ")) {
               password = choice.substring("Password Create: ".length());
               user = new User(username, password);
            }

            else if (choice.startsWith("Password Login: ")) {
               password = choice.substring("Password Login: ".length());
               boolean passed = false;
               File f = new File("files/" + username + ".txt");
               if(f.exists()) {
                  BufferedReader passwordsFile = new BufferedReader(new FileReader(new File("files/usernamesAndPasswords.txt")));
                  String userDetails = passwordsFile.readLine();
                  while(userDetails != null) {
                     String[] details = userDetails.split("-");
                     if(details[0].equals(username) && details[1].equals(password)) {
                        output.write("Logged In!\n");
                        output.flush();
                        passed = true;
                        break;
                     } else if(details[0].equals(username) && !details[1].equals(password)) {
                        output.write("Wrong Password\n");
                        output.flush();
                        break;
                     } else {
                        userDetails = passwordsFile.readLine();
                     }
                  }
                  passwordsFile.close();
               } else {
                  passed = true;
                  output.write("Created New User!\n");
                  output.flush();
               }

               if(passed) {
                  user = new User(username, password);
               }
            }

            else if (choice.startsWith("Name: ")) {
               name = choice.substring("Name: ".length());
               user.setName(name);
            }

            else if (choice.startsWith("Profile Description: ")) {
               profileDescription = choice.substring("Profile Description: ".length());
               user.setProfileDescription(profileDescription);
            }

            else if (choice.startsWith("Profile Picture: ")) {
               profilePicture = choice.substring("Profile Picture: ".length());
               user.setProfilePicture(profilePicture);
            }
            else if (choice.equals("Profile Information: ")) {
               output.write(user.getName() + "\n");
               output.flush();
               output.write(user.getProfileDescription() + "\n");
               output.flush();
               output.write(user.getProfilePicture() + "\n");
               output.flush();
               //profile picture only returns the path right now since were in the terminal
               //change to be a ImageIcon with the GUI
            }

            else if(choice.startsWith("Check Block/Friends: ")) {
               String receiver = choice.substring("Check Block/Friends: ".length());
               if(user.isBlocked(receiver, user.getUsername())){
                  output.write("One of you has blocked the other\n");
                  output.flush();
               } else if(!User.checkCanReceiveAnyone(receiver) && !User.checkIsFriend(receiver, user.getUsername())) {
                  output.write("The reciever Doesnt Accept Messages from Non-Friends\n");
                  output.flush();
               } else if(!User.checkCanReceiveAnyone(user.getUsername()) && !User.checkIsFriend(user.getUsername(), receiver)) {
                  output.write("You can't message Non-Friends, please friend this person first\n");
                  output.flush();
               } else {
                  output.write("Can message\n");
                  output.flush();
               }
            }

            else if (choice.startsWith("Check Profile of: ")) {
               String otherUsername = choice.substring("Check Profile of: ".length());
               String[] parts = User.otherUserProfile(otherUsername);
               System.out.println(parts.length);
               if(parts.length <= 1 ) {
                  output.write("This User Doesnt Exist\n");
                  output.flush();
               } else {
                  output.write(parts[0] + "\n");
                  output.write(parts[1] + "\n");
                  output.write(parts[2] + "\n");
                  output.flush();
               }
            }

            else if (choice.startsWith("Message: ")) {

               String otherUser = choice.substring("Message: ".length(), choice.indexOf("-"));
               System.out.println(otherUser);
               choice = choice.substring(choice.indexOf("-") + 1);

               String thisMessage = choice.substring(0, choice.indexOf("-"));
               System.out.println(thisMessage);
               choice = choice.substring(choice.indexOf("-") + 1);

               String type = choice.substring(0, choice.indexOf("-"));
               System.out.println(type);
               choice = choice.substring(choice.indexOf("-") + 1);

               String counter = choice;
               System.out.println(counter);
               String first = (user.getUsername().compareTo(otherUser) > 0 ? otherUser : user.getUsername());
               String second = (user.getUsername().equals(first) ? otherUser : user.getUsername());

               System.out.println("we reached this point");
               System.out.println("we reached this point");

               if (Integer.parseInt(counter) == 0) {
                  //First time coming back so print the message history
                  System.out.println("before we send message");
                  boolean messageSent = user.sendMessage(new Message(user, type, thisMessage), otherUser);
                  System.out.println("we just sent a message");
                  String messageHistory = user.readMessages(otherUser);
                  output.write("Message: " + "\n" + messageHistory + "\n");
                  output.flush();
                  System.out.println("we just sent a message");
               } else if(Integer.parseInt(counter) > 0) {
                  //Second time, so everything is already printed, we just want the new messages to start coming in
                  user.sendMessage(new Message(user, type, thisMessage), otherUser);
                  output.write("Message: " + "\n" + user.findMostRecentMessages(otherUser) + "\n");
                  output.flush();
                  System.out.println("we just sent a message");
               }
            }


            else if (choice.startsWith("Block User: ")) {
               blockUser = choice.substring("Block User: ".length());
               user.blockUser(blockUser);
            }
            else if (choice.startsWith("Unblock User: ")) {
               unblockUser = choice.substring("Unblock User: ".length());
               user.unblockUser(unblockUser);
            }
            else if (choice.startsWith("Blocked Users: ")) {
               output.write(user.getBlockedUsers() + "\n");
               output.flush();
            }
            else if (choice.startsWith("Add Friend: ")) {
               addFriend = choice.substring("Add Friend: ".length());
               user.addFriend(addFriend);
            }
            else if (choice.startsWith("Remove Friend: ")) {
               removeFriend = choice.substring("Remove Friend: ".length());
               user.removeFriend(removeFriend);
            }
            else if (choice.startsWith("Friend List: ")) {
               output.write(user.getFriends() + "\n");
               output.flush();
            }

            else if(choice.startsWith("Change Can Receive Anyone: ")) {
               boolean yesNo = Boolean.parseBoolean(choice.substring("Change Can Receive Anyone: ".length()));
               user.setReceiveAnyone(yesNo);
            }
            else if (choice.startsWith("Exit")) {
               clientSocket.close();
               break;
            }

            else if(choice.equals("quit")) {
               output.write("quit+\n");
               output.flush();
            }

            else if(choice.startsWith("DELETE: ")) {
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
               while((line = br.readLine()) != null) {
                  String[] parts = line.split("-");
                  if(parts[2].equals(message) && parts[0].equals(username)) {
                     user.deleteMessage(receiver, new Message(user, type, message));
                     output.write("Successful Delete Message\n");
                     output.flush();
                     condition = true;
                     break;
                  }
               }
               if(!condition) {
                  output.write("This Messages Does Not Exist\n");
                  output.flush();
               }


            } else if(choice.equals("Give username")) {
               output.write(username + "\n");
               output.flush();
            }

            else {
               System.out.println("None of teh commands");
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