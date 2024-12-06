import org.w3c.dom.Text;

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
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
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

//MERGE THE READ MESSAGE THREAD CLASS INTO THE GRAPHICAL SERVER, AND WHEN YOU INITIALIZE THE CONSTRUCTOR FOR THE SERVER
// PASS IN THE VARIABLES FOR THE FRAME, JTEXTPANE, ETC. BASICALLY EVERYHTING THATS AN OBJECT AND NOT JUST TEXT.

public class GraphicalClient extends JComponent implements GraphicalClientInterface, Runnable {
    private static final int SERVER_PORT = 4241;
    private static Object gateKeep = new Object();
    public String[] message = {""};
    public String[] receiver = {""};
    Thread read;
    public BufferedWriter writer;
    public BufferedReader reader;
    public JTextPane textPane;
    public JFrame frame;



    public void actionsAfterLogin(BufferedWriter bw, BufferedReader br, Socket socket) {
        reader = br;
        writer = bw;
        ImageIcon originalIcon = new ImageIcon("files/icon.png");
        Image image = originalIcon.getImage();

        Image scaledIcon = image.getScaledInstance(50, 50, Image.SCALE_SMOOTH); // New width and height
        ImageIcon icon = new ImageIcon(scaledIcon);

        frame = new JFrame("Skyline Chat");
        frame.setLayout(null);
        frame.setSize(1000, 800);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //text area should update concurrently with new messages being sent and received as well
        //as the second the user enters a dm with another person, all of those messages show up.
        textPane = new JTextPane();
//        textPane.setBackground(new Color(210,246,253));
        textPane.setEditable(false);
        textPane.setText("Click the \"Send Message\" Button To View Your Message History" +
                "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" +
                "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        JScrollPane scrollPane = new JScrollPane(textPane);
        frame.setBackground(new Color(210,246,253));
        scrollPane.setBounds(250, 100, 700, 570);

        frame.add(scrollPane);

        JTextField textField = new JTextField();
//        textField.setBackground(new Color(210,246,253));
        textField.setEditable(true);
        textField.setText("Click the \"Send Message\" Button To Use This Field");
        textField.setBounds(350, 675, 500, 35);
        frame.add(textField);

        JLabel sendMessageLabel = new JLabel("Send Message: ");
        sendMessageLabel.setBackground(new Color(210,246,253));
        sendMessageLabel.setBounds(250, 675, 200, 35);
        frame.add(sendMessageLabel);

        JLabel messagesLabel = new JLabel("");
        messagesLabel.setBackground(new Color(210,246,253));
        messagesLabel.setFont(new Font("Arial", Font.BOLD, 24));
        messagesLabel.setBounds(250, 50, 800, 35);
        frame.add(messagesLabel);

        JButton enterButton = new JButton("Enter");
        enterButton.setBounds(850, 670, 125, 25);
        frame.add(enterButton);
        enterButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String initialCheck = "";
                try {
                    bw.write("Check Block/Friends: " + receiver[0] + "\n");
                    bw.flush();
                    initialCheck = br.readLine();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                if (initialCheck.equals("One of you has blocked the other")) {
                    JOptionPane.showMessageDialog(null, "You Currently have this user blocked or are blocked by this user", "Send Message", JOptionPane.INFORMATION_MESSAGE, icon);
                } else if (textField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "You Can't Send An Empty Message", "Send Message", JOptionPane.INFORMATION_MESSAGE, icon);
                } else {
                    if (receiver[0].equals("")) {
                        JOptionPane.showMessageDialog(null, "Please Click Send Message To Get Started", "Send Message", JOptionPane.INFORMATION_MESSAGE, icon);
                    } else {
                        Object[] options = {"Text", "Image"};
                        int typeInt = JOptionPane.showOptionDialog(null, "What Message Type Is This (Image/Text)?", "Send Message", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, icon, options, options[0]);
//                        System.out.println(type);
                        if(typeInt != -1) {
                            try {
                                if (typeInt == 1) {
                                    bw.write("Check Valid Image File " + textField.getText() + "\n");
                                    bw.flush();
                                    String valid = br.readLine();
                                    if (valid.equals("Valid Image")) {
                                        if (textField.getText().contains("file://")) {
                                            String field = textField.getText();
                                            String og = field;
                                            String profilePicture = field.substring(8);
                                            System.out.println(profilePicture);
                                            // <p><img src='file:///Users/Pranav/Downloads/imagination.png' alt='' width='300' height='200'></p>
                                            try {
                                                String fileName = profilePicture.substring(profilePicture.lastIndexOf("/") + 1);
                                                File f1 = new File(profilePicture);
                                                File f2 = new File("./files/" + "<text>" + fileName);

                                                Files.copy(f1.toPath(), f2.toPath(), StandardCopyOption.COPY_ATTRIBUTES);
                                                profilePicture = "files/" + "<text>" + fileName;
                                                field = field.substring(0, 8) + profilePicture;
                                                System.out.println(field);
                                            } catch (Exception e2) {
                                                e2.printStackTrace();
                                                field = og;
                                            }
                                            message[0] = "<p><img src='" + field.replaceAll(" ", "%20") + "' alt='' width='300' height='200'>" + "</p>";

                                        } else {
                                            String field = textField.getText();
                                            String og = field;
                                            String profilePicture = field;
                                            System.out.println(profilePicture);
                                            // <p><img src='file:///Users/Pranav/Downloads/imagination.png' alt='' width='300' height='200'></p>
                                            try {
                                                String fileName = profilePicture.substring(profilePicture.lastIndexOf("/") + 1);
                                                File f1 = new File(profilePicture);
                                                File f2 = new File("./files/" + "<text>" + fileName);

                                                profilePicture = "files/" + "<text>" + fileName;
                                                field = profilePicture;
                                                Files.copy(f1.toPath(), f2.toPath(), StandardCopyOption.COPY_ATTRIBUTES);
                                            } catch (FileAlreadyExistsException e1) {
                                                e1.printStackTrace();
                                            } catch (Exception e2) {
                                                e2.printStackTrace();
                                                field = og;
                                            }
                                            message[0] = "<p><img src='" + field.replaceAll(" ", "%20") + "' alt='' width='300' height='200'>" + "</p>";
                                        }
                                        bw.write("Message: " + receiver[0] + "-" + message[0] + "-" + "Image" + "\n");
                                        bw.flush();
                                    } else {
                                        JOptionPane.showMessageDialog(null, "The File Path You Entered Was Invalid", "Send Message", JOptionPane.ERROR_MESSAGE, icon);
                                    }
                                } else {
                                    message[0] = textField.getText();
                                    System.out.println("Message: " + receiver[0] + "-" + message[0] + "-" + "Text" + "\n");
                                    bw.write("Message: " + receiver[0] + "-" + message[0] + "-" + "Text" + "\n");
                                    bw.flush();
                                }
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                        }

                    }
                }
            }
        });

