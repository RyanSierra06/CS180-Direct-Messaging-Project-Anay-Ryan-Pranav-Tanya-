import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import java.io.File;
import java.io.FileReader;


public class User {
    // profile information
    String name;
    String userName;
    String password;
    String profileDescription;
    String profilePicture;
    String userFileName;

    public User(String userName, String password) {
        try(BufferedReader br = new BufferedReader(new FileReader("userNameAndPasswords.txt"))) {
            String line = br.readLine();
            while(line != null) {
                String[] vars = line.split(" ");
                if(vars[0].equals(userName)) {
                    try(BufferedReader br2 = new BufferedReader(new FileReader(userName + ".txt"))) {
                        String personalIdentifiers = br2.readLine();
                        String[] personalArr = personalIdentifiers.split("-");
                        this.userName = personalArr[0];
                        this.name = personalArr[1];
                        this.profileDescription = personalArr[2];
                        this.profilePicture = personalArr[3];
                        return;
                    } catch(IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            createUser(userName, password);

        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void createUser(String userName, String password) {
        this.userName = userName;
        this.password = password;
        this.name = null;
        this.profileDescription = null;
        this.profilePicture = null;
        this.userFileName = userName + ".txt";
        try(PrintWriter pw = new PrintWriter(new FileWriter(new File(this.userFileName)));
            PrintWriter pw2 = new PrintWriter(new FileWriter(new File("userNameAndPasswords.txt"), true))) {
            pw.println(userName + "-" + name + "-" + profileDescription + "-" + profilePicture); // personal identifiers
            pw.println(); // blocked users
            pw.println(); // friends
            pw2.println(userName + " " + password);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setProfileDescription(String profileDescription) {
        this.profileDescription = profileDescription;
    }

    public String getProfileDescription() {
        return this.profileDescription;
    }

    public String getProfilePicture() {
        return this.profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public void sendMessage(Message message, String reciver) {
        String first = (this.userName.compareTo(reciver) < 0 ? reciver : this.userName);
        String second = (this.userName.equals(first) ? reciver : this.userName);
        try(PrintWriter pw = new PrintWriter(new FileWriter(new File(first + "-" + second + ".txt"), true))) {
            pw.println(this.userName + "-" + reciver + "-" + message);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void blockUser(String blockedUser) {
        try(BufferedReader br = new BufferedReader(new FileReader(this.userFileName));
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(this.userFileName), false))) {
            String line1 = br.readLine();
            String line2 = br.readLine() + "-" + blockedUser;
            String line3 = br.readLine();
            bw.write(line1 + "\n");
            bw.write(line2 + "\n");
            bw.write(line3 + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteMessage(String reciver, Message message) {
        String first = "";
        String second = "";
        File inputFile = new File(first + "-" + second + ".txt");
        File tempFile = new File("tempFile.txt");

        if(reciver.compareTo(userName) < 0) {
            first = reciver;
            second = this.userName;
        } else if(reciver.compareTo(userName) > 0) {
            second = reciver;
            first = this.userName;
        }

        try {
            BufferedReader br = new BufferedReader(new FileReader(first + "-" + second + ".txt"));
            BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile));
            String line = "";
            while((line = br.readLine()) != null) {
                bw.write(line);
                if(line.equals(message)) {
                   continue;
                }
            }
            if (inputFile.delete()) {
                tempFile.renameTo(inputFile);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String getBlockedUsers() {
        StringBuilder sb = new StringBuilder();
        try(BufferedReader br = new BufferedReader(new FileReader(this.userFileName))) {
            br.readLine();
            String blockedUsers = br.readLine();
            for(String s : blockedUsers.split("-")) {
                sb.append(s);
            }

            return sb.toString();
        } catch(IOException e) {
            e.printStackTrace();
            return "something broke";
        }
    }

    public String getFriends() {
        StringBuilder sb = new StringBuilder();
        try(BufferedReader br = new BufferedReader(new FileReader(this.userFileName))) {
            br.readLine();
            br.readLine();
            String friends = br.readLine();
            for(String s : friends.split("-")) {
                sb.append(s);
            }

            return sb.toString();
        } catch(IOException e) {
            e.printStackTrace();
            return "something broke";
        }
    }

    public void unblockUser(String previouslyBlockedUser) {
        try(BufferedReader br = new BufferedReader(new FileReader(this.userFileName));
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(this.userFileName), false))) {
            String line1 = br.readLine();
            String line2 = br.readLine();
            int startInd = line2.indexOf(previouslyBlockedUser);
            int endInd = line2.indexOf(previouslyBlockedUser) + previouslyBlockedUser.length();
            if(startInd == 0 && endInd == line2.length()) {
                line2 = "";
            } else if(startInd == 0) {
                line2 = line2.substring(0, startInd) + line2.substring(endInd + 1);            
            } else if(endInd == line2.length()) {
                line2 = line2.substring(0, startInd - 1) + line2.substring(endInd);            
            } else {
                line2 = line2.substring(0, startInd - 1) + line2.substring(endInd);
            }
            String line3 = br.readLine();

            bw.write(line1 + "\n");
            bw.write(line2 + "\n");
            bw.write(line3 + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public String readMessages(String receiver) {
        //go through the file and just print the messages sent per each person 
        //we probably need some implementation to print them in the correct order
        String first = "";
        String second = "";
        StringBuilder sb = new StringBuilder();
        if(receiver.compareTo(userName) < 0) {
            first = receiver;
            second = this.userName;
        } else if(receiver.compareTo(userName) > 0) {
            second = receiver;
            first = this.userName;
        }
        try {
            BufferedReader br = new BufferedReader(new FileReader(first + "-" + second + ".txt"));
            String line = "";
            while((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public String findUser(String userName) {
        File f = new File(userName + ".txt");
        if(f.exists()) {
            try(BufferedReader br = new BufferedReader(new FileReader(userName + ".txt"))) {
                return br.readLine();
            } catch(IOException e) {
                return "User not found - exception";
            }
        } else {
            return "User not found";
        }
    }

    public void addFriend(String newFriend) {
        // TODO finish this method
        // friends.add(newFriend);
    }
    public void removeFriend(String oldFriend) {
        // TODO finish this method
        // friends.remove(oldFriend);
    }
    public void modifyMessageRecivingLimit() {
        // if you can see messages from anybody or just messages from friends
    }

}