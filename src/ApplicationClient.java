import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ApplicationClient implements ApplicationInterface {
    private static final int SERVER_PORT = 4242;

    private static void actionsAfterLogin(User currentUser, Scanner sc, PrintWriter writer, BufferedReader reader) {
        boolean exit = false;
        while (!exit) {
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

        do {
            System.out.print("Enter username (no '-' allowed, cannot be empty): ");
            user = sc.nextLine().trim();
            validUser = !user.contains("-") && !user.isEmpty();

            if (!validUser) {
                System.out.println("Invalid username! Try again.");
            }
        } while (!validUser);

        do {
            System.out.print("Enter password (no '-' allowed, cannot be empty): ");
            pass = sc.nextLine().trim();
            validUser = !pass.contains("-") && !pass.isEmpty();

            if (!validUser) {
                System.out.println("Invalid password! Try again.");
            }
        } while (!validUser);

        writer.println("CREATE_ACCOUNT");
        writer.println(user);
        writer.println(pass);

        String response = reader.readLine();
        if ("SUCCESS".equals(response)) {
            System.out.println("Account created successfully.");
            return new User(user, pass);
        } else {
            System.out.println("Failed to create account: " + response);
            return null;
        }
    }

    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in);
             Socket socket = new Socket("localhost", SERVER_PORT);
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            System.out.println("Welcome to the Social Media Application (Phase 1)");

            boolean validChoice;
            String choice;
            do {
                System.out.println("User Menu");
                System.out.println("1. CREATE A NEW ACCOUNT");
                System.out.println("2. LOGIN WITH EXISTING ACCOUNT");
                System.out.println("3. EXIT");

                choice = sc.nextLine().trim();
                validChoice = "123".contains(choice) && choice.length() == 1;

                if (!validChoice) {
                    System.out.println("Invalid option. Please try again.");
                }
            } while (!validChoice);

            switch (choice) {
                case "1" -> {
                    User newUser = createUserMain(sc, writer, reader);
                    if (newUser != null) {
                        System.out.println("Account created! You can now log in.");
                    }
                }

                case "2" -> {
                    System.out.print("Enter username: ");
                    String user = sc.nextLine().trim();
                    System.out.print("Enter password: ");
                    String pass = sc.nextLine().trim();

                    writer.println("LOGIN");
                    writer.println(user);
                    writer.println(pass);

                    String loginResponse = reader.readLine();
                    if ("SUCCESS".equals(loginResponse)) {
                        User currentUser = new User(user, pass);
                        actionsAfterLogin(currentUser, sc, writer, reader);
                    } else {
                        System.out.println("Login failed: " + loginResponse);
                    }
                }

                case "3" -> System.out.println("Exiting the application.");
                default -> System.out.println("Invalid input.");
            }
            System.out.println("Thank you for using The Social Media Application (Phase 2)!");

        } catch (IOException e) {
            System.err.println("Error connecting to server: " + e.getMessage());
        }
    }
}