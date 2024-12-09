import org.w3c.dom.Text;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
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


public class GraphicalClient extends JComponent implements GraphicalClientInterface {
    private static final int SERVER_PORT = 4241;
    private static Object gateKeep = new Object();
    public String[] message = {""};
    public String[] receiver = {""};
    Thread read;
    public BufferedWriter writer;
    public BufferedReader reader;
    public JTextPane textPane;
    public JFrame frame;
    public String readLine = "";

    // MAIN CONTENT PANE BUTTONS
    private JButton setNameButton;
    private JButton setProfileDescriptionButton;
    private JButton setProfilePictureButton;
    private JButton addFriendButton;
    private JButton removeFriendButton;
    private JButton viewFriendButton;
    private JButton canReceiveFromButton;
    private JButton blockUserButton;
    private JButton unblockUserButton;
    private JButton viewBlockedUsersButton;
    private JButton sendMessageButton;
    private JButton viewAllUsersButton;
    private JButton enterButton;
    private JButton deleteButton;
    private JButton quitButton;
    ImageIcon icon;


    public void actionsAfterLogin(BufferedWriter bw, BufferedReader br, Socket socket, GraphicalClient client, String userNAME) {

        ImageIcon originalIcon = new ImageIcon("files/icon.png");
        Image image = originalIcon.getImage();
        Image scaledIcon = image.getScaledInstance(50, 50, Image.SCALE_SMOOTH); // New width and height
        icon = new ImageIcon(scaledIcon);

        frame = new JFrame("Skyline Chat");
        frame.setLayout(null);
        frame.setSize(900, 500);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        String username = "";
        String profileDescription = "";
        String profilePicture = "";
        try {
            bw.write("Profile Information: \n");
            bw.flush();
            username = br.readLine();
            profileDescription = br.readLine();
            profilePicture = br.readLine();
        } catch (IOException e1) {
            System.out.println(e1.getMessage());
        }

        String mainText = "";
        if (username == null || username.trim().isEmpty()) {
            mainText = "Skyline Chat";
            username = "";
        } else {
            mainText = username;
        }

        Image scaled;
        ImageIcon mainIcon;
        ImageIcon pfpIcon;
        Image pfpImage;
        if (profilePicture != null && !profilePicture.trim().isEmpty()) {
            pfpIcon = new ImageIcon(profilePicture);
            pfpImage = pfpIcon.getImage();
            scaled = pfpImage.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            mainIcon = new ImageIcon(scaled);
        } else {
            pfpIcon = new ImageIcon("files/icon.png");
            pfpImage = pfpIcon.getImage();
            scaled = pfpImage.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            mainIcon = new ImageIcon(scaled);
        }


        JPanel imageTextLabel = new JPanel();
        imageTextLabel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        imageTextLabel.setPreferredSize(new Dimension(200, 300));


        JLabel skylineImageLabel = new JLabel(mainIcon);

        JLabel skylineChatLabel = new JLabel(mainText);
        skylineChatLabel.setFont(new Font("Arial", Font.BOLD, 20));

        imageTextLabel.add(skylineImageLabel);
        imageTextLabel.add(skylineChatLabel);
        imageTextLabel.setBounds(10, 10, 200, 200);
        imageTextLabel.setBackground(new Color(210, 246, 253));
        frame.add(imageTextLabel);


        JPanel nameButtonPanel = new JPanel();
        nameButtonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

        JLabel nameLabel = new JLabel(username);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));

        setNameButton = new JButton("Set Name");
        setNameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = JOptionPane.showInputDialog(null, "What do you want to set your name to?",
                        "Set Name", JOptionPane.PLAIN_MESSAGE);
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
                    JOptionPane.showMessageDialog(null, "Name updated successfully.",
                            "Set Name", JOptionPane.INFORMATION_MESSAGE, icon);
                    nameLabel.setText(name);
                    skylineChatLabel.setText(name);
                    frame.repaint();
                } else {
                    JOptionPane.showMessageDialog(null, "Name either contained a " +
                            "\"-\" or was empty", "Set Name", JOptionPane.ERROR_MESSAGE, icon);
                }
            }
        });

        nameButtonPanel.add(nameLabel);
        nameButtonPanel.add(setNameButton);
        nameButtonPanel.setBounds(200, 32, 200, 100);
        nameButtonPanel.setBackground(new Color(210, 246, 253));
        frame.add(nameButtonPanel);


        JPanel descriptionButtonPanel = new JPanel();
        descriptionButtonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

        int size = 20;
        String[] breakProfileDescription = profileDescription.split(" ");
        profileDescription = "<html>";
        for (int i = 0; i < breakProfileDescription.length; i++) {
            profileDescription = profileDescription + breakProfileDescription[i] + " ";
            if (i % 5 == 0) {
                size -= 4;
                profileDescription = profileDescription + "<br>";
            }
        }
        profileDescription = profileDescription + "</html>";
        JLabel descriptionLabel = new JLabel(profileDescription);
        descriptionLabel.setFont(new Font("Arial", Font.BOLD, size));


        setProfileDescriptionButton = new JButton("Set Profile Description");
        setProfileDescriptionButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String description = JOptionPane.showInputDialog(null,
                        "Enter profile description: ", "Set Profile Description", JOptionPane.PLAIN_MESSAGE);
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
                    JOptionPane.showMessageDialog(null, "Profile description updated.",
                            "Set Profile Description", JOptionPane.INFORMATION_MESSAGE, icon);
                    int size = 20;
                    String[] breakProfileDescription = description.split(" ");
                    description = "<html>";
                    for (int i = 0; i < breakProfileDescription.length; i++) {
                        description = description + breakProfileDescription[i] + " ";
                        if (i % 5 == 0 && i != 0) {
                            size -= 4;
                            description = description + "<br>";
                        }
                    }
                    description = description + "</html>";
                    descriptionLabel.setText(description);
                    descriptionLabel.setFont(new Font("Arial", Font.BOLD, size));
                    descriptionButtonPanel.setBounds(400, 12 + size, 220, 150);
                    frame.repaint();
                } else {
                    JOptionPane.showMessageDialog(null, "Profile descriptions " +
                            "either contained a \"-\" or was empty", "Set Profile Description",
                            JOptionPane.ERROR_MESSAGE, icon);
                }
            }
        });

        descriptionButtonPanel.add(descriptionLabel);
        descriptionButtonPanel.add(setProfileDescriptionButton);
        descriptionButtonPanel.setBounds(400, 12 + size, 200, 150);
        descriptionButtonPanel.setBackground(new Color(210, 246, 253));
        frame.add(descriptionButtonPanel);


        JPanel pfpButtonPanel = new JPanel();
        pfpButtonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

        pfpImage = pfpIcon.getImage();
        scaled = pfpImage.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        mainIcon = new ImageIcon(scaled);
        JLabel pfpLabel = new JLabel(mainIcon);
        pfpLabel.setFont(new Font("Arial", Font.BOLD, 20));

        setProfilePictureButton = new JButton("Set Profile Picture");
        setProfilePictureButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String imagePath = JOptionPane.showInputDialog(null,
                        "Please enter the file path to the image you want to" +
                                " set as your profile picture", "Set Profile Picture", JOptionPane.PLAIN_MESSAGE);
                //to test, go copy the absolute path from any of the images
                if (imagePath == null || imagePath.trim().isEmpty()) {
                    return;
                }
                if (imagePath.contains("-")) {
                    do {
                        imagePath = JOptionPane.showInputDialog(null, "Your input contained " +
                                "a \"-\". Please enter the file path to the image you want to set as " +
                                "your profile picture", "Set Profile Picture", JOptionPane.PLAIN_MESSAGE);
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
                        File imageFile = new File(imagePath);
                        BufferedImage image = ImageIO.read(imageFile);

                        Image scaledImage = image.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                        ImageIcon imageIcon = new ImageIcon(scaledImage);
                        JLabel imageLabel = new JLabel(imageIcon);
                        imageLabel.setPreferredSize(new Dimension(100, 100));

                        JOptionPane.showMessageDialog(null, imageLabel, "New Profile Picture",
                                JOptionPane.PLAIN_MESSAGE, icon);

                        skylineImageLabel.setIcon(imageIcon);
                        scaledImage = image.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                        imageIcon = new ImageIcon(scaledImage);
                        pfpLabel.setIcon(imageIcon);

                        frame.repaint();

                    } else {
                        JOptionPane.showMessageDialog(null, choice, "Set Profile Picture",
                                JOptionPane.ERROR_MESSAGE, icon);
                    }
                } catch (IOException e1) {
                    System.out.println(e1.getMessage());
                }
            }
        });

        pfpButtonPanel.add(pfpLabel);
        pfpButtonPanel.add(setProfilePictureButton);
        pfpButtonPanel.setBounds(600, 1, 200, 150);
        pfpButtonPanel.setBackground(new Color(210, 246, 253));
        frame.add(pfpButtonPanel);


        JPanel friendButtonPanel = new JPanel();
        friendButtonPanel.setLayout(new BoxLayout(friendButtonPanel, BoxLayout.X_AXIS));

        addFriendButton = new JButton("Add Friend");
        addFriendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = "";
                String newFriend = JOptionPane.showInputDialog(null,
                        "Enter username to add as friend: ", "Add Friend", JOptionPane.PLAIN_MESSAGE);
                if (newFriend != null && !newFriend.trim().isEmpty()) {
                    try {
                        bw.write("Give username\n");
                        bw.flush();
                        username = br.readLine();
                        if (username.equals(newFriend)) {
                            JOptionPane.showMessageDialog(null, "You cannot add yourself as a friend",
                                    "Add Friend", JOptionPane.ERROR_MESSAGE, icon);
                        } else {
                            bw.write("Add Friend: " + newFriend + "\n");
                            bw.flush();
                            String check = br.readLine();
                            if (check.equals("Successful")) {
                                bw.write("Unblock User: " + newFriend + "\n");
                                bw.flush();
                                readLine = br.readLine();
                                JOptionPane.showMessageDialog(null, newFriend + " has been added to friends",
                                        "Add Friend", JOptionPane.INFORMATION_MESSAGE, icon);
                            } else {
                                JOptionPane.showMessageDialog(null, newFriend + " doesn't exist", "Add Friend",
                                        JOptionPane.INFORMATION_MESSAGE, icon);
                            }
                        }
                    } catch (IOException e1) {
                        throw new RuntimeException(e1);
                    }
                }
            }
        });
        removeFriendButton = new JButton("Remove Friend");
        removeFriendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = "";
                String oldFriend = JOptionPane.showInputDialog(null, "Enter username to remove from friends: ",
                        "Remove Friend", JOptionPane.PLAIN_MESSAGE);
                if (oldFriend != null && !oldFriend.trim().isEmpty()) {
                    try {
                        bw.write("Give username\n");
                        bw.flush();
                        username = br.readLine();
                        if (username.equals(oldFriend)) {
                            JOptionPane.showMessageDialog(null, "You cannot remove yourself as a friend",
                                    "Add Friend", JOptionPane.ERROR_MESSAGE, icon);
                        } else {
                            bw.write("Remove Friend: " + oldFriend + "\n");
                            bw.flush();
                            String check = br.readLine();
                            if (check.equals("Successful")) {
                                JOptionPane.showMessageDialog(null, oldFriend + " has been removed from friends",
                                        "Remove Friend", JOptionPane.INFORMATION_MESSAGE, icon);
                            } else {
                                JOptionPane.showMessageDialog(null, oldFriend + " doesn't exist or isn't in your" +
                                        " current friends list", "Remove Friend",
                                        JOptionPane.INFORMATION_MESSAGE, icon);
                            }
                        }
                    } catch (IOException e1) {
                        throw new RuntimeException(e1);
                    }
                }
            }
        });

        viewFriendButton = new JButton("View Friends");
        viewFriendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    bw.write("Friend List: " + "\n");
                    bw.flush();
                    String[] friends = br.readLine().split(" ");
                    ArrayList<JMenuItem> menuItems = new ArrayList<>();
                    boolean added = false;
                    JPopupMenu popupMenu = new JPopupMenu();
                    for (String friend : friends) {
                        JMenuItem menuItem = new JMenuItem(friend);
                        popupMenu.add(menuItem);
                        menuItems.add(menuItem);
                        added = true;
                    }
                    if (!added) {
                        popupMenu.add(new JMenuItem("No Current Friends"));
                    } else {
                        for (JMenuItem menuItem : menuItems) {
                            menuItem.addActionListener(input -> {
                                try {
                                    bw.write("Check Profile of: " + menuItem.getText() + "\n");
                                    bw.flush();
                                    String name = br.readLine();
                                    String desc = br.readLine();
                                    String picPath = br.readLine();
                                    if (picPath == null || picPath.trim().isEmpty()) {
                                        JOptionPane.showMessageDialog(null, "Name: " + name + "\n" +
                                                "Description: " + desc + "\n" + "Profile Picture: None Set" + "\n",
                                                "View Friends", JOptionPane.PLAIN_MESSAGE, icon);
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

                                        JOptionPane.showMessageDialog(null, panel, "View Friends",
                                                JOptionPane.PLAIN_MESSAGE, icon);
                                    }

                                } catch (IOException ex) {
                                    System.out.println(ex.getMessage());
                                }
                            });
                        }
                    }
                    popupMenu.show(viewFriendButton, viewFriendButton.getX(), viewFriendButton.getHeight());
                    frame.repaint();

                } catch (IOException e1) {
                    throw new RuntimeException(e1);
                }
            }
        });
        canReceiveFromButton = new JButton("Change Receiving Options");
        canReceiveFromButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int num = JOptionPane.showConfirmDialog(null,
                        "Do you want to receive messages from anyone (yes/no)",
                        "Change Receiving Options", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE, icon);
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
                        JOptionPane.showMessageDialog(null,
                                "You can now receive messages from everyone", "Change Receiving Options",
                                JOptionPane.INFORMATION_MESSAGE, icon);
                    } else {
                        JOptionPane.showMessageDialog(null, "You can now receive messages from only friends",
                                "Change Receiving Options", JOptionPane.INFORMATION_MESSAGE, icon);
                    }
                }
            }
        });

        JLabel friendsLabel = new JLabel("Friends: ");
        friendsLabel.setFont(new Font("Arial", Font.BOLD, 20));
        friendButtonPanel.add(friendsLabel);
        friendButtonPanel.add(viewFriendButton);
        friendButtonPanel.add(addFriendButton);
        friendButtonPanel.add(removeFriendButton);
        friendButtonPanel.add(canReceiveFromButton);
        friendButtonPanel.setBounds(120, 200, 700, 90);
        friendButtonPanel.setBackground(new Color(210, 246, 253));
        frame.add(friendButtonPanel);


        JPanel blockButtonPanel = new JPanel();
        blockButtonPanel.setLayout(new BoxLayout(blockButtonPanel, BoxLayout.X_AXIS));

        blockUserButton = new JButton("Block User");
        blockUserButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String blockUser = JOptionPane.showInputDialog(null, "Enter username to block: ",
                        "Block User", JOptionPane.PLAIN_MESSAGE);
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
                            JOptionPane.showMessageDialog(null, "You cannot block yourself",
                                    "Block User", JOptionPane.ERROR_MESSAGE, icon);
                        } else {
                            bw.write("Block User: " + blockUser + "\n");
                            bw.flush();
                            String check = br.readLine();
                            if (check.equals("Successful")) {
                                bw.write("Remove Friend: " + blockUser + "\n");
                                bw.flush();
                                readLine = br.readLine();
                                JOptionPane.showMessageDialog(null, blockUser + " has been blocked",
                                        "Block User", JOptionPane.INFORMATION_MESSAGE, icon);
                            } else if (check.equals("Failed")) {
                                JOptionPane.showMessageDialog(null, blockUser + " doesnt exist",
                                        "Block User", JOptionPane.INFORMATION_MESSAGE, icon);
                            }
                        }
                    } catch (IOException e1) {
                        System.out.println(e1.getMessage());
                    }
                }
            }
        });
        unblockUserButton = new JButton("Unblock User");
        unblockUserButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = "";
                String unblockUser = JOptionPane.showInputDialog(null, "Enter username to unblock: ",
                        "Block User", JOptionPane.PLAIN_MESSAGE);
                if (unblockUser != null && !unblockUser.trim().isEmpty()) {
                    try {
                        try {
                            bw.write("Give username\n");
                            bw.flush();
                            username = br.readLine();
                        } catch (IOException e1) {
                            System.out.println(e1.getMessage());
                        }

                        if (username.equals(unblockUser)) {
                            JOptionPane.showMessageDialog(null, "You Cannot Block Yourself",
                                    "Block User", JOptionPane.INFORMATION_MESSAGE, icon);
                        } else {
                            bw.write("Unblock User: " + unblockUser + "\n");
                            bw.flush();
                            String check = br.readLine();
                            if (check.equals("Successful")) {
                                JOptionPane.showMessageDialog(null, unblockUser + " has been unblocked",
                                        "Block User", JOptionPane.INFORMATION_MESSAGE, icon);
                            } else if (check.equals("Failed")) {
                                JOptionPane.showMessageDialog(null, unblockUser + " doesnt exist",
                                        "Block User", JOptionPane.INFORMATION_MESSAGE, icon);
                            }
                        }

                    } catch (IOException e1) {
                        throw new RuntimeException(e1);
                    }
                }
            }
        });
        viewBlockedUsersButton = new JButton("View Blocked Users");
        viewBlockedUsersButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    bw.write("Blocked Users: " + "\n");
                    bw.flush();
                    String[] blockedUsers = br.readLine().split(" ");
                    ArrayList<JMenuItem> menuItems = new ArrayList<>();
                    boolean added = false;
                    JPopupMenu popupMenu = new JPopupMenu();
                    for (String blockUser : blockedUsers) {
                        if (blockUser != null && !blockUser.trim().isEmpty()) {
                            JMenuItem menuItem = new JMenuItem(blockUser);
                            popupMenu.add(menuItem);
                            menuItems.add(menuItem);
                            added = true;
                        }
                    }
                    if (!added) {
                        popupMenu.add(new JMenuItem("No One Is Blocked"));
                    } else {
                        for (JMenuItem menuItem : menuItems) {
                            menuItem.addActionListener(input -> {
                                try {
                                    bw.write("Check Profile of: " + menuItem.getText() + "\n");
                                    bw.flush();
                                    String name = br.readLine();
                                    String desc = br.readLine();
                                    String picPath = br.readLine();
                                    if (picPath == null || picPath.trim().isEmpty()) {
                                        JOptionPane.showMessageDialog(null, "Name: " + name + "\n" +
                                                "Description: " + desc + "\n" + "Profile Picture: None Set" + "\n",
                                                "View Friends", JOptionPane.PLAIN_MESSAGE, icon);
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

                                        JOptionPane.showMessageDialog(null, panel, "View Friends",
                                                JOptionPane.PLAIN_MESSAGE, icon);
                                    }

                                } catch (IOException ex) {
                                    System.out.println(ex.getMessage());
                                }
                            });
                        }
                    }
                    popupMenu.show(viewBlockedUsersButton, viewBlockedUsersButton.getX(), viewBlockedUsersButton.getHeight());
                    frame.repaint();

                } catch (IOException e1) {
                    throw new RuntimeException(e1);
                }
            }
        });

        JLabel blockLabel = new JLabel("Blocked Users: ");
        blockLabel.setFont(new Font("Arial", Font.BOLD, 20));
        blockButtonPanel.add(blockLabel);
        blockButtonPanel.add(viewBlockedUsersButton);
        blockButtonPanel.add(blockUserButton);
        blockButtonPanel.add(unblockUserButton);
        blockButtonPanel.setBounds(120, 250, 600, 120);
        blockButtonPanel.setBackground(new Color(210, 246, 253));
        frame.add(blockButtonPanel);


        sendMessageButton = new JButton("Message User");
        sendMessageButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String receiverInput = JOptionPane.showInputDialog(null, "Enter The User You Want To Message",
                        "Select User", JOptionPane.PLAIN_MESSAGE);
                if (receiverInput != null && !receiverInput.trim().isEmpty()) {
                    try {
                        String username = "";
                        try {
                            bw.write("Give username\n");
                            bw.flush();
                            username = br.readLine();
                        } catch (IOException e1) {
                            System.out.println(e1.getMessage());
                        }
                        if (username.equals(receiverInput)) {
                            JOptionPane.showMessageDialog(null, "You cannot message yourself",
                                    "Select User", JOptionPane.ERROR_MESSAGE, icon);
                        } else {
                            bw.write("Check Block/Friends: " + receiverInput + "\n");
                            bw.flush();
                            String choice = br.readLine();
                            if (choice.equals("Can message")) {
                                JOptionPane.showMessageDialog(null, "Entered DM",
                                        "Select User", JOptionPane.INFORMATION_MESSAGE, icon);
                                receiver[0] = receiverInput;
                                client.actionsWithinDM(bw, br, socket, receiverInput, client, userNAME);
                            } else {
                                JOptionPane.showMessageDialog(null, choice, "Select User",
                                        JOptionPane.ERROR_MESSAGE, icon);
                            }
                        }
                    } catch (IOException e1) {
                        throw new RuntimeException(e1);
                    }
                }
            }
        });


        JPanel messagingPanel = new JPanel();
        messagingPanel.setLayout(new BoxLayout(messagingPanel, BoxLayout.X_AXIS));

        JLabel messageLabel = new JLabel("Send Message: ");
        messageLabel.setFont(new Font("Arial", Font.BOLD, 20));

        viewAllUsersButton = new JButton("View All Users");
        viewAllUsersButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    bw.write("View All Users\n");
                    bw.flush();
                    String[] allUsers = br.readLine().split("-");
                    Arrays.sort(allUsers);

                    JList<String> userList = new JList<>(allUsers);
                    userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

                    userList.addListSelectionListener(event -> {
                        if (!event.getValueIsAdjusting()) {
                            String selectedUser = userList.getSelectedValue();
                            if (selectedUser != null) {
                                try {
                                    bw.write("Check Profile of: " + selectedUser + "\n");
                                    bw.flush();
                                    String name = br.readLine();
                                    String desc = br.readLine();
                                    String picPath = br.readLine();
                                    if (picPath == null || picPath.trim().isEmpty()) {
                                        JOptionPane.showMessageDialog(null, "Name: "
                                                + name + "\n" + "Description: " + desc + "\n" + "Profile Picture: " +
                                                "None Set", "View Friends", JOptionPane.PLAIN_MESSAGE, icon);
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

                                        JOptionPane.showMessageDialog(null, panel, "View Friends",
                                                JOptionPane.PLAIN_MESSAGE, icon);
                                    }
                                } catch (IOException ex) {
                                    System.out.println(ex.getMessage());
                                }
                            }
                        }
                    });

                    JScrollPane scrollPane = new JScrollPane(userList);
                    scrollPane.setPreferredSize(new Dimension(200, 100));

                    JPopupMenu popupMenu = new JPopupMenu();
                    popupMenu.add(scrollPane);

                    popupMenu.show(viewAllUsersButton, 0, viewAllUsersButton.getHeight());
                } catch (IOException e1) {
                    System.out.println(e1.getMessage());
                }
            }
        });


        messagingPanel.add(messageLabel);
        messagingPanel.add(sendMessageButton);
        messagingPanel.add(viewAllUsersButton);
        messagingPanel.setBounds(220, 326, 600, 120);
        messagingPanel.setBackground(new Color(210, 246, 253));
        frame.add(messagingPanel);

        frame.setVisible(true);
        frame.repaint();

        frame.getContentPane().setBackground(new Color(210, 246, 253));
        frame.setVisible(true);
    }

    public void actionsWithinDM(BufferedWriter bw, BufferedReader br, Socket socket,
                                String person, GraphicalClient client, String userNAME) {
        reader = br;
        writer = bw;

        frame = new JFrame("Messages: " + person);
        frame.setLayout(null);
        frame.setSize(400, 500);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // Prevent auto-closing the frame
        frame.setBackground(new Color(210, 246, 253));
        frame.getContentPane().setBackground(new Color(210, 246, 253));
        frame.setResizable(false);
        frame.setVisible(true);

        // Add WindowListener to detect window closing
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                setNameButton.setEnabled(true);
                setProfileDescriptionButton.setEnabled(true);
                setProfilePictureButton.setEnabled(true);
                addFriendButton.setEnabled(true);
                removeFriendButton.setEnabled(true);
                viewFriendButton.setEnabled(true);
                canReceiveFromButton.setEnabled(true);
                blockUserButton.setEnabled(true);
                unblockUserButton.setEnabled(true);
                viewBlockedUsersButton.setEnabled(true);
                sendMessageButton.setEnabled(true);
                viewAllUsersButton.setEnabled(true);
                try {
                    bw.write("quit messaging\n");
                    bw.flush();
                } catch (Exception error) {
                    System.out.println(error.getMessage());
                }
                frame.dispose();
            }
        });

        // Disable buttons initially
        setNameButton.setEnabled(false);
        setProfileDescriptionButton.setEnabled(false);
        setProfilePictureButton.setEnabled(false);
        addFriendButton.setEnabled(false);
        removeFriendButton.setEnabled(false);
        viewFriendButton.setEnabled(false);
        canReceiveFromButton.setEnabled(false);
        blockUserButton.setEnabled(false);
        unblockUserButton.setEnabled(false);
        viewBlockedUsersButton.setEnabled(false);
        sendMessageButton.setEnabled(false);
        viewAllUsersButton.setEnabled(false);

        textPane = new JTextPane();
        textPane.setEditable(false);
