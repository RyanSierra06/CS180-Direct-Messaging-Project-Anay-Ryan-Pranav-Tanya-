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
                        if(ft.compareTo(Files.getLastModifiedTime(Path.of(f.getAbsolutePath()))) < 0 || firstTime) {
                            ft = Files.getLastModifiedTime(Path.of(f.getAbsolutePath()));

                            messageHistory = "";
                            br = new BufferedReader(new FileReader(f));
                            String message;
                            while ((message = br.readLine()) != null) {
                                if(!message.isEmpty()) {
                                    String[] parts = message.split("-");
                                    if(!parts[2].equals(" ") && !parts[2].isEmpty()) {
                                        if(parts[2].startsWith("<p><img src='")) {
                                            String profilePicture = parts[2].substring(13, parts[2].lastIndexOf("alt") - 2);
                                            File file = new File(profilePicture);
                                            String path = file.getAbsolutePath();
                                            parts[2] = "<p><img src='file://" + path + "' alt='' width='300' height='200'></p>";
                                            messageHistory = messageHistory + "<p>" + parts[0] + ":</p>" + parts[2];
                                        } else {
                                            messageHistory = messageHistory + "<p>" + parts[0] + ": " + parts[2] + "</p>";
                                        }
                                    }
                                }
                            }
                            output.write(messageHistory + "\n");
                            output.flush();
                            firstTime = false;
                        }

                        if(Thread.currentThread().isInterrupted()) {
                            exit = true;
                        }
                    }

                    br.close();
                    break;
                } else {
                    BufferedWriter bw = new BufferedWriter(new FileWriter("files/" + fileName));
                    bw.write(fileName.substring(0, fileName.length() - 4) +  "-" + " " + "\n");
                    bw.flush();
                    bw.close();
                }
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}