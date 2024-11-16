import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
   private static AtomicInteger counter = new AtomicInteger(2);
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
            if (choice.startsWith("Username: ")) {
               //TODO change choice to maybe input.readLine() or input.readLine() to choice
               username = choice.substring("Username: ".length());
            }
            else if (choice.startsWith("Password: ")) {
               password = choice.substring("Password: ".length());
               user = new User(username, password);
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
            else if (choice.startsWith("Profile Information: ")) {
               output.write(user.getName() + "\n");
               output.write(user.getProfileDescription() + "\n");
               output.write(user.getProfilePicture() + "\n");
               output.flush();
               //profile picture only returns the path right now since were in the terminal
               //change to be a ImageIcon with the GUI
            }
            else if (choice.startsWith("Message: ")) {
               String otherUser = choice.substring("Message: ".length());
               String thisMessage = choice.substring(("Message: " + otherUser + " ").length());
               String type = choice.substring(("Message: " + otherUser + " " + thisMessage + " ").length());
               if(user.isBlocked(user.getUsername(), otherUser)) {
                  output.write("Block Error: Failed to send message.");
                  output.flush();
               } else {
                  String first = (user.getUsername().compareTo(otherUser) > 0 ? otherUser : user.getUsername());
                  String second = (user.getUsername().equals(first) ? otherUser : user.getUsername());
                  File f = new File("files/" + first + "-" + second + ".txt");
                  if(f.exists()) {
                     user.sendMessage(new Message(user, type, thisMessage), otherUser);
                     String messageHistory = user.readMessages(otherUser);
                     output.write(messageHistory + "\n");
                     output.flush();
                  } else {
                     output.write("There are no messages between you and " + otherUser + "\n");
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
               blockedUsers = choice.substring("Blocked Users: ".length());
               output.write(blockedUsers + "\n");
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
               friends = choice.substring("Friend List: ".length());
               output.write(friends + "\n");
               output.flush();
            }
            else if (choice.startsWith("Exit")) {
               break;
            } else {
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
