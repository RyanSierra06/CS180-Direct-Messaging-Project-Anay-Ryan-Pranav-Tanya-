import java.io.*;
import java.net.*;
import java.util.Scanner;


public class ApplicationClient implements ApplicationInterface {
    private static final int SERVER_PORT = 4242;
    private static final Object gateKeep = new Object();

    private static synchronized void actionsAfterLogin(User currentUser, Scanner sc) {
        boolean exit = false;
        while (!exit) {
            //Maybe change the scanner to not pass to the method and instead make a new one
            System.out.println("\nUser Actions Menu:");
            System.out.println("1. Set Name");
            System.out.println("2. Set Profile Description");
            System.out.println("3. Set Profile Picture");
            System.out.println("4. View Profile Information");
            System.out.println("5. Send Message");
            System.out.println("6. Block User");
            System.out.println("7. Unblock User");
            System.out.println("8. View Blocked Users");
            System.out.println("9. Add Friend");
            System.out.println("10. Remove Friend");
            System.out.println("11. View Friends");
            System.out.println("12. Read Messages");
            System.out.println("13. Log Out");

            System.out.print("Choose an action (1-13): ");
            String action = sc.nextLine().trim();

            switch (action) {
                case "1" -> {
                    System.out.print("Enter new name: ");
                    String name = sc.nextLine();
                    currentUser.setName(name);
                    System.out.println("Name updated successfully.");
                }

                case "2" -> {
                    System.out.print("Enter profile description: ");
                    String description = sc.nextLine();
                    currentUser.setProfileDescription(description);
                    System.out.println("Profile description updated.");

                }

                case "3" -> {
                    System.out.print("Enter profile picture URL: ");
                    String picture = sc.nextLine();
                    currentUser.setProfilePicture(picture);
                    System.out.println("Profile picture updated.");
                }

                case "4" -> {
                    System.out.println("Name: " + currentUser.getName());
                    System.out.println("Profile Description: " + currentUser.getProfileDescription());
                    System.out.println("Profile Picture: " + currentUser.getProfilePicture());
                }

                case "5" -> {
                    //synchronize the sending of the message
                    //check to see if the user is blocked first
                    //display the actual message (you already do this later, but maybe there's a way
                    //to show the message history as it happens instead of calling for it
                    //block user mid send
                    System.out.print("Enter receiver username: ");
                    String receiver = sc.nextLine();
                    System.out.print("Enter message type content (Image/Text): ");
                    String type = sc.nextLine();
                    System.out.print("Enter message content: ");
                    String content = sc.nextLine();
                    Message message = new Message(currentUser, type, content);
                    currentUser.sendMessage(message, receiver);
                    System.out.println("Message sent to " + receiver);
                }

                case "6" -> {
                    //check if the method returns true of false and let the user know
                    //if the person is already blocked or if they successfully blocked
                    System.out.print("Enter username to block: ");
                    String blockUser = sc.nextLine();
                    currentUser.blockUser(blockUser);
                    System.out.println(blockUser + " has been blocked.");
                }

                case "7" -> {
                    //check if the method returns true of false and let the user know
                    //if the person is already blocked or if they successfully blocked
                    System.out.print("Enter username to unblock: ");
                    String unblockUser = sc.nextLine();
                    currentUser.unblockUser(unblockUser);
                    System.out.println(unblockUser + " has been unblocked.");
                }

                case "8" -> {
                    System.out.println("Blocked Users: " + currentUser.getBlockedUsers());
                }

                case "9" -> {
                    //Same comments as for the block and unblock user
                    System.out.print("Enter username to add as friend: ");
                    String newFriend = sc.nextLine();
                    currentUser.addFriend(newFriend);
                    System.out.println(newFriend + " added to friends.");
                }

                case "10" -> {
                    //Same comments as for the block and unblock user
                    System.out.print("Enter username to remove from friends: ");
                    String oldFriend = sc.nextLine();
                    currentUser.removeFriend(oldFriend);
                    System.out.println(oldFriend + " removed from friends.");
                }

                case "11" -> {
                    System.out.println("Friends List: " + currentUser.getFriends());
                }

                case "12" -> {
                    System.out.print("Enter sender username to view messages from: ");
                    String sender = sc.nextLine();
                    System.out.println("Messages from " + sender + ": " + currentUser.readMessages(sender));
                }

                case "13" -> {
                    System.out.println("Logging out...");
                    exit = true;
                }

                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static User createUserMain(Scanner sc) {
        //SETTING THE USERNAME
        String user = "";
        String pass = "";
        boolean validUser = false;
        boolean validPass = false;

        do {
            System.out.println("Enter the username without '-' (also it can't be empty): ");
            user = sc.nextLine().trim();
            if (user.contains("-")) {
                System.out.println("Username contains '-'. Try again!");
            } else if (user.isEmpty()) {
                System.out.println("Empty username! Try again!");
            } else {
                File f = new File("files/"+ user + ".txt");
                if (f.exists()) {
                    System.out.println("Username already exists! Try again!");
                } else {
                    validUser = true;
                }
            }

        } while (!validUser);



        do {
            System.out.println("Enter the password without '-' (also it can't be empty): ");
            pass = sc.nextLine().trim();
            if (pass.contains("-")) {
                System.out.println("Password contains '-'. Try again!");
            } else if (pass.isEmpty()) {
                System.out.println("Empty password! Try again!");

            } else {
                validPass = true;
            }

        } while (!validPass);

        User newUser = null;

        synchronized (gateKeep) {
            try {
                newUser = new User(user, pass);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return newUser;
    }


    public static void main(String args[]) throws IOException {

        Scanner sc = new Scanner(System.in);
        Socket socket = null;
        System.out.println("Welcome to the Social Media Application (Phase 1)");

        String userMenu = "User Menu";
        String CHOICE_1 = "1. CREATE A NEW ACCOUNT";
        String CHOICE_2 = "2. LOGIN WITH EXISTING ACCOUNT";
        String CHOICE_3 = "3. EXIT";

        String choice = "";

        boolean validChoice = false;
        do {
            System.out.println(userMenu);
            System.out.println(CHOICE_1);
            System.out.println(CHOICE_2);
            System.out.println(CHOICE_3);
            choice = sc.nextLine();
            if ("123".contains(choice) || choice.length() == 1) {
                validChoice = true;
            }
        } while (!validChoice);

        switch (choice) {
            case "1" -> {
                User newUser = createUserMain(sc);
                System.out.println("Created Login!");
            }
            case "2" -> {
                System.out.println("Enter the username: ");
                String user = sc.nextLine().trim();
                System.out.println("Enter the password: ");
                String pass = sc.nextLine().trim();

                File f = new File("files/" + user + ".txt");
                if (f.exists()) {
                    User currentUser = new User(user, pass);
                    if (currentUser.getPassword().equals(pass)) {
                        actionsAfterLogin(currentUser, sc);
                    }
                    else{
                        System.out.println("Invalid Password!");
                    }
                } else {
                    System.out.println("Sorry User does not exist!");
                }
            }

            case "3" -> {
                System.out.println("Exiting the Menu!");
            }

            default -> {
                System.out.println("Invalid Input!");
            }
         }  //end of switch   

            System.out.println("Thank you for using The Social Media Application (Phase 2)!");

        }
}