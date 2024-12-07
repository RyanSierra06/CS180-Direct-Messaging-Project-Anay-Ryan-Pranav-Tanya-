import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;

/**
 * Group PJ -- ReadMessageThread.java
 * <p>
 * This is the ReadMessageThread
 * (see more in the ReadMe)
 *
 * @author Pranav Neti, Ryan Sierra, Tanya Jain, Anay Misra - Lab Section 12
 * @version Nov 17, 2024
 */

public class ReadMessageThreadGraphical implements Runnable {
    String fileName;
    BufferedWriter output;

    //TODO consider passing a boolean to the server to then go to the client which lets the client know to check for something new, like maybe append it????
    // also, another thing we could try is making another thread class that edits the file by writing it to the sever, and then the server calls the other class to run the thread looking for the new messages and such
    // ^ do this probably
    //server starts this to start reading, then ReadMessageThread passes that message history to the server, and from there the server takes and passes the values to a new thead,
    // which off in the distance keeps checking for new read lines on the same socket, allowing you to read line multiple times, with one of the threads only looking for a certain one
    // because of buffered reader stuff, we have to write to a different output stream for the displayMessageThread
    boolean exit = false;

    public ReadMessageThreadGraphical(String fileName, BufferedWriter output) {
        this.fileName = fileName;
        this.output = output;
    }

    @Override
    public void run() {
        try {
            while (true) {
                File f = new File("files/" + fileName);
                String messageHistory = "";
                FileTime ft = Files.getLastModifiedTime(Path.of(f.getAbsolutePath()));
                boolean firstTime = true;
                if (f.exists()) {
                    BufferedReader br = new BufferedReader(new FileReader("files/" + fileName));
                    while (!exit) {
                        // System.out.println(ft.compareTo(Files.getLastModifiedTime(Path.of(f.getAbsolutePath()))));
                        if(ft.compareTo(Files.getLastModifiedTime(Path.of(f.getAbsolutePath()))) < 0 || firstTime) {
                            ft = Files.getLastModifiedTime(Path.of(f.getAbsolutePath()));
                            
                            messageHistory = "";
                            br = new BufferedReader(new FileReader(f));
                            String message;
                            while ((message = br.readLine()) != null) {
                                if(!message.isEmpty()) {
                                    String[] parts = message.split("-");
                                    System.out.println("THIS IS A MESSAGE LOOK AT ME HERE: " + message);
                                    if(!parts[2].equals(" ") && !parts[2].isEmpty()) {
                                        if(parts[2].startsWith("<p><img src='")) {
                                            String profilePicture = parts[2].substring(13, parts[2].lastIndexOf("alt") - 2);
                                            File file = new File(profilePicture);
                                            String path = file.getAbsolutePath();

                                            parts[2] = "<p><img src='file://" + path + "' alt='' width='300' height='200'></p>";
                                            // <p><img src='./files/<text>imagination.png' alt='' width='300' height='200'></p>

                                            messageHistory = messageHistory + "<p>" + parts[0] + ":</p>" + parts[2];
                                        } else {
                                            messageHistory = messageHistory + "<p>" + parts[0] + ": " + parts[2] + "</p>";
                                        }
                                    }
                                }
                            }

                            System.out.println("Broke Out");
                            output.write(messageHistory + "\n");
                            output.flush();
                            firstTime = false;
                        }

                        if(Thread.currentThread().isInterrupted()) {
                            System.out.println("WE GOT INTRUPPTED");
                            exit = true;
                        }
                    }

                    br.close();
                    System.out.println("closing socket");
                    break;
                } else {
                    BufferedWriter bw = new BufferedWriter(new FileWriter("files/" + fileName));
                    bw.write(fileName.substring(0, fileName.length() - 4) +  "-" + " " + "\n");
                    bw.flush();
                    bw.close();
//                     BufferedReader br = new BufferedReader(new FileReader("files/" + fileName));
//                     while (!exit) {
//                         if(f.lastModified() > lastModifiedTime || firstTime) {
//                             lastModifiedTime = f.lastModified();
//                             messageHistory = "";
//                             br = new BufferedReader(new FileReader(f));
//                             String message;
//                             while ((message = br.readLine()) != null) {
//                                 if(!message.isEmpty()) {
//                                     String[] parts = message.split("-");
//                                     System.out.println("THIS IS A MESSAGE LOOK AT ME HERE: " + message);
//                                     if(!parts[2].equals(" ") && !parts[2].isEmpty()) {
//                                         if(parts[2].startsWith("<p><img src='")) {
//                                             String profilePicture = parts[2].substring(13, parts[2].lastIndexOf("alt") - 2);
//                                             File file = new File(profilePicture);
//                                             String path = file.getAbsolutePath();

//                                             parts[2] = "<p><img src='" + path + "' alt='' width='300' height='200'></p>";
//                                             // <p><img src='./files/<text>imagination.png' alt='' width='300' height='200'></p>

//                                             messageHistory = messageHistory + "<p>" + parts[0] + ":</p>" + parts[2];
//                                         } else {
//                                             messageHistory = messageHistory + "<p>" + parts[0] + ": " + parts[2] + "</p>";
//                                         }
//                                     }
//                                 }

// //                                textPane.setContentType("text/html");
// //                                textPane.setText(messageHistory);
// //                                textPane.setCaretPosition(textPane.getDocument().getLength());
// //                                frame.repaint();
//                             }
//                             System.out.println("Broke Out");
//                             output.write(messageHistory + "\n");
//                             output.flush();
//                             firstTime = false;
//                         }

//                         if(Thread.currentThread().isInterrupted()) {
//                             System.out.println("WE GOT INTRUPPTED");
//                             exit = true;
//                         }
//                     }
//                     br.close();
//                     System.out.println("closing socket");
//                     break;
                }
            }

        } catch (IOException e) {
            System.out.println(" ");
        }
    }
}