        //To use the delete button, type the text in the send message text field and then press the delete button
        //Same goes for the send message button
        JButton deleteButton = new JButton("Delete Message");
        deleteButton.setBounds(850, 695, 125, 25);
        frame.add(deleteButton);
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                pauseThread();
                String initialCheck = "";
                try {
                    bw.write("Check Block/Friends: " + receiver[0] + "\n");
                    bw.flush();
                    initialCheck = br.readLine();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                if (initialCheck.equals("One of you has blocked the other")) {
                    JOptionPane.showMessageDialog(null, "You Currently have this user blocked or are blocked by this user", "Delete Message", JOptionPane.INFORMATION_MESSAGE, icon);
                } else {
                    if (receiver[0].equals("")) {
                        JOptionPane.showMessageDialog(null, "Please Click Send Message To Get Started", "Delete Message", JOptionPane.INFORMATION_MESSAGE, icon);
                    } else {
                        Object[] options = {"Text", "Image"};
                        int type = JOptionPane.showOptionDialog(null, "What Message Type Is This (Image/Text)?", "Delete Message", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, icon, options, options[0]);
                        if (textField.getText().trim().isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Cant Delete An Empty Message", "Delete Message", JOptionPane.ERROR_MESSAGE, icon);
                        } else if (type != -1) {
                            message[0] = textField.getText();
                            String result = "";
                            try {
                                String typeStr = "";
                                if(type == 1) {
                                    typeStr = "Image";
                                } else {
                                    typeStr = "Text";
                                }
                                bw.write("DELETE: " + receiver[0] + "-" + message[0] + "-" + typeStr + "\n");
                                bw.flush();
                                result = br.readLine();
                                System.out.println("LOOK HERE: " + result);
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                            if (result.equals("Successful Delete Message")) {
                                JOptionPane.showMessageDialog(null, "Message Was Deleted", "Delete Message", JOptionPane.INFORMATION_MESSAGE, icon);
                            } else if (result.equals("This Messages Does Not Exist")) {
                                JOptionPane.showMessageDialog(null, "This Message Doesnt Exist", "Delete Message", JOptionPane.INFORMATION_MESSAGE, icon);
                            } else if (result.equals("You Dont Own This Image")) {
                                JOptionPane.showMessageDialog(null, "This Is A Message You Didnt Send, Therefore You Can't Delete It", "Delete Message", JOptionPane.INFORMATION_MESSAGE, icon);

                            }
                        }
//                        continueThread(br, bw, socket, frame, textPane);
                    }
                }
            }
        });

        frame.setVisible(true);

        frame.setLayout(new BorderLayout());

        JButton setNameButton = new JButton("Set Name");
        setNameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                pauseThread();
                String name = JOptionPane.showInputDialog(null, "What do you want to set your name to?", "Set Name", JOptionPane.PLAIN_MESSAGE);
                if (name == null) {
                    return;
                }
                if (!name.trim().isEmpty() && !name.contains("-")) {
                    try {
                        bw.write("Name: " + name + "\n");
                        bw.flush();
                    } catch (IOException e1) {
                        throw new RuntimeException(e1);
                    }
                    JOptionPane.showMessageDialog(null, "Name updated successfully.", "Set Name", JOptionPane.INFORMATION_MESSAGE, icon);
                } else {
                    JOptionPane.showMessageDialog(null, "Name either contained a \"-\" or was empty", "Set Name", JOptionPane.ERROR_MESSAGE, icon);
                }
