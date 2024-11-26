import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.util.Scanner;

/**
 * Project 4 -- ApplicationClient
 * <p>
 * Runs a client for each user, asking them to log-in and then do further actions for direct
 * messaging and their account modification
 * (See the ReadMe for more details)
 *
 * @author Pranav Neti, Ryan Sierra, Tanya Jain, Anay Misra - Lab Section 12
 * @version Nov 17, 2024
 */

//TODO, when working on phase 3, the main classes that work together are Graphical Client, Graphical Server, and ReadMessageThread as well as User and Message for the database

public class GraphicalClient extends JComponent implements GraphicalClientInterface {
    //TODO make the graphical client interface
    //TODO consider changing all the null parent components to the frame on the JOptionPanes
    private static final int SERVER_PORT = 4243;
    private static Object gateKeep = new Object();
    public String[] message = {""};
    public String[] receiver = {""};



    public void actionsAfterLogin(BufferedWriter bw, BufferedReader br, Socket socket) {
        JFrame frame = new JFrame("Graphical Client Frame");
        frame.setLayout(null);
        frame.setSize(1000, 800);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //text area should update concurrently with new messages being sent and received as well as the second the user enters a dm with another person, all of those messages show up.
        JTextArea textArea = new JTextArea(10, 30);
        textArea.setEditable(false);
        textArea.setText("Click the \"Send Message\" Button To View Your Message History");
        textArea.append("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        textArea.append("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        textArea.append("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBounds(250, 100, 700, 570);

        frame.add(scrollPane);

        JTextField textField = new JTextField();
        textField.setEditable(true);
        textField.setText("Click the \"Send Message\" Button To Use This Field");
        textField.setBounds(350,675,500,35);
        frame.add(textField);

        JLabel sendMessageLabel = new JLabel("Send Message: ");
        sendMessageLabel.setBounds(250,675,200,35);
        frame.add(sendMessageLabel);

        JLabel messagesLabel = new JLabel("Message History: ");
        messagesLabel.setFont(new Font("Arial", Font.BOLD, 24));
        messagesLabel.setBounds(250,50,800,35);
        frame.add(messagesLabel);

        JButton enterButton = new JButton("Enter");
        enterButton.setBounds(850,670,125,25);
        frame.add(enterButton);
        enterButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(receiver[0].equals("")) {
                    JOptionPane.showMessageDialog(null, "Please Click Send Message To Get Started", "Send Message", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    message[0] = textField.getText();
                    String type = JOptionPane.showInputDialog(null, "What Message Type Is This (Image/Text)?", "Send Message", JOptionPane.QUESTION_MESSAGE);
                    if(type != null) {
                        while (true) {
                            if (!type.equalsIgnoreCase("Image") && !type.equalsIgnoreCase("Text")) {
                                type = JOptionPane.showInputDialog(null, "Invalid message type, please try again", "Send Message", JOptionPane.ERROR_MESSAGE);
                            } else {
                                break;
                            }
                        }
                        try {
                            bw.write("Message: " + receiver[0] + "-" + message[0] + "-" + type + "\n");
                            bw.flush();

                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
            }
        });

        //To use the delete button, type the text in the send message text field and then press the delete button
        //Same goes for the send message button
        JButton deleteButton = new JButton("Delete Message");
        deleteButton.setBounds(850,695,125,25);
        frame.add(deleteButton);
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //TODO currently not working, but this probably has to do with the messages not concurrently showing since its messing up the readLines()
                if(receiver[0].equals("")) {
                    JOptionPane.showMessageDialog(null, "Please Click Send Message To Get Started", "Send Message", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    message[0] = textField.getText();
                    String type = JOptionPane.showInputDialog(null, "What Message Type Is This (Image/Text)?", "Delete Message", JOptionPane.QUESTION_MESSAGE);
                    if(type != null) {
                        while (true) {
                            if (!type.equalsIgnoreCase("Image") && !type.equalsIgnoreCase("Text")) {
                                type = JOptionPane.showInputDialog(null, "Invalid message type, please try again", "Delete Message", JOptionPane.ERROR_MESSAGE);
                            } else {
                                break;
                            }
                        }
                        String result = "";
                        try {
                            bw.write("DELETE: " + receiver[0] + "-" + message[0] + "-" + type + "\n");
                            bw.flush();
                            result = br.readLine();
                            System.out.println(result);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                        if (result.equals("Successful Delete Message")) {
                            JOptionPane.showMessageDialog(null, "Message Was Deleted", "Delete Message", JOptionPane.INFORMATION_MESSAGE);
                        } else if (result.equals("This Messages Does Not Exist")) {
                            JOptionPane.showMessageDialog(null, "This Message Doesnt Exist", "Delete Message", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }

                }
            }
        });


        frame.setVisible(true);

        frame.setLayout(new BorderLayout());

        JButton setNameButton = new JButton("Set Name");
        setNameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = JOptionPane.showInputDialog(null, "What do you want to set your name to?", "Set Name", JOptionPane.PLAIN_MESSAGE );
                if(name != null) {
                    try {
                        bw.write("Name: " + name + "\n");
                        bw.flush();
                    } catch (IOException e1) {
                        throw new RuntimeException(e1);
                    }
                    JOptionPane.showMessageDialog(null, "Name updated successfully.", "Set Name", JOptionPane.INFORMATION_MESSAGE);
                }

            }
        });
        JButton setProfileDescriptionButton = new JButton("Set Profile Description");
        setProfileDescriptionButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String description = JOptionPane.showInputDialog(null, "Enter profile description: ", "Set Profile Description", JOptionPane.PLAIN_MESSAGE );
                if(description != null && !description.isEmpty() && !description.contains("-")) {
                    try {
                        bw.write("Profile Description: " + description + "\n");
                        bw.flush();
                    } catch (IOException e1) {
                        throw new RuntimeException(e1);
                    }
                    JOptionPane.showMessageDialog(null, "Profile description updated.", "Set Profile Description", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Profile descriptions cannot contain a \"-\"", "Set Profile Description", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        JButton setProfilePictureButton = new JButton("Set Profile Picture");
        setProfilePictureButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String imagePath = "";
                Object[] images = {"Dog", "Cat", "Bear", "Lion"};
                String selectedOption = (String) JOptionPane.showInputDialog(null,"Pick the image you want for your profile:", "Set Profile Picture", JOptionPane.DEFAULT_OPTION, null, images, images[0]);
                imagePath = "files/" + selectedOption + ".jpeg";
                if(selectedOption != null) {
                    try {
                        bw.write("Profile Picture: " + imagePath + "\n");
                        bw.flush();

                        if(br.readLine().equals("Image Found")) {
                            System.out.println("Image Found");
                            File imageFile = new File(imagePath);
                            BufferedImage image = ImageIO.read(imageFile);
                            Image scaledImage = image.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                            ImageIcon imageIcon = new ImageIcon(scaledImage);
                            JLabel imageLabel = new JLabel(imageIcon);
                            imageLabel.setPreferredSize(new Dimension(100,100));
                            JOptionPane.showMessageDialog(null, imageLabel, "New Profile Picture", JOptionPane.PLAIN_MESSAGE);

                        } else if(br.readLine().equals("Image file not found.")) {
                            JOptionPane.showMessageDialog(null, "Image file not found.", "Set Profile Picture", JOptionPane.ERROR_MESSAGE);
                        } else if(br.readLine().equals("Failed to load the image")) {
                            JOptionPane.showMessageDialog(null, "Failed to load the image:\n", "Set Profile Picture", JOptionPane.ERROR_MESSAGE);
                        } else if(br.readLine().equals("No input provided.")) {
                            JOptionPane.showMessageDialog(null, "No input provided.", "Set Profile Picture", JOptionPane.WARNING_MESSAGE);
                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    System.out.println("Profile picture updated.");
                }
            }
        });
        JButton viewProfileInformationButton = new JButton("View Profile Information");
        viewProfileInformationButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    bw.write("Profile Information: " + "\n");
                    bw.flush();
                    String name = br.readLine();
                    String desc = br.readLine();
                    String picPath = br.readLine();

                    if(picPath == null || picPath.isEmpty() || picPath.equals(" ")) {
                        JOptionPane.showMessageDialog(null, "Name: " + name + "\n" + "Description: " + desc + "\n" + "Profile Picture: None Set" + "\n");
                    } else {
                        File imageFile = new File(picPath);
                        BufferedImage image = ImageIO.read(imageFile);
                        Image scaledImage = image.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                        ImageIcon imageIcon = new ImageIcon(scaledImage);
                        JLabel imageLabel = new JLabel(imageIcon);
                        imageLabel.setPreferredSize(new Dimension(100, 100));

                        JPanel panel = new JPanel();
                        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
                        panel.add(new JLabel("Name: " + name));
                        panel.add(new JLabel("Description: " + desc));
                        panel.add(new JLabel("Profile Picture: "));
                        panel.add(imageLabel);

                        JOptionPane.showMessageDialog(null, panel, "Set Profile Information", JOptionPane.PLAIN_MESSAGE);
                    }
                } catch (IOException e1) {
                    throw new RuntimeException(e1);
                }
            }
        });
        JButton viewOtherProfileInformationButton = new JButton("View Another Users Profile");
        viewOtherProfileInformationButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = JOptionPane.showInputDialog(null, "Which users profile do you want to view (Please enter a valid username)", "View Another Users Profile", JOptionPane.QUESTION_MESSAGE);
                if(username != null) {
                    try {
                        bw.write("Check Profile of: " + username + "\n");
                        bw.flush();
                    } catch (IOException e1) {
                        throw new RuntimeException(e1);
                    }
                    try {
                        String firstLine = br.readLine();
                        if (firstLine.equals("This User Doesnt Exist")) {
                            JOptionPane.showMessageDialog(null, "This User Doesnt Exist.", "View Another Users Profile", JOptionPane.ERROR_MESSAGE);
                        } else {
                            String name = firstLine;
                            String desc = br.readLine();
                            String picPath = br.readLine();

                            if(picPath == null || picPath.isEmpty() || picPath.equals(" ")) {
                                JOptionPane.showMessageDialog(null, "Name: " + name + "\n" + "Description: " + desc + "\n" + "Profile Picture: None Set" + "\n");
                            } else {
                                File imageFile = new File(picPath);
                                BufferedImage image = ImageIO.read(imageFile);
                                Image scaledImage = image.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                                ImageIcon imageIcon = new ImageIcon(scaledImage);
                                JLabel imageLabel = new JLabel(imageIcon);
                                imageLabel.setPreferredSize(new Dimension(100, 100));

                                JPanel panel = new JPanel();
                                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
                                panel.add(new JLabel("Name: " + name));
                                panel.add(new JLabel("Description: " + desc));
                                panel.add(new JLabel("Profile Picture: "));
                                panel.add(imageLabel);

                                JOptionPane.showMessageDialog(null, panel, "Set Profile Information", JOptionPane.PLAIN_MESSAGE);
                            }

                        }
                    } catch (IOException e1) {
                        throw new RuntimeException(e1);
                    }
                }
            }
        });

        JPanel northPanel = new JPanel();
        northPanel.add(setNameButton);
        northPanel.add(setProfileDescriptionButton);
        northPanel.add(setProfilePictureButton);
        northPanel.add(viewProfileInformationButton);
        northPanel.add(viewOtherProfileInformationButton);
        frame.add(northPanel, BorderLayout.NORTH);


        JButton sendMessageButton = new JButton("Send Message");
        sendMessageButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // TODO fix reentering chat
                String receiverInput = JOptionPane.showInputDialog(null, "Enter The User You Want To Message", "Send Message", JOptionPane.QUESTION_MESSAGE);
                if(receiverInput != null) {
                    try {
                        bw.write("Check Block/Friends: " + receiverInput + "\n");
                        bw.flush();
                        String choice = br.readLine();
                        if (choice.equals("Can message")) {
                            messagesLabel.setText("Message History: " + receiverInput);
                            textField.setText("");
                            JOptionPane.showMessageDialog(null, "Entered DM", "Send Message", JOptionPane.INFORMATION_MESSAGE);
                            receiver[0] = receiverInput;
                            String username = "";
                            try {
                                bw.write("Give username\n");
                                bw.flush();
                                username = br.readLine();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            System.out.println("here");
                            Thread read = new Thread(new ReadMessageThread(receiverInput, username, socket));
                            read.start();
                            System.out.println("thread issue");

                            StringBuilder messageHistory = new StringBuilder();
                            String line = "";
                            System.out.println("start of while");
                            int counter = 0;
                            while ((line = br.readLine()) != null) {
                                //TODO issue with updating without being stuck in the while loop
                                System.out.println("reading: " + line);
                                messageHistory.append(line).append("\n");
                                textArea.setText(messageHistory.toString());
                                frame.repaint();
                            }
                        } else {
                            //cant message
                            JOptionPane.showMessageDialog(null, choice, "Send Message", JOptionPane.PLAIN_MESSAGE);
                            //goes back to the larger frame
                        }

                    } catch (IOException e1) {
                        throw new RuntimeException(e1);
                    }
                }
            }
        });
        JButton blockUserButton = new JButton("Block User");
        blockUserButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String blockUser = JOptionPane.showInputDialog(null, "Enter username to block: ", "Block User", JOptionPane.QUESTION_MESSAGE);
                if(blockUser != null) {
                    try {
                        bw.write("Block User: " + blockUser + "\n");
                        bw.flush();
                        String check = br.readLine();
                        if(check.equals("Successful")) {
                            bw.write("Remove Friend: " + blockUser + "\n");
                            br.readLine();
                            JOptionPane.showMessageDialog(null, blockUser + " has been blocked", "Block User", JOptionPane.INFORMATION_MESSAGE);
                        } else if(check.equals("Failed")) {
                            JOptionPane.showMessageDialog(null, blockUser + " doesnt exist", "Block User", JOptionPane.INFORMATION_MESSAGE);
                        }


                    } catch (IOException e1) {
                        throw new RuntimeException(e1);
                    }
                }
            }
        });
        JButton unblockUserButton = new JButton("Unblock User");
        unblockUserButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String unblockUser = JOptionPane.showInputDialog(null, "Enter username to unblock: ", "Block User", JOptionPane.QUESTION_MESSAGE);
                if(unblockUser != null) {
                    try {
                        bw.write("Unblock User: " + unblockUser + "\n");
                        bw.flush();
                        String check = br.readLine();
                        if(check.equals("Successful")) {
                            JOptionPane.showMessageDialog(null, unblockUser + " has been unblocked", "Block User", JOptionPane.INFORMATION_MESSAGE);
                        } else if(check.equals("Failed")) {
                            JOptionPane.showMessageDialog(null, unblockUser + " doesnt exist", "Block User", JOptionPane.INFORMATION_MESSAGE);
                        }

                    } catch (IOException e1) {
                        throw new RuntimeException(e1);
                    }
                }
            }
        });
        JButton viewBlockedUsersButton = new JButton("View Blocked Users");
        viewBlockedUsersButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    bw.write("Blocked Users: " + "\n");
                    bw.flush();
                    JOptionPane.showMessageDialog(null, "Blocked Users: " + br.readLine(), "Block Users", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException e1) {
                    throw new RuntimeException(e1);
                }
            }
        });
        JPanel westPanel = new JPanel(new GridBagLayout());
        westPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 0;
        westPanel.add(sendMessageButton, gbc);
        gbc.gridy = 1;
        westPanel.add(blockUserButton, gbc);
        gbc.gridy = 2;
        westPanel.add(unblockUserButton, gbc);
        gbc.gridy = 3;
        westPanel.add(viewBlockedUsersButton, gbc);
        frame.add(westPanel, BorderLayout.WEST);


        JButton addFriendButton = new JButton("Add Friend");
        addFriendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String newFriend = JOptionPane.showInputDialog(null,"Enter username to add as friend: ", "Add Friend", JOptionPane.QUESTION_MESSAGE);
                if(newFriend != null) {
                    try {
                        bw.write("Add Friend: " + newFriend + "\n");
                        bw.flush();
                        String check = br.readLine();
                        if(check.equals("Successful")) {
                            bw.write("Unblock User: " + newFriend + "\n");
                            br.readLine();
                            JOptionPane.showMessageDialog(null, newFriend + " has been added to friends", "Add Friend", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null, newFriend + " doesn't exist", "Add Friend", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } catch (IOException e1) {
                        throw new RuntimeException(e1);
                    }
                }
            }
        });
        JButton removeFriendButton = new JButton("Remove Friend");
        removeFriendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String oldFriend = JOptionPane.showInputDialog(null,"Enter username to remove from friends: ", "Remove Friend", JOptionPane.QUESTION_MESSAGE);
                if(oldFriend != null) {
                    try {
                        bw.write("Remove Friend: " + oldFriend + "\n");
                        bw.flush();
                        String check = br.readLine();
                        if(check.equals("Successful")) {
                            JOptionPane.showMessageDialog(null, oldFriend + " has been removed from friends", "Remove Friend", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null, oldFriend + " doesn't exist", "Remove Friend", JOptionPane.INFORMATION_MESSAGE);
                        }

                    } catch (IOException e1) {
                        throw new RuntimeException(e1);
                    }
                }
            }
        });
        JButton viewFriendButton = new JButton("View Friends");
        viewFriendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    bw.write("Friend List: " + "\n");
                    bw.flush();
                    JOptionPane.showMessageDialog(null, "Friend List: " + br.readLine(), "Friends", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException e1) {
                    throw new RuntimeException(e1);
                }
            }
        });
        JButton canReceiveFromButton = new JButton("Change Receiving Options (Friends/All)");
        canReceiveFromButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int num = JOptionPane.showConfirmDialog(null, "Do you want to receive messages from anyone (yes/no)", "Change Receiving Options", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if(num != JOptionPane.CLOSED_OPTION) {
                    int choice = Integer.parseInt(String.valueOf(num));
                    boolean yesNo = (choice == 0);
                    try {
                        bw.write("Change Can Receive Anyone: " + yesNo + "\n");
                        bw.flush();
                    } catch (IOException e1) {
                        throw new RuntimeException(e1);
                    }
                    if(yesNo) {
                        JOptionPane.showMessageDialog(null, "You can now receive messages from everyone", "Change Receiving Options", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "You can now receive messages from only friends", "Change Receiving Options", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });

        JPanel eastPanel = new JPanel();
        eastPanel.add(addFriendButton);
        eastPanel.add(removeFriendButton);
        eastPanel.add(viewFriendButton);
        eastPanel.add(canReceiveFromButton);
        frame.add(eastPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        GraphicalClient client = new GraphicalClient();
        JOptionPane.showMessageDialog(null,"Welcome to the Social Media Application (Phase 3)", "GraphicalClient", JOptionPane.INFORMATION_MESSAGE);

        JTextField username = new JTextField();
        JTextField password = new JPasswordField();
        Object[] usernamePasswordMessage = {"Username:", username, "Password:", password};
        Object[] loginOrCreateAccount = {"Exit", "Create an Account", "Login"};


        Socket socket = new Socket("localhost", 4243);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        boolean mostOuterCheck = true;

        do {
            int loginOrCreateAccountChoice = JOptionPane.showOptionDialog(null, "Do you want to login or create an account?", "GraphicalClient", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, loginOrCreateAccount, null);
            int choice = Integer.parseInt(String.valueOf(loginOrCreateAccountChoice));

            switch (choice) {
                case 1 -> {
                    String user = "";
                    String pass = "";
                    boolean validUser = false;
                    boolean validPass = false;
                    boolean validUserAndPass = false;
                    do {
                        int option = JOptionPane.showConfirmDialog(null, usernamePasswordMessage, "Create an Account", JOptionPane.OK_CANCEL_OPTION);
                        if (option == JOptionPane.OK_OPTION) {
                            user = username.getText();
                            pass = password.getText();

                            if (!user.contains("-") && !user.isEmpty()) {
                                validUser = true;
                            } else {
                                JOptionPane.showMessageDialog(null,"InvalidUsername. Username either contained a \"-\" or was empty", "GraphicalClient", JOptionPane.INFORMATION_MESSAGE);
                            }
                            if (!pass.contains("-") && !pass.isEmpty() && !pass.contains(" ")) {
                                validPass = true;
                            } else {
                                JOptionPane.showMessageDialog(null,"Invalid Password. Password either contains a \"-\" \" \"or is empty", "GraphicalClient", JOptionPane.INFORMATION_MESSAGE);
                            }

                            if(validUser && validPass) {
                                bw.write("Username Create: " + user + "\n");
                                bw.flush();
                                if (br.readLine().equals("New User")) {
                                    validUserAndPass = true;
                                    bw.write("Password Create: " + pass + "\n");
                                    bw.flush();
                                    JOptionPane.showMessageDialog(null,"Account Creation Successful", "GraphicalClient", JOptionPane.INFORMATION_MESSAGE);
                                    client.actionsAfterLogin(bw, br, socket);
                                } else {
                                    JOptionPane.showMessageDialog(null,"Username is in use, select a new username", "GraphicalClient", JOptionPane.INFORMATION_MESSAGE);
                                }
                                mostOuterCheck = false;
                            }
                        } else {
                            JOptionPane.showMessageDialog(null,"Account Creation canceled", "GraphicalClient", JOptionPane.INFORMATION_MESSAGE);
                            break;
                        }

                    } while (!validUserAndPass);


                }

                case 2 -> {
                    boolean checkInValidInput = true;
                    while(checkInValidInput){
                        int option = JOptionPane.showConfirmDialog(null, usernamePasswordMessage, "Login", JOptionPane.OK_CANCEL_OPTION);
                        if(option == JOptionPane.OK_OPTION) {
                            if(username.getText().isEmpty() || username.getText().contains("-") || username.getText().contains(" ")) {
                                JOptionPane.showMessageDialog(null,"Invalid Username. Contains a \"-\", \" \", or is empty", "GraphicalClient", JOptionPane.INFORMATION_MESSAGE);
                            } else if(password.getText().isEmpty() || password.getText().contains("-") || password.getText().contains(" ")) {
                                JOptionPane.showMessageDialog(null,"Invalid Password. Contains a \"-\", \" \", or is empty", "GraphicalClient", JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                bw.write("Username Login: " + username.getText() + "\n");
                                bw.flush();
                                String pass = password.getText();
                                bw.write("Password Login: " + pass + "\n");
                                bw.flush();
                                String line = br.readLine();
                                if(line.equals("Logged In!")) {
                                    JOptionPane.showMessageDialog(null,"Login Successful. Welcome Old User", "GraphicalClient", JOptionPane.INFORMATION_MESSAGE);
                                    checkInValidInput = false;
                                    mostOuterCheck = false;
                                    client.actionsAfterLogin(bw, br, socket);
                                } else if(line.equals("Created New User!")) {
                                    int newAccount = JOptionPane.showConfirmDialog(null,"The username and password you entered doesnt belong to any account. Would you like to create a new account with this information?", "GraphicalClient", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                                    int intNewAccount = Integer.parseInt(String.valueOf(newAccount));
                                    if(intNewAccount == 0) {
                                        bw.write("yes\n");
                                        bw.flush();
                                        JOptionPane.showMessageDialog(null,"Login Successful. Welcome New User", "GraphicalClient", JOptionPane.INFORMATION_MESSAGE);
                                        checkInValidInput = false;
                                        mostOuterCheck = false;
                                        client.actionsAfterLogin(bw, br, socket);
                                    } else {
                                        bw.write("no\n");
                                        bw.flush();
                                        checkInValidInput = false;
                                    }

                                } else if (line.equals("Wrong Password")) {
                                    JOptionPane.showMessageDialog(null,"Wrong Password. Please Try Again!", "GraphicalClient", JOptionPane.INFORMATION_MESSAGE);
                                }
                            }
                        } else {
                            JOptionPane.showMessageDialog(null,"Login canceled", "GraphicalClient", JOptionPane.INFORMATION_MESSAGE);
                            break;
                        }
                    }
                }

                case 0 -> {
                    bw.write("Exit" + "\n");
                    bw.flush();
                    JOptionPane.showMessageDialog(null,"Goodbye!", "GraphicalClient", JOptionPane.INFORMATION_MESSAGE);
                    mostOuterCheck = false;
                    System.exit(0);
                }

                default -> {
                    System.exit(0);
//                    JOptionPane.showMessageDialog(null,"Invalid Input (Should never be reached)", "GraphicalClient", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } while (mostOuterCheck);
    }
}