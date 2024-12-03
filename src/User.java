import java.io.*;

// Example usernamesAndPasswords.txt
// username-password
// username-password
// username-password

// Example "username".txt
// username-name-profileDescription-profilePicture-receiveAnyone
// blockedUsername1-blockedUsername2-blockedUsername3
// friendUsername1-friendUsername2-friendUsername3

// Example "firstUsername-secondUsername".txt
// senderUserame-receiverUsername-message
// these are 2 people dm's and firstUsername-secondUsername are in lexographic order


// Change friends adding to force other user to accept or reject
// Change password = password in user for existing file to check if the passwords r equal and follow through

/**
 * Group PJ -- User
 * <p>
 * This is the User class
 * (See the ReadMe for more details)
 *
 * @author Pranav Neti, Ryan Sierra, Tanya Jain, Anay Misra - Lab Section 12
 * @version Nov 17, 2024
 */

public class User implements UserInterface {
    // profile information
    private String name;
    private String username;
    private String password;
    private String profileDescription;
    private String profilePicture;
    private String userFileName;
    private boolean receiveAnyone;
    private static Object mainLock = new Object();

    public User(String username, String password) {
        try (BufferedReader br = new BufferedReader(new FileReader("files/usernamesAndPasswords.txt"))) {
            String line = br.readLine();
            if (line == null || line.isEmpty()) {
                createUser(username, password);
                return;
            } else {
                while (line != null) {
                    String[] vars = line.split("-");
                    if (vars[0].equals(username) && vars[1].equals(password)) {
                        try (BufferedReader br2 = new BufferedReader(new FileReader("files/" + username + ".txt"))) {
                            System.out.println("past LINE \n \n \n");
                            String personalIdentifiers = br2.readLine();
                            String[] personalArr = personalIdentifiers.split("-");
                            this.username = personalArr[0];
                            this.name = personalArr[1];
                            this.profileDescription = personalArr[2];
                            this.profilePicture = personalArr[3];
                            this.receiveAnyone = Boolean.parseBoolean(personalArr[4]);
                            this.userFileName = this.username + ".txt";
                            this.password = password;
                            return;
                        } catch (IOException e) {
                            createUser(username, password);
                            return;
                        }
                    } else if (vars[0].equals(username)) {
                        this.username = null;
                    } else {
                        line = br.readLine();
                    }
                }
            }

            createUser(username, password);
            return;

        } catch (IOException e) {
            // e.printStackTrace();
        }
    }