//        textPane.setBackground(new Color(210, 246, 253));
        textPane.setText("Your Chat is empty, use the enter button to Send a Message" +
                "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        JScrollPane scrollPane = new JScrollPane(textPane);
        frame.setBackground(new Color(210, 246, 253));
        scrollPane.setBounds(10, 10, 380, 400);
        frame.add(scrollPane);

        JTextField textField = new JTextField();
        textField.setEditable(true);
        textField.setText("Click the \"Send\" Button to send a message from this field");
        textField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (textField.getText().equals("Click the \"Send\" Button to send a message from this field")) {
                    textField.setText("");
                    frame.repaint();
                }
            }
        });

        textField.setBounds(10, 410, 380, 35);
        frame.add(textField);

        enterButton = new JButton("Send");
        enterButton.setBounds(5, 445, 130, 25);
        frame.add(enterButton);

        deleteButton = new JButton("Delete");
        deleteButton.setBounds(135, 445, 130, 25);
        frame.add(deleteButton);

        quitButton = new JButton("Exit DM");
        quitButton.setBounds(265, 445, 130, 25);
        frame.add(quitButton);

        enterButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String initialCheck = "";
                if (textField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "You Can't Send An Empty Message",
                            "Send Message", JOptionPane.INFORMATION_MESSAGE, icon);
                } else {
                    Object[] options = {"Text", "Image"};
                    int typeInt = JOptionPane.showOptionDialog(null, "What Message Type Is This (Image/Text)?",
                            "Send Message", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, icon, options, options[0]);
                    if (typeInt != -1) {
                        try {
                            if (typeInt == 1) {
                                String valid = "";
                                File imageFile = new File(textField.getText());
                                BufferedImage img = ImageIO.read(imageFile);
                                if (img == null) {
                                    valid = "Invalid Image";
                                } else {
                                    valid = "Valid Image";
                                }

                                if (valid.equals("Valid Image")) {
                                    String field = textField.getText();
                                    String og = field;
                                    if (field.startsWith("file://")) {
                                        field = field.substring(7);
                                    }

                                    String fileName = field.substring(field.lastIndexOf("/") + 1);
                                    File f1 = new File(field);
                                    File f2 = new File("./files/" + "<text>" + fileName);

                                    field = "files/" + "<text>" + fileName;
                                    try {
                                        Files.copy(f1.toPath(), f2.toPath(), StandardCopyOption.COPY_ATTRIBUTES);
                                    } catch (FileAlreadyExistsException e1) {
                                        e1.printStackTrace();
                                    }

                                    message[0] = "<p><img src='" + field.replaceAll(" ", "%20") +
                                            "' alt='' width='300' height='200'>" + "</p>";

                                    DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                                    BufferedImage image = ImageIO.read(f1);
                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    byte[] imageBytes = baos.toByteArray();
                                    dos.writeInt(imageBytes.length);
                                    dos.flush();
                                    if (f1.getAbsolutePath().endsWith("jpg") || f1.getAbsolutePath().endsWith("JPG")) {
                                        ImageIO.write(image, "jpg", baos);
                                    } else if (f1.getAbsolutePath().endsWith("png") ||
                                            f1.getAbsolutePath().endsWith("PNG")) {
                                        ImageIO.write(image, "png", baos);
                                    }

                                    baos.flush();
                                    dos.write(imageBytes);
                                    dos.flush();

                                    bw.write("Message: " + receiver[0] + "-" + message[0] + "-" + "Image" + "\n");
                                    bw.flush();
                                } else {
                                    JOptionPane.showMessageDialog(null, "The File Path You Entered Was Invalid",
                                            "Send Message", JOptionPane.ERROR_MESSAGE, icon);
                                }
                            } else {
                                message[0] = textField.getText();
                                bw.write("Message: " + receiver[0] + "-" + message[0] + "-" + "Text" + "\n");
                                bw.flush();
                            }
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String initialCheck = "";
                if (receiver[0].equals("")) {
                    JOptionPane.showMessageDialog(null, "Please Click Send Message To Get Started",
                            "Delete Message", JOptionPane.INFORMATION_MESSAGE, icon);
                } else {
                    Object[] options = {"Text", "Image"};
                    int type = JOptionPane.showOptionDialog(null, "What Message Type Is This (Image/Text)?",
                            "Delete Message", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, icon, options, options[0]);
                    if (textField.getText().trim().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Cant Delete An Empty Message",
                                "Delete Message", JOptionPane.ERROR_MESSAGE, icon);
                    } else if (type != -1) {
                        message[0] = textField.getText();
                        String result = "";
                        try {
                            String typeStr = "";
                            if (type == 1) {
                                typeStr = "Image";
                            } else {
                                typeStr = "Text";
                            }
                            bw.write("DELETE: " + receiver[0] + "-" + message[0] + "-" + typeStr + "\n");
                            bw.flush();
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
            }
        });

        quitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setNameButton.setEnabled(true);
                setProfileDescriptionButton.setEnabled(true);
                setProfilePictureButton.setEnabled(true);
                addFriendButton.setEnabled(true);
                removeFriendButton.setEnabled(true);
                viewFriendButton.setEnabled(true);
                canReceiveFromButton.setEnabled(true);
                blockUserButton.setEnabled(true);
                unblockUserButton.setEnabled(true);
                viewBlockedUsersButton.setEnabled(true);
                sendMessageButton.setEnabled(true);
                viewAllUsersButton.setEnabled(true);
                try {
                    bw.write("quit messaging\n");
                    bw.flush();
                } catch (Exception error) {
                    System.out.println(error.getMessage());
                }
                frame.dispose();
            }
        });

        String first = (userNAME.compareTo(person) > 0 ? person : userNAME);
        String second = (userNAME.equals(first) ? person : userNAME);
        try {
            bw.write("SEND CHAT LOG: " + first + "-" + second + ".txt\n");
            bw.flush();
            Thread t = new Thread(new GraphicalClientReader(br, bw, textPane, frame, socket));
            t.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        GraphicalClient client = new GraphicalClient();
        ImageIcon originalIcon = new ImageIcon("files/icon.png");
        Image image = originalIcon.getImage();
        Image scaledIcon = image.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        ImageIcon icon = new ImageIcon(scaledIcon);
        JOptionPane.showMessageDialog(null, "Welcome to Skyline Chat",
                "Skyline Chat", JOptionPane.INFORMATION_MESSAGE, icon);

        JTextField username = new JTextField();
        JTextField password = new JPasswordField();
        Object[] usernamePasswordMessage = {"Username:", username, "Password:", password};
        Object[] loginOrCreateAccount = {"Exit", "Create an Account", "Login"};


        Socket socket = new Socket("localhost", 4241);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        boolean mostOuterCheck = true;

        do {
            int loginOrCreateAccountChoice = JOptionPane.showOptionDialog(null,
                    "Do you want to login or create an account?", "Skyline Chat", JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE, icon, loginOrCreateAccount, null);
            int choice = Integer.parseInt(String.valueOf(loginOrCreateAccountChoice));

            switch (choice) {
                case 1 -> {
                    String user = "";
                    String pass = "";
                    boolean validUser = false;
                    boolean validPass = false;
                    boolean validUserAndPass = false;
                    do {
                        int option = JOptionPane.showConfirmDialog(null, usernamePasswordMessage,
                                "Create an Account", JOptionPane.OK_CANCEL_OPTION,
                                JOptionPane.INFORMATION_MESSAGE, icon);
                        if (option == JOptionPane.OK_OPTION) {
                            user = username.getText();
                            pass = password.getText();

                            if (!user.contains("-") && !user.isEmpty()) {
                                validUser = true;
                            } else {
                                JOptionPane.showMessageDialog(null, "InvalidUsername. Username either contained a" +
                                        " \"-\" or was empty", "Skyline Chat", JOptionPane.INFORMATION_MESSAGE, icon);
                            }
                            if (!pass.contains("-") && !pass.isEmpty() && !pass.contains(" ")) {
                                validPass = true;
                            } else {
                                JOptionPane.showMessageDialog(null, "Invalid Password. Password either" +
                                        " contains a \"-\" \" \"or is empty", "Skyline Chat",
                                        JOptionPane.INFORMATION_MESSAGE, icon);
                            }

                            if (validUser && validPass) {
                                bw.write("Username Create: " + user + "\n");
                                bw.flush();
                                if (br.readLine().equals("New User")) {
                                    validUserAndPass = true;
                                    bw.write("Password Create: " + pass + "\n");
                                    bw.flush();
                                    JOptionPane.showMessageDialog(null, "Account Creation Successful",
                                            "Skyline Chat", JOptionPane.INFORMATION_MESSAGE, icon);
                                    client.actionsAfterLogin(bw, br, socket, client, username.getText());
                                } else {
                                    JOptionPane.showMessageDialog(null, "Username is in use, select a new username",
                                            "Skyline Chat", JOptionPane.INFORMATION_MESSAGE, icon);
                                }
                                mostOuterCheck = false;
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Account Creation canceled",
                                    "Skyline Chat", JOptionPane.INFORMATION_MESSAGE, icon);
                            break;
                        }

                    } while (!validUserAndPass);


                }

                case 2 -> {
                    boolean checkInValidInput = true;
                    while (checkInValidInput) {
                        int option = JOptionPane.showConfirmDialog(null, usernamePasswordMessage,
                                "Login", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, icon);
                        if (option == JOptionPane.OK_OPTION) {
                            if (username.getText().isEmpty() || username.getText().contains("-")
                                    || username.getText().contains(" ")) {
                                JOptionPane.showMessageDialog(null, "Invalid Username." +
                                        " Contains a \"-\", \" \", or is empty", "Skyline Chat",
                                        JOptionPane.INFORMATION_MESSAGE, icon);
                            } else if (password.getText().isEmpty() || password.getText().contains("-")
                                    || password.getText().contains(" ")) {
                                JOptionPane.showMessageDialog(null, "Invalid Password. " +
                                        "Contains a \"-\", \" \", or is empty", "Skyline Chat",
                                        JOptionPane.INFORMATION_MESSAGE, icon);
                            } else {
                                bw.write("Username Login: " + username.getText() + "\n");
                                bw.flush();
                                String pass = password.getText();
                                bw.write("Password Login: " + pass + "\n");
                                bw.flush();
                                String line = br.readLine();
                                if (line.equals("Logged In!")) {
                                    JOptionPane.showMessageDialog(null, "Login Successful. Welcome Old User",
                                            "Skyline Chat", JOptionPane.INFORMATION_MESSAGE, icon);
                                    checkInValidInput = false;
                                    mostOuterCheck = false;
                                    client.actionsAfterLogin(bw, br, socket, client, username.getText());
                                } else if (line.equals("Created New User!")) {
                                    int newAccount = JOptionPane.showConfirmDialog(null,
                                            "The username and password you entered doesnt belong to any account." +
                                                    " Would you like to create a new account with this information?",
                                            "Skyline Chat", JOptionPane.YES_NO_OPTION,
                                            JOptionPane.QUESTION_MESSAGE, icon);
                                    int intNewAccount = Integer.parseInt(String.valueOf(newAccount));
                                    if (intNewAccount == 0) {
                                        bw.write("yes\n");
                                        bw.flush();
                                        JOptionPane.showMessageDialog(null, "Login Successful." +
                                                " Welcome New User", "Skyline Chat",
                                                JOptionPane.INFORMATION_MESSAGE, icon);
                                        checkInValidInput = false;
                                        mostOuterCheck = false;
                                        client.actionsAfterLogin(bw, br, socket, client, username.getText());
                                    } else {
                                        bw.write("no\n");
                                        bw.flush();
                                        checkInValidInput = false;
                                    }
                                } else if (line.equals("Wrong Password")) {
                                    JOptionPane.showMessageDialog(null, "Wrong Password." +
                                            " Please Try Again!", "Skyline Chat",
                                            JOptionPane.INFORMATION_MESSAGE, icon);
                                } else {
                                    JOptionPane.showMessageDialog(null, "You're here",
                                            "Skyline Chat", JOptionPane.INFORMATION_MESSAGE, icon);
                                }
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Login canceled",
                                    "Skyline Chat", JOptionPane.INFORMATION_MESSAGE, icon);
                            break;
                        }
                    }
                }

                case 0 -> {
                    bw.write("Exit" + "\n");
                    bw.flush();
                    JOptionPane.showMessageDialog(null, "Goodbye!",
                            "Skyline Chat", JOptionPane.INFORMATION_MESSAGE, icon);
                    mostOuterCheck = false;
                    System.exit(0);
                }

                default -> {
                    System.exit(0);
                }
            }
        } while (mostOuterCheck);
    }

}