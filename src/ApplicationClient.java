import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ApplicationClient implements ApplicationInterface {
    private static final int SERVER_PORT = 4242;
    private static final Object gateKeep = new Object();

    private static void actionsAfterLogin(User currentUser, Scanner sc, PrintWriter writer, BufferedReader reader) {
        boolean exit = false;
        while (!exit) {
            //Maybe change the scanner to not pass to the method and instead make a new one
            System.out.println("\nUser Actions Menu:");
            System.out.println("1. Set Name");
            System.out.println("2. Set Profile Description");
            System.out.println("3. Set Profile Picture");
            System.out.println("4. View Profile Information");
            System.out.println("5. Log Out");

            System.out.print("Choose an action (1-5): ");
            String action = sc.nextLine().trim();

            try {
                switch (action) {
                    case "1" -> {
                        System.out.print("Enter new name: ");
                        String name = sc.nextLine();
                        writer.println("SET_NAME");
                        writer.println(name);
                        System.out.println("Name updated successfully.");
                    }

                    case "2" -> {
                        System.out.print("Enter profile description: ");
                        String description = sc.nextLine();
                        writer.println("SET_DESCRIPTION");
                        writer.println(description);
                        System.out.println("Profile description updated.");
                    }

                    case "3" -> {
                        System.out.print("Enter profile picture URL: ");
                        String picture = sc.nextLine();
                        writer.println("SET_PICTURE");
                        writer.println(picture);
                        System.out.println("Profile picture updated.");
                    }

                    case "4" -> {
                        writer.println("VIEW_PROFILE");
                        System.out.println("Profile Information:");
                        System.out.println(reader.readLine());
                    }

                case "5" -> {
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
                    System.out.print("Enter username to block: ");
                    String blockUser = sc.nextLine();
                    currentUser.blockUser(blockUser);
                    System.out.println(blockUser + " has been blocked.");
                }

                case "7" -> {
                    System.out.print("Enter username to unblock: ");
                    String unblockUser = sc.nextLine();
                    currentUser.unblockUser(unblockUser);
                    System.out.println(unblockUser + " has been unblocked.");
                }

                case "8" -> {
                    System.out.println("Blocked Users: " + currentUser.getBlockedUsers());
                }

                case "9" -> {
                    System.out.print("Enter username to add as friend: ");
                    String newFriend = sc.nextLine();
                    currentUser.addFriend(newFriend);
                    System.out.println(newFriend + " added to friends.");
                }

                case "10" -> {
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
            } catch (IOException e) {
                System.out.println("Error communicating with server: " + e.getMessage());
            }
        }
    }

    private static User createUserMain(Scanner sc, PrintWriter writer, BufferedReader reader) throws IOException {
        String user = "";
        String pass = "";
        boolean validUser;
        boolean validPass;

        do {
            System.out.print("Enter username (no '-' allowed, cannot be empty): ");
            user = sc.nextLine().trim();
            if (user.contains("-")) {
                System.out.println("Username contains '-'. Try again!");
                validUser = false;
            }
                    else if (user.length() == 0) {
                System.out.println("Empty username! Try again!");
                validUser = false;
            } else {
                File f = new File("files/"+user + ".txt");
                if (f.exists()) {
                    System.out.println("Username already exists! Try again!");
                    validUser = false;
                } else {
                    validUser = true;
                }
            }

        } while (!validUser);

        do {
            System.out.print("Enter password (no '-' allowed, cannot be empty): ");
            pass = sc.nextLine().trim();
            if (pass.contains("-")) {
                System.out.println("Password contains '-'. Try again!");
                validPass = false;
            } else if (pass.length() == 0) {
                System.out.println("Empty password! Try again!");
                validPass = false;
            } else {
                validPass = true;
            }

        } while (!validPass);

        User newUser = null;

        synchronized (gateKeep) {
            try {
                newUser = new User(user, pass);
            } catch (java.lang.Exception e) {
                e.printStackTrace();
            }
        }

        return newUser;
    }


    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in);
             Socket socket = new Socket("localhost", SERVER_PORT);
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

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
            if (!"123".contains(choice) || choice.length() != 1) {
                validChoice = false;
            } else {
                validChoice = true;
            }
        } while (!validChoice);

        switch (choice) {
            case "1" -> {
                User newUser = createUserMain(sc, writer, reader);
                System.out.println("Created Login!");
                break;
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
                        actionsAfterLogin(currentUser, sc, writer, reader);
                    }
                    else{
                        System.out.println("Invalid Password!");
                    }
                } else {
                    System.out.println("Sorry User does not exist!");
                }

                break;
            }

            case "3" -> {
                System.out.println("Exiting the Menu!");
            }

            default -> {
                System.out.println("Invalid Input!");
            }
        }
            System.out.println("Thank you for using The Social Media Application (Phase 2)!");

        } catch (IOException e) {
            System.err.println("Error connecting to server: " + e.getMessage());
        }
    }
}