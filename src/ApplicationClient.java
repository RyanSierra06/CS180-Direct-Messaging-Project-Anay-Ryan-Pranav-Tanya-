import java.util.Scanner;

public class ApplicationC extends ApplicationInterface {
    private static final Object gateKeep;

    private synchronized void actionsAfterLogin(User currentUser) {
        boolean exit = false;
        while (!exit) {
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
                    System.out.print("Enter receiver username: ");
                    String receiver = sc.nextLine();
                    System.out.print("Enter message content: ");
                    String content = sc.nextLine();
                    Message message = new Message(content);
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
        }
    }

    private User createUserMain() {
        //SETTING THE USERNAME
        String user = "";
        boolean validUser = false;
        do {
            System.out.println("Enter the username without '-' (also it can't be empty): ");
            user = sc.nextLine().trim();
            if (user.contains("-") {
                System.out.println("Username contains '-'. Try again!");
                validUser = false;
            }
                    else if (user.length() == 0) {
                System.out.println("Empty username! Try again!");
                validUser = false;
            } else {
                File f = new File("files/"user + ".txt");
                if (f.exists) {
                    System.out.println("Username already exists! Try again!");
                    validUser = false;
                } else {
                    validUser = true;
                }
            }

        } while (!validUser);

        //SETTING THE PASSWORD
        String pass = "";
        boolean validPass = false;
        do {
            System.out.println("Enter the password without '-' (also it can't be empty): ");
            pass = sc.nextLine().trim();
            if (pass.contains("-") {
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
                User newUser = new User(user, pass);
            } catch (java.lang.Exception e) {
                e.printStackTrace();
            }
        }

        return newUser
    }


    public static void main(String args[]) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome to the Social Media Application (Phase 1)");

        String userMenu = "User Menu";
        String CHOICE_1 = "1. CREATE A NEW ACCOUNT";
        String CHOICE_2 = "2. LOGIN WITH EXISTING ACCOUNT";
        String CHOICE_3 = "3. EXIT"

        String choice = "";

        boolean validChoice = false;
        do {
            System.out.print(userMenu);
            System.out.println(CHOICE_1);
            System.out.println(CHOICE_2);
            choice = sc.nextLine();
            if (!"123".contains(choice) || choice.length() != 1) {
                validChoice = false;
            } else {
                validChoice = true;
            }
        } while (!validChoice)

        switch (choice) {
            case "1" -> {
                User newUser = createUserMain();
                System.out.println("Created Login!");
                break;
            }

            case "2" -> {
                System.out.println("Enter the username: ");
                user = sc.nextLine().trim();
                System.out.println("Enter the password: ");
                pass = sc.nextLine().trim();

                File f = new File("files/" + user + ".txt")
                if (f.exists()) {
                    User currentUser = new User(user, pass);
                    if (User.getPassword().equals(pass)) {
                        actionsAfterLogin(currentUser)
                    }
                    else{
                        System.out.println("Invalid Password" +
                                "!")
                    }
                } else {
                    System.out.println("Sorry User does not exist!")
                }

                break;
            }

            case "3" -> {
                System.out.println("Exiting the Menu!")
            }

            case default -> {
                System.out.println("Invalid Input!");
            }

        }

        System.out.println("Thank you for using The Social Media Application (Phase 1)!");
    }


}