    public void createUser(String username1, String password1) {
        this.username = username1;
        this.password = password1;
        this.name = " ";
        this.profileDescription = " ";
        this.profilePicture = " ";
        this.receiveAnyone = false;
        this.userFileName = username1 + ".txt";

        try (PrintWriter pw2 = new PrintWriter(new FileWriter(new File("files/usernamesAndPasswords.txt"), true))) {
            PrintWriter pw = new PrintWriter(new FileWriter(new File("files/" + this.userFileName), true));

            pw.println(username1 + "-" + name + "-" + profileDescription + "-" +
                    profilePicture + "-" + receiveAnyone); // personal identifiers
            pw.println(); // blocked users
            pw.println(); // friends

            pw2.println(username1 + "-" + password);
            pw.flush();
            pw.close();
        } catch (IOException e) {
            // e.printStackTrace();
        }
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        try (BufferedReader br = new BufferedReader(new FileReader("files/" + this.userFileName))) {
            String line1 = br.readLine();
            String line2 = br.readLine();
            String line3 = br.readLine();
            String[] params = line1.split("-");
            params[1] = name;
            line1 = "";
            for (String s : params) {
                line1 += s + "-";
            }
            line1 = line1.substring(0, line1.length() - 1);

            PrintWriter pw = new PrintWriter("files/" + this.userFileName);
            pw.println(line1);
            pw.println(line2);
            pw.println(line3);
            pw.close();

            this.name = name;
        } catch (IOException e) {
            // e.printStackTrace();
        }
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setProfileDescription(String profileDescription) {
        try (BufferedReader br = new BufferedReader(new FileReader("files/" + this.userFileName))) {
            String line1 = br.readLine();

            String line2 = br.readLine();
            String line3 = br.readLine();

            String[] params = line1.split("-");
            params[2] = profileDescription;
            line1 = "";
            for (String s : params) {
                line1 += s + "-";
            }
            line1 = line1.substring(0, line1.length() - 1);


            BufferedWriter bw = new BufferedWriter(new FileWriter(new File("files/" + this.userFileName), false));
            bw.write(line1 + "\n");
            bw.write(line2 + "\n");
            bw.write(line3 + "\n");
            bw.flush();
            bw.close();
            this.profileDescription = profileDescription;
        } catch (IOException e) {
            // e.printStackTrace();
        }
    }

    public String getProfileDescription() {
        return this.profileDescription;
    }

    public String getProfilePicture() {
        return this.profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        try (BufferedReader br = new BufferedReader(new FileReader("files/" + this.userFileName))) {
            String line1 = br.readLine();
            String line2 = br.readLine();
            String line3 = br.readLine();

            String[] params = line1.split("-");
            params[3] = profilePicture;
            line1 = "";
            for (String s : params) {
                line1 += s + "-";
            }
            line1 = line1.substring(0, line1.length() - 1);


            BufferedWriter bw = new BufferedWriter(new FileWriter(new File("files/" + this.userFileName), false));
            bw.write(line1 + "\n");
            bw.write(line2 + "\n");
            bw.write(line3 + "\n");
            bw.flush();
            bw.close();
            this.profilePicture = profilePicture;
        } catch (IOException e) {
            // e.printStackTrace();
        }
    }

    public boolean getReceiveAnyone() {
        return this.receiveAnyone;
    }

    public void setReceiveAnyone(boolean receiveAnyone) {
        try (BufferedReader br = new BufferedReader(new FileReader("files/" + this.userFileName))) {
            String line1 = br.readLine();
            String line2 = br.readLine();
            String line3 = br.readLine();

            String[] params = line1.split("-");
            params[4] = Boolean.toString(receiveAnyone);
            line1 = "";
            for (String s : params) {
                line1 += s + "-";
            }
            line1 = line1.substring(0, line1.length() - 1);

            BufferedWriter bw = new BufferedWriter(new FileWriter(new File("files/" + this.userFileName), false));
            bw.write(line1 + "\n");
            bw.write(line2 + "\n");
            bw.write(line3 + "\n");
            bw.flush();
            bw.close();
            this.receiveAnyone = receiveAnyone;
        } catch (IOException e) {
            // e.printStackTrace();
        }
    }

    public boolean canReceiveFrom(String senderUsername) {
        if (!checkUserExists(senderUsername)) {
            return false;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(new File("files/" + this.userFileName)))) {
            String firstLine = br.readLine();
            br.readLine();
            String friends = br.readLine();
            return friends.contains(senderUsername) || firstLine.endsWith("true");
        } catch (IOException e) {
            // e.printStackTrace();
            return false;
        }
    }

    public boolean sendMessage(Message message, String reciver) {
        synchronized (mainLock) {
            System.out.println("YUH");
            System.out.println(message.getMessageText());
            System.out.println(message.getType());
            System.out.println(message.getMainUser());

            if (!checkUserExists(reciver)) {
                return false;
            }
            if (this.canReceiveFrom(reciver)) {
                System.out.print("We are writing");
                String first = (this.username.compareTo(reciver) > 0 ? reciver : this.username);
                String second = (this.username.equals(first) ? reciver : this.username);
                try (PrintWriter pw = new PrintWriter(new FileWriter(new File("files/"
                        + first + "-" + second + ".txt"), true))) {
                    pw.println(this.username + "-" + reciver + "-" + message.getMessageText());
                } catch (IOException e) {
                    // e.printStackTrace();
                }
            } else {
                return false;
            }

            return true;
        }
    }