//                continueThread(br, bw, socket, frame, textPane);
            }
        });
        JButton setProfileDescriptionButton = new JButton("Set Profile Description");
        setProfileDescriptionButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                pauseThread();
                String description = JOptionPane.showInputDialog(null, "Enter profile description: ", "Set Profile Description", JOptionPane.PLAIN_MESSAGE);
                if (description == null) {
                    return;
                }
                if (!description.trim().isEmpty() && !description.contains("-")) {
                    try {
                        bw.write("Profile Description: " + description + "\n");
                        bw.flush();
                    } catch (IOException e1) {
                        throw new RuntimeException(e1);
                    }
                    JOptionPane.showMessageDialog(null, "Profile description updated.", "Set Profile Description", JOptionPane.INFORMATION_MESSAGE, icon);
                } else {
                    JOptionPane.showMessageDialog(null, "Profile descriptions either contained a \"-\" or was empty", "Set Profile Description", JOptionPane.ERROR_MESSAGE, icon);
                }
//                continueThread(br, bw, socket, frame, textPane);
            }
        });
        JButton setProfilePictureButton = new JButton("Set Profile Picture");
        setProfilePictureButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                pauseThread();
                String imagePath = JOptionPane.showInputDialog(null, "Please enter the file path to the image you want to set as your profile picture", "Set Profile Picture", JOptionPane.PLAIN_MESSAGE);
                //to test, go copy the absolute path from any of the images
                if (imagePath == null || imagePath.trim().isEmpty()) {
                    return;
                }
                if (imagePath.contains("-")) {
                    do {
                        imagePath = JOptionPane.showInputDialog(null, "Your input contained a \"-\". Please enter the file path to the image you want to set as your profile picture", "Set Profile Picture", JOptionPane.PLAIN_MESSAGE);
                        if (imagePath == null || imagePath.trim().isEmpty()) {
                            break;
                        }
                    } while (imagePath.contains("-"));
                }
                try {
                    bw.write("Profile Picture: " + imagePath + "\n");
                    bw.flush();
                    String choice = br.readLine();
                    if (choice.equals("Image Found")) {
                        System.out.println("Image Found");
                        File imageFile = new File(imagePath);
                        BufferedImage image = ImageIO.read(imageFile);
                        Image scaledImage = image.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                        ImageIcon imageIcon = new ImageIcon(scaledImage);
                        JLabel imageLabel = new JLabel(imageIcon);
                        imageLabel.setPreferredSize(new Dimension(100, 100));
                        JOptionPane.showMessageDialog(null, imageLabel, "New Profile Picture", JOptionPane.PLAIN_MESSAGE, icon);
                    } else {
                        JOptionPane.showMessageDialog(null, choice, "Set Profile Picture", JOptionPane.ERROR_MESSAGE, icon);
                    }
                } catch (IOException e1) {
                    System.out.println("");
                }
                System.out.println("Profile picture updated.");
