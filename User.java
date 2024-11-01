import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileReader;

// Example usernameAndPasswords.txt
// username-password
// username-password
// username-password

// Example "username".txt
// username-name-profileDescription-profilePicture-reciveAnyone
// blockedUsername1-blockedUsername2-blockedUsername3
// friendUsername1-friendUsername2-friendUsername3

// Example "firstUsername-secondUsername".txt
// senderUserame-reciverUsername-message 
// these are 2 people dm's and firstUsername-secondUsername are in lexographic order


public class User implements UserInterface{
    // profile information
    String name;
    String username;
    String password;
    String profileDescription;
    String profilePicture;
    String userFileName;
    boolean reciveAnyone; 

    public User(String username, String password) {
        try(BufferedReader br = new BufferedReader(new FileReader("usernameAndPasswords.txt"))) {
            String line = br.readLine();
            while(line != null) {
                String[] vars = line.split("-");
                if(vars[0].equals(username)) {
                    try(BufferedReader br2 = new BufferedReader(new FileReader(username + ".txt"))) {
                        String personalIdentifiers = br2.readLine();
                        String[] personalArr = personalIdentifiers.split("-");
                        this.username = personalArr[0];
                        this.name = personalArr[1];
                        this.profileDescription = personalArr[2];
                        this.profilePicture = personalArr[3];
                        this.reciveAnyone = Boolean.parseBoolean(personalArr[4]);
                        return;
                    } catch(IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            createUser(username, password);

        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void createUser(String username, String password) {
        this.username = username;
        this.password = password;
        this.name = null;
        this.profileDescription = null;
        this.profilePicture = null;
        this.reciveAnyone = false;
        this.userFileName = username + ".txt";
        try(PrintWriter pw = new PrintWriter(new FileWriter(new File(this.userFileName)));
            PrintWriter pw2 = new PrintWriter(new FileWriter(new File("usernameAndPasswords.txt"), true))) {
            pw.println(username + "-" + name + "-" + profileDescription + "-" + profilePicture + "-" + reciveAnyone); // personal identifiers
            pw.println(); // blocked users
            pw.println(); // friends
            pw2.println(username + "-" + password);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        try(BufferedReader br = new BufferedReader(new FileReader(this.userFileName));
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(this.userFileName), false))) {
            String line1 = br.readLine();
            String line2 = br.readLine();
            String line3 = br.readLine();

            String[] params = line1.split("-");
            params[1] = name;
            line1 = "";
            for(String s : params) {
                line1 += s + "-";
            }
            line1 = line1.substring(0, line1.length()-1);

            bw.write(line1 + "\n");
            bw.write(line2 + "\n");
            bw.write(line3 + "\n");
            this.name = name;
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setProfileDescription(String profileDescription) {
        try(BufferedReader br = new BufferedReader(new FileReader(this.userFileName));
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(this.userFileName), false))) {
            String line1 = br.readLine();
            String line2 = br.readLine();
            String line3 = br.readLine();

            String[] params = line1.split("-");
            params[2] = profileDescription;
            line1 = "";
            for(String s : params) {
                line1 += s + "-";
            }
            line1 = line1.substring(0, line1.length()-1);

            bw.write(line1 + "\n");
            bw.write(line2 + "\n");
            bw.write(line3 + "\n");
            this.profileDescription = profileDescription;
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public String getProfileDescription() {
        return this.profileDescription;
    }

    public String getProfilePicture() {
        return this.profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        try(BufferedReader br = new BufferedReader(new FileReader(this.userFileName));
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(this.userFileName), false))) {
            String line1 = br.readLine();
            String line2 = br.readLine();
            String line3 = br.readLine();

            String[] params = line1.split("-");
            params[3] = profilePicture;
            line1 = "";
            for(String s : params) {
                line1 += s + "-";
            }
            line1 = line1.substring(0, line1.length()-1);

            bw.write(line1 + "\n");
            bw.write(line2 + "\n");
            bw.write(line3 + "\n");
            this.profilePicture = profilePicture;
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public boolean getReciveAnyone() {
        return this.reciveAnyone;
    }

    public void setReciveAnyone(boolean reciveAnyone) {
        try(BufferedReader br = new BufferedReader(new FileReader(this.userFileName));
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(this.userFileName), false))) {
            String line1 = br.readLine();
            String line2 = br.readLine();
            String line3 = br.readLine();

            String[] params = line1.split("-");
            params[4] = Boolean.toString(reciveAnyone);
            line1 = "";
            for(String s : params) {
                line1 += s + "-";
            }
            line1 = line1.substring(0, line1.length()-1);

            bw.write(line1 + "\n");
            bw.write(line2 + "\n");
            bw.write(line3 + "\n");
            this.reciveAnyone = reciveAnyone;
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public boolean canReciveFrom(String senderUsername) {
        try(BufferedReader br = new BufferedReader(new FileReader(new File(this.userFileName)))) {
            br.readLine();
            br.readLine();
            String friends = br.readLine();
            if(friends.contains(senderUsername)) {
                return true;
            } else if(this.reciveAnyone) {
                return true;
            } else {
                return false;
            }

        } catch(IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void sendMessage(Message message, String reciver) {
        String first = (this.username.compareTo(reciver) < 0 ? reciver : this.username);
        String second = (this.username.equals(first) ? reciver : this.username);
        try(PrintWriter pw = new PrintWriter(new FileWriter(new File(first + "-" + second + ".txt"), true))) {
            pw.println(this.username + "-" + reciver + "-" + message);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void blockUser(String blockedUser) {
        try(BufferedReader br = new BufferedReader(new FileReader(this.userFileName));
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(this.userFileName), false))) {
            String line1 = br.readLine();
            String line2 = br.readLine();
            if(line2 == null || line2.isEmpty()) {
                line2 = blockedUser;
            } else {
                line2 += "-" + blockedUser;
            }
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

        if(reciver.compareTo(username) < 0) {
            first = reciver;
            second = this.username;
        } else if(reciver.compareTo(username) > 0) {
            second = reciver;
            first = this.username;
        }

        File inputFile = new File(first + "-" + second + ".txt");
        File tempFile = new File("tempFile.txt");

        try(BufferedReader br = new BufferedReader(new FileReader(first + "-" + second + ".txt"));
            BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))) {
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
        String first = "";
        String second = "";
        StringBuilder sb = new StringBuilder();
        if(receiver.compareTo(username) < 0) {
            first = receiver;
            second = this.username;
        } else if(receiver.compareTo(username) > 0) {
            second = receiver;
            first = this.username;
        }
        try(BufferedReader br = new BufferedReader(new FileReader(first + "-" + second + ".txt"))) {
            String line = "";
            while((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public String findUser(String username) {
        File f = new File(username + ".txt");
        if(f.exists()) {
            try(BufferedReader br = new BufferedReader(new FileReader(username + ".txt"))) {
                return br.readLine();
            } catch(IOException e) {
                return "User not found " + e.getMessage();
            }
        } else {
            return "User not found";
        }
    }

    public void addFriend(String newFriend) {
        try(BufferedReader br = new BufferedReader(new FileReader(this.userFileName));
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(this.userFileName), false))) {
            String line1 = br.readLine();
            String line2 = br.readLine();
            String line3 = br.readLine();
            if(line3 == null || line3.isEmpty()) {
                line3 = newFriend;
            } else {
                line3 += "-" + newFriend;
            }
            bw.write(line1 + "\n");
            bw.write(line2 + "\n");
            bw.write(line3 + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
    public void removeFriend(String oldFriend) {
        try(BufferedReader br = new BufferedReader(new FileReader(this.userFileName));
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(this.userFileName), false))) {
            String line1 = br.readLine();
            String line2 = br.readLine();
            String[] line3 = br.readLine().split("-");
            String newLine3 = "";
            for(String parts : line3) {
                if(parts == oldFriend) {
                    continue;
                }
                newLine3 += parts + "-";
            }
            bw.write(line1 + "\n");
            bw.write(line2 + "\n");
            bw.write(newLine3 + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}