    public boolean blockUser(String blockedUser) {
        if (!checkUserExists(blockedUser)) {
            return false;
        }
        try (BufferedReader br = new BufferedReader(new FileReader("files/" + this.userFileName))) {
            String line1 = br.readLine();
            String line2 = br.readLine();
            if (line2 == null || line2.isEmpty()) {
                line2 = blockedUser;
            } else if (line2.contains(blockedUser)) {
                return true;
            } else {
                line2 += "-" + blockedUser;
            }
            String line3 = br.readLine();
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File("files/" + this.userFileName), false));
            bw.write(line1 + "\n");
            bw.write(line2 + "\n");
            bw.write(line3 + "\n");
            bw.close();
            return true;
        } catch (IOException e) {
            // e.printStackTrace();
            return false;
        }
    }

    public boolean isBlocked(String thisUsername, String otherUsername) {
        if (!checkUserExists(thisUsername)) {
            return false;
        }
        if (!checkUserExists(otherUsername)) {
            return false;
        }

        try (BufferedReader thisUsernameBR = new BufferedReader(new FileReader("files/" + thisUsername + ".txt"));
             BufferedReader otherUsernameBR = new BufferedReader(new FileReader("files/" + otherUsername + ".txt"))) {
            thisUsernameBR.readLine();
            String thisLine2 = thisUsernameBR.readLine();
            otherUsernameBR.readLine();
            String otherLine2 = otherUsernameBR.readLine();
            
            return (thisLine2.contains(otherUsername) || otherLine2.contains(thisUsername));

        } catch (IOException e) {
            //e.printStackTrace();
            return false;
        }

    }

    public boolean deleteMessage(String reciver, Message message) {
        synchronized (mainLock) {
            if (!checkUserExists(reciver)) {
                return false;
            }
            String first = "";
            String second = "";

            if (reciver.compareTo(username) < 0) {
                first = reciver;
                second = this.username;
            } else if (reciver.compareTo(username) > 0) {
                second = reciver;
                first = this.username;
            }

            File inputFile = new File("files/" + first + "-" + second + ".txt");

            try (BufferedReader br = new BufferedReader(new FileReader("files/" + first + "-" + second + ".txt"))) {
                String finalString = "";
                String line = "";

                while ((line = br.readLine()) != null) {
                    String[] parts = line.split("-");
                    if (parts[2].equals(message.getMessageText())) {
                        continue;
                    }
                    finalString += line + "\n";
                }
                System.out.println(finalString + "THIS IS FINAL");
                BufferedWriter bw = new BufferedWriter(new FileWriter(inputFile));
                bw.write(finalString);
                bw.close();

                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

        }

    }

    public String getBlockedUsers() {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader("files/" + this.userFileName))) {
            br.readLine();
            String blockedUsers = br.readLine();
            for (String s : blockedUsers.split("-")) {
                sb.append(s).append(" ");
            }

            return sb.toString();
        } catch (IOException e) {
            // e.printStackTrace();
            return "something broke";
        }
    }


    public String getFriends() {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader("files/" + this.userFileName))) {
            br.readLine();
            br.readLine();
            String friends = br.readLine();
            for (String s : friends.split("-")) {
                sb.append(s).append(" ");
            }

            return sb.toString();
        } catch (IOException e) {
            // e.printStackTrace();
            return "something broke";
        }
    }

    public boolean unblockUser(String previouslyBlockedUser) {
        if (!checkUserExists(previouslyBlockedUser)) {
            return false;
        }
        if(previouslyBlockedUser.equals(username)) {
            return false;
        }
        try (BufferedReader br = new BufferedReader(new FileReader("files/" + this.userFileName))) {
            String line1 = br.readLine();
            String line2 = br.readLine();
            if(line2.contains(previouslyBlockedUser)) {
                int startInd = line2.indexOf(previouslyBlockedUser);
                int endInd = line2.indexOf(previouslyBlockedUser) + previouslyBlockedUser.length();
                if (startInd == 0 && endInd == line2.length()) {
                    line2 = "";
                } else if (startInd == 0) {
                    line2 = line2.substring(0, startInd) + line2.substring(endInd + 1);
                } else if (endInd == line2.length()) {
                    line2 = line2.substring(0, startInd - 1) + line2.substring(endInd);
                } else {
                    line2 = line2.substring(0, startInd - 1) + line2.substring(endInd);
                }
                String line3 = br.readLine();

                BufferedWriter bw = new BufferedWriter(new FileWriter(new File("files/" + this.userFileName), false));
                bw.write(line1 + "\n");
                bw.write(line2 + "\n");
                bw.write(line3 + "\n");
                bw.close();
                return true;
            } else {
                return false;
            }

        } catch (IOException e) {
            // e.printStackTrace();
            return false;
        }
    }

    public String readMessages(String receiver) {
        String first = "";
        String second = "";
        StringBuilder sb = new StringBuilder();
        if (receiver.compareTo(username) < 0) {
            first = receiver;
            second = this.username;
        } else if (receiver.compareTo(username) > 0) {
            second = receiver;
            first = this.username;
        }
        try (BufferedReader br = new BufferedReader(new FileReader("files/" + first + "-" + second + ".txt"))) {
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }

        } catch (IOException e) {
            // e.printStackTrace();
        }
        return sb.toString();
    }

    public String findMostRecentMessages(String otherUsername) {
        String messageHistory = readMessages(otherUsername);
        String[] messages = messageHistory.split("\n");
        StringBuilder result = new StringBuilder();
        for (int i = messages.length - 1; i >= 0; i--) {
            if (messages[i].startsWith(this.username) || i == 0) {
                for (int j = i; j < messages.length; j++) {
                    result.append(messages[j]).append("\n");
                }
            }
        }

        return result.toString();
    }


    public String findUser(String username1) {
        File f = new File("files/" + username1 + ".txt");
        if (f.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader("files/" + username1 + ".txt"))) {
                return br.readLine();
            } catch (IOException e) {
                return "User not found " + e.getMessage();
            }
        } else {
            return "User not found";
        }
    }

    public boolean addFriend(String newFriend) {
        System.out.println("files/" + this.userFileName);
        if (!checkUserExists(newFriend)) {
            System.out.println("THINK USER DONT EXIST");
            return false;
        }
        try (BufferedReader br = new BufferedReader(new FileReader("files/" + this.userFileName))) {
            String line1 = br.readLine();
            String line2 = br.readLine();
            String line3 = br.readLine();
            if (line3 == null || line3.isEmpty()) {
                line3 = newFriend;
            } else if (line3.contains(newFriend)) {
                return true;
            } else {
                line3 += "-" + newFriend;
            }

            BufferedWriter bw = new BufferedWriter(new FileWriter(new File("files/" + this.userFileName), false));
            bw.write(line1 + "\n");
            bw.write(line2 + "\n");
            bw.write(line3 + "\n");
            bw.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    public boolean removeFriend(String oldFriend) {
        System.out.println("In the user class");
        if (!checkUserExists(oldFriend)) {
            System.out.println("Shouldnt happpen");
            return false;
        }
        try (BufferedReader br = new BufferedReader(new FileReader("files/" + this.userFileName))) {
            System.out.println("Redaing into the file for remove");
            String line1 = br.readLine();
            String line2 = br.readLine();
            String line3 = br.readLine();
            if(!line3.contains(oldFriend)) {
                return false;
            } else {
                System.out.println("Found the else");
                String newLine3 = "";
                if (line3.contains("-")) {
                    String[] line3Part = line3.split("-");
                    System.out.println("this shouldn't happen");
                    for (String parts : line3Part) {
                        if (parts.equals(oldFriend)) {
                            continue;
                        }
                        newLine3 += parts + "-";
                    }
                }
                System.out.println("Finding File");
                BufferedWriter bw = new BufferedWriter(new FileWriter(new File("files/" + this.userFileName), false));
                bw.write(line1 + "\n");
                bw.write(line2 + "\n");
                bw.write(newLine3 + "\n");
                bw.close();
                System.out.println("Ending editiing the file");
                return true;
            }
        } catch (IOException e) {
            // e.printStackTrace();
            return false;
        }
    }

    public boolean checkUserExists(String username1) {
        File f = new File("files/" + username1 + ".txt");
        return f.exists();
    }

    public static boolean checkCanReceiveAnyone(String otherUsername) {
        try (BufferedReader br = new BufferedReader(new FileReader("files/" + otherUsername + ".txt"))) {
            return br.readLine().endsWith("true");
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean checkIsFriend(String otherUsername, String thisUsername) {
        try (BufferedReader br = new BufferedReader(new FileReader("files/" + otherUsername + ".txt"))) {
            br.readLine();
            br.readLine();
            return br.readLine().contains(thisUsername);
        } catch (Exception e) {
            return false;
        }
    }

    public static String[] otherUserProfile(String otherUsername) {
        boolean condition = false;
        String[] result = new String[3];
        try (BufferedReader br = new BufferedReader(new FileReader("files/usernamesAndPasswords.txt"))) {
            String message = "";
            while ((message = br.readLine()) != null) {
                String[] parts = message.split("-");
                if (parts[0].equals(otherUsername)) {
                    condition = true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!condition) {
            return new String[]{""};
        } else {
            try (BufferedReader br2 = new BufferedReader(new FileReader("files/" + otherUsername + ".txt"))) {
                System.out.println("files/" + otherUsername + ".txt");
                String[] parts = br2.readLine().split("-");
                System.out.println(parts.length);
                result[0] = parts[1];
                result[1] = parts[2];
                result[2] = parts[3];
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }


}