//                continueThread(br, bw, socket, frame, textPane);
            }
        });
        JButton viewProfileInformationButton = new JButton("View Profile Information");
        viewProfileInformationButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                pauseThread();
                try {
                    bw.write("Profile Information: " + "\n");
                    bw.flush();
                    String name = br.readLine();
                    String desc = br.readLine();
                    String picPath = br.readLine();

                    if (picPath == null || picPath.trim().isEmpty() || picPath.equals(" ")) {
                        JOptionPane.showMessageDialog(null, "Name: " + name + "\n" + "Description: " + desc + "\n" + "Profile Picture: None Set" + "\n", "View Profile Information", JOptionPane.PLAIN_MESSAGE, icon);
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

                        JOptionPane.showMessageDialog(null, panel, "Set Profile Information", JOptionPane.PLAIN_MESSAGE, icon);
                    }
                } catch (IOException e1) {
                    throw new RuntimeException(e1);
                }
//                continueThread(br, bw, socket, frame, textPane);
            }
        });
        JButton viewOtherProfileInformationButton = new JButton("View Another Users Profile");
        viewOtherProfileInformationButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                pauseThread();
                String username = JOptionPane.showInputDialog(null, "Which users profile do you want to view (Please enter a valid username)", "View Another Users Profile", JOptionPane.PLAIN_MESSAGE);
                if (username != null && !username.trim().isEmpty()) {
                    try {
                        bw.write("Check Profile of: " + username + "\n");
                        bw.flush();
                    } catch (IOException e1) {
                        throw new RuntimeException(e1);
                    }
                    try {
                        String firstLine = br.readLine();
                        if (firstLine.equals("This User Doesnt Exist")) {
                            JOptionPane.showMessageDialog(null, "This User Doesnt Exist.", "View Another Users Profile", JOptionPane.ERROR_MESSAGE, icon);
                        } else {
                            String name = firstLine;
                            String desc = br.readLine();
                            String picPath = br.readLine();

                            if (picPath == null || picPath.trim().isEmpty()) {
                                JOptionPane.showMessageDialog(null, "Name: " + name + "\n" + "Description: " + desc + "\n" + "Profile Picture: None Set" + "\n", "View Another Users Profile", JOptionPane.PLAIN_MESSAGE, icon);
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

                                JOptionPane.showMessageDialog(null, panel, "Set Profile Information", JOptionPane.PLAIN_MESSAGE, icon);
                            }

                        }
                    } catch (IOException e1) {
                        throw new RuntimeException(e1);
                    }
                }
