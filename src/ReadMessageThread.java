import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Group PJ -- ReadMessageThread.java
 * <p>
 * This is the ReadMessageThread
 * (see more in the ReadMe)
 *
 * @author Pranav Neti, Ryan Sierra, Tanya Jain, Anay Misra - Lab Section 12
 * @version Nov 17, 2024
 */

public class ReadMessageThread implements Runnable {
    String first;
    String second;
    Socket socket;
    JTextPane textPane;
    JFrame frame;

    boolean exit = false;

    public ReadMessageThread(String user1, String user2, Socket socket, JTextPane textPane, JFrame frame) {
        first = (user1.compareTo(user2) > 0 ? user2 : user1);
        second = (user1.equals(first) ? user2 : user1);
        this.socket = socket;
        this.textPane = textPane;
        this.frame = frame;
    }

    @Override
    public void run() {
        try {
            while (true) {
                File f = new File("files/" + first + "-" + second + ".txt");
                String messageHistory = "";
                long lastModifiedTime = f.lastModified();
                boolean firstTime = true;
                if (f.exists()) {
                    BufferedReader br = new BufferedReader(new FileReader("files/" + first + "-" + second + ".txt"));
                    while (!exit) {
                        if(f.lastModified() > lastModifiedTime || firstTime) {
                            lastModifiedTime = f.lastModified();
                            messageHistory = "";
                            br = new BufferedReader(new FileReader(f));
                            String message;
                            while ((message = br.readLine()) != null) {
                                if(!message.isEmpty()) {
                                    String[] parts = message.split("-");
                                    System.out.println("THIS IS A MESSAGE LOOK AT ME HERE: " + message);
                                    if(!parts[2].equals(" ") && !parts[2].isEmpty()) {
                                        if(parts[2].startsWith("<p><img src='")) {
                                            messageHistory = messageHistory + "<p>" + parts[0] + ":</p>" + parts[2];
                                        } else {
                                            messageHistory = messageHistory + "<p>" + parts[0] + ": " + parts[2] + "</p>";
                                        }
                                    }
                                }
                                textPane.setContentType("text/html");
                                textPane.setText(messageHistory);
                                textPane.setCaretPosition(textPane.getDocument().getLength());
                                frame.repaint();
                            }
                            firstTime = false;
                        }
                    }
                    br.close();
                    System.out.println("closing socket");
                    break;
                } else {
                    BufferedWriter bw = new BufferedWriter(new FileWriter("files/" + first + "-" + second + ".txt"));
                    bw.write(first + "-" + second +  "-" + " " + "\n");
                    bw.flush();
                    bw.close();
                    BufferedReader br = new BufferedReader(new FileReader("files/" + first + "-" + second + ".txt"));
                    while (!exit) {
                        if(f.lastModified() > lastModifiedTime || firstTime) {
                            lastModifiedTime = f.lastModified();
                            messageHistory = "";
                            br = new BufferedReader(new FileReader(f));
                            String message;
                            while ((message = br.readLine()) != null) {
                                if(!message.isEmpty()) {
                                    String[] parts = message.split("-");
                                    System.out.println("THIS IS A MESSAGE LOOK AT ME HERE: " + message);
                                    if(!parts[2].equals(" ") && !parts[2].isEmpty()) {
                                        if(parts[2].startsWith("<p><img src='")) {
                                            messageHistory = messageHistory + "<p>" + parts[0] + ":</p>" + parts[2];
                                        } else {
                                            messageHistory = messageHistory + "<p>" + parts[0] + ": " + parts[2] + "</p>";
                                        }
                                    }
                                }
                                textPane.setContentType("text/html");
                                textPane.setText(messageHistory);
                                textPane.setCaretPosition(textPane.getDocument().getLength());
                                frame.repaint();
                            }
                            firstTime = false;
                        }
                    }
                    br.close();
                    System.out.println("closing socket");
                    break;
                }
            }

        } catch (IOException e) {
            System.out.println(" ");
        }
    }
}
