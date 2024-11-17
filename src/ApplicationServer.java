import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Group PJ -- Application Server
 *
 * This is the Application Server class.
 *
 * @version Nov 14, 2024
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
               //TODO change choice to maybe input.readLine() or input.readLine() to choice
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
               //TODO change choice to maybe input.readLine() or input.readLine() to choice
               username = choice.substring("Username: ".length());               
            }

            else if (choice.startsWith("Password Create: ")) {
               password = choice.substring("Password Create: ".length());
               user = new User(username, password);
            }

            else if (choice.startsWith("Password Login: ")) {
               password = choice.substring("Password Login: ".length());
               File f = new File("files/" + username + ".txt");
               if(f.exists()) {
                  BufferedReader passwordsFile = new BufferedReader(new FileReader(new File("files/usernamesAndPasswords.txt")));
                  String userDetails = passwordsFile.readLine();
                  while(userDetails != null) {
                     String[] details = userDetails.split("-");
                     if(details[0].equals(username) && details[1].equals(password)) {
                        output.write("Logged In!\n");
                        output.flush();
                     } else if(details[0].equals(username)) {
                        output.write("Wrong Password\n");
                        output.flush();
                     } else {
                        userDetails = passwordsFile.readLine();
                     }
                  }
                  passwordsFile.close();
               } else {
                  user = new User(username, password);
                  output.write("Created New User!\n");
                  output.flush();
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

               if(user.isBlocked(user.getUsername(), otherUser)) {
                  output.write("Block Error: Failed to send message." + "\n");
                  output.flush();
               } else if(!user.getReceiveAnyone() && !user.getFriends().contains(otherUser)) {
                  //TODO CHANGE TO ADD IMPLEMENTATION FOR CAN RECEIVE FROM ON FRIENDS
                  output.write("This User Doesnt Accept Messages from Non-Friends" + "\n");
                  output.flush();
               }

               else {
                  String first = (user.getUsername().compareTo(otherUser) > 0 ? otherUser : user.getUsername());
                  String second = (user.getUsername().equals(first) ? otherUser : user.getUsername());
                  File f = new File("files/" + first + "-" + second + ".txt");

                  System.out.println("we reached this point");

                  if (Integer.parseInt(counter) == 0) {
                     //First time coming back so print the message history
                     user.sendMessage(new Message(user, type, thisMessage), otherUser);
                     String messageHistory = user.readMessages(otherUser);
                     output.write("Message: " + "\n" + messageHistory + "\n");
                     output.flush();
                  } else if(Integer.parseInt(counter) > 0) {
                     //Second time, so everything is already printed, we just want the new messages to start coming in
                     user.sendMessage(new Message(user, type, thisMessage), otherUser);
                     output.write("Message: " + "\n" + user.findMostRecentMessages(otherUser) + "\n");
                     output.flush();
                  }
               }


               //TODO back in the client, once you're in case 5, start a
               // loop to keep messaging that user until a certain value is types
               // Maybe have an atomic integer to display message history the first time and
               // then go into just the straight messages back and fourth
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
            else if (choice.startsWith("Exit")) {
               clientSocket.close();
               break;
            }

            else if(choice.equals("quit")) {
               output.write("quit");
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