//                continueThread(br, bw, socket, frame, textPane);
            }
        });

        JPanel northPanel = new JPanel();
        northPanel.setBackground(new Color(210,246,253));
        northPanel.add(setNameButton);
        northPanel.add(setProfileDescriptionButton);
        northPanel.add(setProfilePictureButton);
        northPanel.add(viewProfileInformationButton);
        northPanel.add(viewOtherProfileInformationButton);
        frame.add(northPanel, BorderLayout.NORTH);


        JButton sendMessageButton = new JButton("Select User");
        sendMessageButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                pauseThread();
                String receiverInput = JOptionPane.showInputDialog(null, "Enter The User You Want To Message", "Select User", JOptionPane.PLAIN_MESSAGE);
                if (receiverInput != null && !receiverInput.trim().isEmpty()) {
                    try {
                        String username = "";
                        try {
                            bw.write("Give username\n");
                            bw.flush();
                            username = br.readLine();
                        } catch (IOException e1) {
                            System.out.println("");
                        }
                        if (username.equals(receiverInput)) {
                            JOptionPane.showMessageDialog(null, "You cannot message yourself", "Select User", JOptionPane.ERROR_MESSAGE, icon);
                        } else {
                            bw.write("Check Block/Friends: " + receiverInput + "\n");
                            bw.flush();
                            String choice = br.readLine();
                            if (choice.equals("Can message")) {
                                messagesLabel.setText("Current Chat: " + receiverInput);
                                textField.setText("");
                                JOptionPane.showMessageDialog(null, "Entered DM", "Select User", JOptionPane.INFORMATION_MESSAGE, icon);
                                receiver[0] = receiverInput;
                                System.out.println("here");
                                textPane.setText("");
                                //Run Thread: username-receiver
                                //read = new Thread(new ReadMessageThread(receiverInput, username, socket, textPane, frame));
                                //read.start();
                                bw.write("Run Thread: " + username + "-" + receiverInput + "\n");
                                bw.flush();

                                read = new Thread(new ReadMessageThreadGraphical(receiverInput, username, br, bw));
                                read.start();
                                Thread thread = new Thread(() -> run());
                                thread.start();
                            } else {
                                //cant message
                                JOptionPane.showMessageDialog(null, choice, "Select User", JOptionPane.PLAIN_MESSAGE, icon);
                                //goes back to the larger frame71
                            }
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
//                pauseThread();
                String blockUser = JOptionPane.showInputDialog(null, "Enter username to block: ", "Block User", JOptionPane.PLAIN_MESSAGE);
                if (blockUser != null && !blockUser.trim().isEmpty()) {
                    try {
                        String username = "";
                        try {
                            bw.write("Give username\n");
                            bw.flush();
                            username = br.readLine();
                        } catch (IOException e1) {
                            System.out.println(e1.getMessage());
                        }
                        if (username.equals(blockUser)) {
                            JOptionPane.showMessageDialog(null, "You cannot block yourself", "Block User", JOptionPane.ERROR_MESSAGE, icon);
                        } else {
                            bw.write("Block User: " + blockUser + "\n");
                            bw.flush();
                            String check = br.readLine();
                            if (check.equals("Successful")) {
                                System.out.println("Removeing Friend");
                                bw.write("Remove Friend: " + blockUser + "\n");
                                bw.flush();
                                br.readLine();
                                System.out.println("Finished Removeing Friend");
                                JOptionPane.showMessageDialog(null, blockUser + " has been blocked", "Block User", JOptionPane.INFORMATION_MESSAGE, icon);
                            } else if (check.equals("Failed")) {
                                JOptionPane.showMessageDialog(null, blockUser + " doesnt exist", "Block User", JOptionPane.INFORMATION_MESSAGE, icon);
                            }
                        }
                    } catch (IOException e1) {
                        System.out.println(e1.getMessage());
                    }
                }
                if (blockUser != receiver[0]) {
//                    continueThread(br, bw, socket, frame, textPane);
                }

            }
        });
        JButton unblockUserButton = new JButton("Unblock User");
        unblockUserButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                pauseThread();
                String username = "";
                String unblockUser = JOptionPane.showInputDialog(null, "Enter username to unblock: ", "Block User", JOptionPane.PLAIN_MESSAGE);
                if (unblockUser != null && !unblockUser.trim().isEmpty()) {
                    try {
                        try {
                            bw.write("Give username\n");
                            bw.flush();
                            username = br.readLine();
                        } catch (IOException e1) {
                            System.out.println("");
                        }

                        if (username.equals(unblockUser)) {
                            JOptionPane.showMessageDialog(null, "You Cannot Block Yourself", "Block User", JOptionPane.INFORMATION_MESSAGE, icon);
                        } else {
                            bw.write("Unblock User: " + unblockUser + "\n");
                            bw.flush();
                            String check = br.readLine();
                            if (check.equals("Successful")) {
                                JOptionPane.showMessageDialog(null, unblockUser + " has been unblocked", "Block User", JOptionPane.INFORMATION_MESSAGE, icon);
                            } else if (check.equals("Failed")) {
                                JOptionPane.showMessageDialog(null, unblockUser + " doesnt exist", "Block User", JOptionPane.INFORMATION_MESSAGE, icon);
                            }
                        }

                    } catch (IOException e1) {
                        throw new RuntimeException(e1);
                    }
                }
//                continueThread(br, bw, socket, frame, textPane);
            }
        });
        JButton viewBlockedUsersButton = new JButton("View Blocked Users");
        viewBlockedUsersButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                pauseThread();
                try {
                    bw.write("Blocked Users: " + "\n");
                    bw.flush();
                    JOptionPane.showMessageDialog(null, "Blocked Users: " + br.readLine(), "Block Users", JOptionPane.INFORMATION_MESSAGE, icon);
                } catch (IOException e1) {
                    throw new RuntimeException(e1);
                }
