import java.io.*;
import java.net.*;
import java.util.Scanner;
//TODO Add functionality for can recieve messages from anyone in case 5
// Fix texting (display who the messsage is from), basically who sent it
// make sure it keeps asking for messages

public class ApplicationClient implements ApplicationInterface {
    private static final int SERVER_PORT = 4242;
    private static final Object gateKeep = new Object();

    private void actionsAfterLogin(BufferedWriter bw, BufferedReader br, Scanner sc) {
        boolean exit = false;
        int displayMessageHistoryCounter = 0;

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
            System.out.println("12. Set Can Receive from anyone");
            System.out.println("13. Log Out");

            System.out.print("Choose an action (1-13): ");
            String action = sc.nextLine().trim();

            switch (action) {
                case "1" -> {
                    System.out.print("Enter new name: ");
                    String name = sc.nextLine();
                    try {
                        bw.write("Name: " + name + "\n");
                        bw.flush();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("Name updated successfully.");
                }

                case "2" -> {
                    System.out.print("Enter profile description: ");
                    String description = sc.nextLine();
                    try {
                        bw.write("Profile Description: " + description + "\n");
                        bw.flush();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("Profile description updated.");

                }

                case "3" -> {
                    System.out.print("Enter profile picture URL: ");
                    String picture = sc.nextLine();
                    try {
                        bw.write("Profile Picture: " + picture + "\n");
                        bw.flush();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("Profile picture updated.");
                }

                case "4" -> {
                    try {
                        bw.write("Profile Information: " + "\n");
                        bw.flush();
                        System.out.println("sent prfoile info request");
                        System.out.println("Name: " + br.readLine());
                        System.out.println("Description: " + br.readLine());
                        System.out.println("Picture: " + br.readLine());

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                case "5" -> {
                    System.out.println("Enter receiver username: ");
                    String receiver = sc.nextLine();
                    try {
                        bw.write("Check Block/Friends: " + receiver + "\n");
                        bw.flush();

                        String choice = br.readLine();
                        if(choice.equals("Can message")) {
                            System.out.println("Entered DM");
                        } else {
                            System.out.println(choice);
                            break;
                        }

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }


                    Thread read = new Thread(new ReadMessageThread(br));
                    read.start();

                    do {
                        System.out.println("What message type is this (Image/Text)? ");
                        String type = sc.nextLine();
                        while(true) {
                            if(!type.equalsIgnoreCase("Image") && !type.equalsIgnoreCase("Text")) {
                                System.out.println("Invalid message type, please try again");
                                type = sc.nextLine();
                            } else {
                                break;
                            }
                        }

                        //TODO HERE IF THE USER AT ANY POINT TYPES "DELETE" DELETE THE LAST MESSAGE THEY SENT
                        System.out.println("Enter your message: (to quit messaging at any point, type \"quit\") (to delete the last message you sent, type \"DELETE\")");
                        String message = sc.nextLine();
                        try {
                            if(message.equals("quit") || type.equals("quit")) {
                                bw.write("quit\n");
                                bw.flush();
                                break;
                            } else if(message.equals("DELETE") || type.equals("DELETE")) {
                                System.out.println("What is the message text of the message you want to delete");
                                message = sc.nextLine();
                                bw.write("DELETE" + receiver + "-" + message + "-" + type + "\n");
                                String result = br.readLine();
                                if(result.equals("Successful Delete Message")) {
                                    System.out.println("Message Was Deleted");
                                } else if(result.equals("This Messages Does Not Exist")) {
                                    System.out.println("This Message Doesnt Exist");
                                }
                            }else {
                                bw.write("Message: " + receiver + "-" + message + "-" + type + "-" + displayMessageHistoryCounter + "\n");
                                bw.flush();
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    } while(true);
                    displayMessageHistoryCounter++;
                    read.interrupt();
                    actionsAfterLogin(bw, br, sc);
                }

                case "6" -> {
                    System.out.print("Enter username to block: ");
                    String blockUser = sc.nextLine();
                    try {
                        bw.write("Block User: " + blockUser + "\n");
                        bw.flush();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    System.out.println(blockUser + " has been blocked.");
                }

                case "7" -> {
                    System.out.print("Enter username to unblock: ");
                    String unblockUser = sc.nextLine();
                    try {
                        bw.write("Unblock User: " + unblockUser + "\n");
                        bw.flush();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println(unblockUser + " has been unblocked.");
                }

                case "8" -> {
                    try {
                        bw.write("Blocked Users: " + "\n");
                        bw.flush();
                        System.out.println("Blocked Users: " + br.readLine());

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                case "9" -> {
                    System.out.print("Enter username to add as friend: ");
                    String newFriend = sc.nextLine();
                    try {
                        bw.write("Add Friend: " + newFriend + "\n");
                        bw.flush();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println(newFriend + " added to friends.");
                }

                case "10" -> {
                    System.out.print("Enter username to remove from friends: ");
                    String oldFriend = sc.nextLine();
                    try {
                        bw.write("Remove Friend: " + oldFriend + "\n");
                        bw.flush();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println(oldFriend + " removed from friends.");
                }

                case "11" -> {
                    try {
                        bw.write("Friend List: " + "\n");
                        bw.flush();
                        System.out.println("Friend List: " + br.readLine());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                case "12" -> {
                    System.out.println("Do you want to receive messages from anyone (yes/no)");
                    String choice = sc.nextLine();
                    boolean yesNo = choice.equalsIgnoreCase("yes");
                    try {
                        bw.write("Change Can Receive Anyone: " + yesNo + "\n");
                        bw.flush();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("Changed Can Receive Anyone to: " + yesNo + "\n");
                }

                case "13" -> {
                    try {
                        bw.write("Exit");
                        bw.flush();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("Logging out...");
                    exit = true;
                    break;
                }

                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }


    public static void main(String args[]) throws IOException {
        Scanner sc = new Scanner(System.in);
        ApplicationClient client = new ApplicationClient();
        System.out.println("Welcome to the Social Media Application (Phase 1)");

        String userMenu = "User Menu";
        String CHOICE_1 = "1. CREATE A NEW ACCOUNT";
        String CHOICE_2 = "2. LOGIN WITH EXISTING ACCOUNT";
        String CHOICE_3 = "3. EXIT";

        String choice = "";

        Socket socket = new Socket("localhost", 4242);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        boolean validChoice = false;
        do {
            System.out.println(userMenu);
            System.out.println(CHOICE_1);
            System.out.println(CHOICE_2);
            System.out.println(CHOICE_3);
            choice = sc.nextLine();
            if ("123".contains(choice)) {
                validChoice = true;
            }
        } while (!validChoice);

        switch (choice) {
            case "1" -> {
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
                    }

                    bw.write("Username Create: " + user + "\n");
                    bw.flush();

                    if(br.readLine().equals("New User")) {
                        validUser = true;
                    } else {
                        System.out.println("Username is in use, select a new username");
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

                bw.write("Password Create: " + pass + "\n");
                bw.flush();

                System.out.println("Created Login!");
                client.actionsAfterLogin(bw, br, sc);
            }

            case "2" -> {
                System.out.println("Enter the username: ");
                String user = sc.nextLine().trim();
                bw.write("Username Login: " + user + "\n");
                bw.flush();
                boolean tryAgain = true;
                while(tryAgain) {
                    System.out.println("Enter the password: ");
                    String pass = sc.nextLine().trim();
                    bw.write("Password Login: " + pass + "\n");
                    bw.flush();
                    String line = br.readLine();
                    if(line.equals("Logged In!") || line.equals("Created New User!")) {
                        if(line.equals("Logged In!")) {
                            System.out.println("Welcome Old User");
                        } else {
                            System.out.println("Welcome New User");
                        }
                        tryAgain = false;
                    } else if(line.equals("Wrong Password")) {
                        System.out.println("Wrong password try again");
                    }
                }

                client.actionsAfterLogin(bw, br, sc);
            }

            case "3" -> {
                bw.write("Exit" + "\n");
                bw.flush();
                System.out.println("Exiting the Menu!");
            }

            default -> {
                System.out.println("Invalid Input!");
            }
        }

        System.out.println("Thank you for using The Social Media Application (Phase 2)!");

    }
}