//                continueThread(br, bw, socket, frame, textPane);
            }
        });

        JPanel westPanel = new JPanel(new GridBagLayout());
        westPanel.setBackground(new Color(210, 246, 253));
        westPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.anchor = GridBagConstraints.NORTH;

        westPanel.add(new JLabel(new ImageIcon(new ImageIcon("files/icon.png").getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH))), gbc);
        gbc.gridy = 1;
        gbc.weighty = 0.4;
        JLabel nameLabel = new JLabel("Skyline Chat");
        nameLabel.setSize(new Dimension(50,50));
        nameLabel.setFont(new Font("Arial", Font.BOLD, 24));
        westPanel.add(nameLabel, gbc);


        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridy = 2;
        westPanel.add(sendMessageButton, gbc);
        gbc.gridy = 3;
        westPanel.add(blockUserButton, gbc);
        gbc.gridy = 4;
        westPanel.add(unblockUserButton, gbc);
        gbc.gridy = 5;
        westPanel.add(viewBlockedUsersButton, gbc);
        gbc.gridy = 6;
        gbc.weighty = 1.0;

        westPanel.add(new JLabel(), gbc);
        frame.add(westPanel, BorderLayout.WEST);


        JButton addFriendButton = new JButton("Add Friend");
        addFriendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                pauseThread();
                String username = "";
                String newFriend = JOptionPane.showInputDialog(null, "Enter username to add as friend: ", "Add Friend", JOptionPane.PLAIN_MESSAGE);
                if (newFriend != null && !newFriend.trim().isEmpty()) {
                    try {
                        bw.write("Give username\n");
                        bw.flush();
                        username = br.readLine();
                        if (username.equals(newFriend)) {
                            JOptionPane.showMessageDialog(null, "You cannot add yourself as a friend", "Add Friend", JOptionPane.ERROR_MESSAGE, icon);
                        } else {
                            bw.write("Add Friend: " + newFriend + "\n");
                            bw.flush();
                            String check = br.readLine();
                            if (check.equals("Successful")) {
                                bw.write("Unblock User: " + newFriend + "\n");
                                br.readLine();
                                JOptionPane.showMessageDialog(null, newFriend + " has been added to friends", "Add Friend", JOptionPane.INFORMATION_MESSAGE, icon);
                            } else {
                                JOptionPane.showMessageDialog(null, newFriend + " doesn't exist", "Add Friend", JOptionPane.INFORMATION_MESSAGE, icon);
                            }
                        }
                    } catch (IOException e1) {
                        throw new RuntimeException(e1);
                    }
                }
//                continueThread(br, bw, socket, frame, textPane);
            }
        });
        JButton removeFriendButton = new JButton("Remove Friend");
        removeFriendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                pauseThread();
                String username = "";
                String oldFriend = JOptionPane.showInputDialog(null, "Enter username to remove from friends: ", "Remove Friend", JOptionPane.PLAIN_MESSAGE);
                if (oldFriend != null && !oldFriend.trim().isEmpty()) {
                    try {
                        bw.write("Give username\n");
                        bw.flush();
                        username = br.readLine();
                        if (username.equals(oldFriend)) {
                            JOptionPane.showMessageDialog(null, "You cannot remove yourself as a friend", "Add Friend", JOptionPane.ERROR_MESSAGE, icon);
                        } else {
                            bw.write("Remove Friend: " + oldFriend + "\n");
                            bw.flush();
                            String check = br.readLine();
                            if (check.equals("Successful")) {
                                JOptionPane.showMessageDialog(null, oldFriend + " has been removed from friends", "Remove Friend", JOptionPane.INFORMATION_MESSAGE, icon);
                            } else {
                                JOptionPane.showMessageDialog(null, oldFriend + " doesn't exist", "Remove Friend", JOptionPane.INFORMATION_MESSAGE, icon);
                            }
                        }
                    } catch (IOException e1) {
                        throw new RuntimeException(e1);
                    }
                }
//                continueThread(br, bw, socket, frame, textPane);
            }
        });
        JButton viewFriendButton = new JButton("View Friends");
        viewFriendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                pauseThread();
                try {
                    bw.write("Friend List: " + "\n");
                    bw.flush();
                    JOptionPane.showMessageDialog(null, "Friend List: " + br.readLine(), "Friends", JOptionPane.INFORMATION_MESSAGE, icon);
                } catch (IOException e1) {
                    throw new RuntimeException(e1);
                }
//                continueThread(br, bw, socket, frame, textPane);
            }
        });
        JButton canReceiveFromButton = new JButton("Change Receiving Options (Friends/All)");
        canReceiveFromButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                pauseThread();
                int num = JOptionPane.showConfirmDialog(null, "Do you want to receive messages from anyone (yes/no)", "Change Receiving Options", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, icon);
                if (num != JOptionPane.CLOSED_OPTION) {
                    int choice = Integer.parseInt(String.valueOf(num));
                    boolean yesNo = (choice == 0);
                    try {
                        bw.write("Change Can Receive Anyone: " + yesNo + "\n");
                        bw.flush();
                    } catch (IOException e1) {
                        throw new RuntimeException(e1);
                    }
                    if (yesNo) {
                        JOptionPane.showMessageDialog(null, "You can now receive messages from everyone", "Change Receiving Options", JOptionPane.INFORMATION_MESSAGE, icon);
                    } else {
                        JOptionPane.showMessageDialog(null, "You can now receive messages from only friends", "Change Receiving Options", JOptionPane.INFORMATION_MESSAGE, icon);
                    }
                }
//                continueThread(br, bw, socket, frame, textPane);
            }
        });

        JPanel eastPanel = new JPanel();
        eastPanel.setBackground(new Color(210,246,253));
        eastPanel.add(addFriendButton);
        eastPanel.add(removeFriendButton);
        eastPanel.add(viewFriendButton);
        eastPanel.add(canReceiveFromButton);
        frame.add(eastPanel, BorderLayout.SOUTH);

        frame.getContentPane().setBackground(new Color(210,246,253));

        frame.setVisible(true);
    }

    @Override
    public void run() {
        try {
            while(true) {
                String readLine = reader.readLine();
                if(readLine.startsWith("Message History: ")) {
                    String messageHistory = readLine.substring("Message History: ".length());
                    textPane.setContentType("text/html");
                    textPane.setText(messageHistory);
                    textPane.setCaretPosition(textPane.getDocument().getLength());
                    frame.repaint();
                } else {
                    writer.write("Push Back: "  + readLine + "\n");
                    writer.flush();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }


    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        GraphicalClient client = new GraphicalClient();
        ImageIcon originalIcon = new ImageIcon("files/icon.png");
        Image image = originalIcon.getImage();
        Image scaledIcon = image.getScaledInstance(50, 50, Image.SCALE_SMOOTH); // New width and height
        ImageIcon icon = new ImageIcon(scaledIcon);
        JOptionPane.showMessageDialog(null, "Welcome to Skyline Chat", "Skyline Chat", JOptionPane.INFORMATION_MESSAGE, icon);

        JTextField username = new JTextField();
        JTextField password = new JPasswordField();
        Object[] usernamePasswordMessage = {"Username:", username, "Password:", password};
        Object[] loginOrCreateAccount = {"Exit", "Create an Account", "Login"};


        Socket socket = new Socket("localhost", 4241);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        boolean mostOuterCheck = true;

        do {
            int loginOrCreateAccountChoice = JOptionPane.showOptionDialog(null, "Do you want to login or create an account?", "Skyline Chat", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, icon, loginOrCreateAccount, null);
            int choice = Integer.parseInt(String.valueOf(loginOrCreateAccountChoice));

            switch (choice) {
                case 1 -> {
                    String user = "";
                    String pass = "";
                    boolean validUser = false;
                    boolean validPass = false;
                    boolean validUserAndPass = false;
                    do {
                        int option = JOptionPane.showConfirmDialog(null, usernamePasswordMessage, "Create an Account", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, icon);
                        if (option == JOptionPane.OK_OPTION) {
                            user = username.getText();
                            pass = password.getText();

                            if (!user.contains("-") && !user.isEmpty()) {
                                validUser = true;
                            } else {
                                JOptionPane.showMessageDialog(null, "InvalidUsername. Username either contained a \"-\" or was empty", "Skyline Chat", JOptionPane.INFORMATION_MESSAGE, icon);
                            }
                            if (!pass.contains("-") && !pass.isEmpty() && !pass.contains(" ")) {
                                validPass = true;
                            } else {
                                JOptionPane.showMessageDialog(null, "Invalid Password. Password either contains a \"-\" \" \"or is empty", "Skyline Chat", JOptionPane.INFORMATION_MESSAGE, icon);
                            }

                            if (validUser && validPass) {
                                bw.write("Username Create: " + user + "\n");
                                bw.flush();
                                if (br.readLine().equals("New User")) {
                                    validUserAndPass = true;
                                    bw.write("Password Create: " + pass + "\n");
                                    bw.flush();
                                    JOptionPane.showMessageDialog(null, "Account Creation Successful", "Skyline Chat", JOptionPane.INFORMATION_MESSAGE, icon);
                                    client.actionsAfterLogin(bw, br, socket);
                                } else {
                                    JOptionPane.showMessageDialog(null, "Username is in use, select a new username", "Skyline Chat", JOptionPane.INFORMATION_MESSAGE, icon);
                                }
                                mostOuterCheck = false;
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Account Creation canceled", "Skyline Chat", JOptionPane.INFORMATION_MESSAGE, icon);
                            break;
                        }

                    } while (!validUserAndPass);


                }

                case 2 -> {
                    boolean checkInValidInput = true;
                    while (checkInValidInput) {
                        int option = JOptionPane.showConfirmDialog(null, usernamePasswordMessage, "Login", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, icon);
                        if (option == JOptionPane.OK_OPTION) {
                            if (username.getText().isEmpty() || username.getText().contains("-") || username.getText().contains(" ")) {
                                JOptionPane.showMessageDialog(null, "Invalid Username. Contains a \"-\", \" \", or is empty", "Skyline Chat", JOptionPane.INFORMATION_MESSAGE, icon);
                            } else if (password.getText().isEmpty() || password.getText().contains("-") || password.getText().contains(" ")) {
                                JOptionPane.showMessageDialog(null, "Invalid Password. Contains a \"-\", \" \", or is empty", "Skyline Chat", JOptionPane.INFORMATION_MESSAGE, icon);
                            } else {
                                bw.write("Username Login: " + username.getText() + "\n");
                                bw.flush();
                                String pass = password.getText();
                                bw.write("Password Login: " + pass + "\n");
                                bw.flush();
                                String line = br.readLine();
                                if (line.equals("Logged In!")) {
                                    JOptionPane.showMessageDialog(null, "Login Successful. Welcome Old User", "Skyline Chat", JOptionPane.INFORMATION_MESSAGE, icon);
                                    checkInValidInput = false;
                                    mostOuterCheck = false;
                                    client.actionsAfterLogin(bw, br, socket);
                                } else if (line.equals("Created New User!")) {
                                    int newAccount = JOptionPane.showConfirmDialog(null, "The username and password you entered doesnt belong to any account. Would you like to create a new account with this information?", "Skyline Chat", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, icon);
                                    int intNewAccount = Integer.parseInt(String.valueOf(newAccount));
                                    if (intNewAccount == 0) {
                                        bw.write("yes\n");
                                        bw.flush();
                                        JOptionPane.showMessageDialog(null, "Login Successful. Welcome New User", "Skyline Chat", JOptionPane.INFORMATION_MESSAGE, icon);
                                        checkInValidInput = false;
                                        mostOuterCheck = false;
                                        client.actionsAfterLogin(bw, br, socket);
                                    } else {
                                        bw.write("no\n");
                                        bw.flush();
                                        checkInValidInput = false;
                                    }

                                } else if (line.equals("Wrong Password")) {
                                    JOptionPane.showMessageDialog(null, "Wrong Password. Please Try Again!", "Skyline Chat", JOptionPane.INFORMATION_MESSAGE, icon);
                                }
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Login canceled", "Skyline Chat", JOptionPane.INFORMATION_MESSAGE, icon);
                            break;
                        }
                    }
                }

                case 0 -> {
                    bw.write("Exit" + "\n");
                    bw.flush();
                    JOptionPane.showMessageDialog(null, "Goodbye!", "Skyline Chat", JOptionPane.INFORMATION_MESSAGE, icon);
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