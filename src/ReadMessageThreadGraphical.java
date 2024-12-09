import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.nio.file.FileAlreadyExistsException;
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
    Socket socket;
    String userName;

    boolean exit = false;

    public ReadMessageThreadGraphical(String fileName, BufferedWriter output, Socket socket, String userName) {
        this.fileName = fileName;
        this.output = output;
        this.socket = socket;
        this.userName = userName;
    }

    @Override
    public void run() {
        try {
            while (true) {
                File f = new File("files/" + fileName);
                String messageHistory = "";
                long lastModifiedTime = f.lastModified();
                boolean firstTime = true;
                if (f.exists()) {
                    BufferedReader br = new BufferedReader(new FileReader("files/" + fileName));
                    while (!exit) {
                        if (f.lastModified() > lastModifiedTime || firstTime) {
                            lastModifiedTime = f.lastModified();

                            messageHistory = "";
                            br = new BufferedReader(new FileReader(f));
                            String message;
                            while ((message = br.readLine()) != null) {
                                if (message.contains("read file: ")) {
                                    continue;
                                }
                                if (!message.isEmpty()) {
                                    String[] parts = message.split("-");
                                    if (!parts[2].equals(" ") && !parts[2].isEmpty()) {
                                        if (parts[2].startsWith("<p><img src='") && !parts[0].equals(userName)) {
                                            String profilePicture = parts[2].substring(13,
                                                    parts[2].lastIndexOf("alt") - 2);
                                            File file = new File(profilePicture);

                                            if (file.exists()) {
                                                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                                                BufferedImage image = ImageIO.read(file);
                                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                                byte[] imageBytes = baos.toByteArray();
                                                dos.writeInt(imageBytes.length);
                                                dos.flush();
                                                if (file.getAbsolutePath().endsWith("jpg") ||
                                                        file.getAbsolutePath().endsWith("JPG")) {
                                                    ImageIO.write(image, "jpg", baos);
                                                } else if (file.getAbsolutePath().endsWith("png") ||
                                                        file.getAbsolutePath().endsWith("PNG")) {
                                                    ImageIO.write(image, "png", baos);
                                                }
                                                baos.flush();
                                                dos.write(imageBytes);
                                                dos.flush();

                                                profilePicture = parts[2].substring(13,
                                                        parts[2].lastIndexOf("alt") - 2);
                                                file = new File(profilePicture);
                                                String path = file.getAbsolutePath();
                                                parts[2] = "<p><img src='file://" + path +
                                                        "' alt='' width='300' height='200'></p>";
                                                messageHistory = messageHistory + "<p>" +
                                                        parts[0] + ":</p>" + parts[2];
                                                output.write("read file: " + profilePicture + "\n");
                                                output.flush();
                                            }

                                        } else if (parts[2].startsWith("<p><img src='")
                                                && parts[0].equals(userName)) {
                                            String profilePicture = parts[2].substring(13,
                                                    parts[2].lastIndexOf("alt") - 2);
                                            File file = new File(profilePicture);

                                            if (!file.exists()) {
                                                DataInputStream dis = new DataInputStream(socket.getInputStream());
                                                // Read the length of the image byte array
                                                int length = dis.readInt();
                                                byte[] imageBytes = new byte[length];

                                                // Read the image bytes
                                                dis.readFully(imageBytes);

                                                // Convert the byte array back to an image
                                                ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes);
                                                BufferedImage receivedImage = ImageIO.read(bais);

                                                try {
                                                    // Save the image to verify
                                                    if (file.getAbsolutePath().endsWith("jpg") ||
                                                            file.getAbsolutePath().endsWith("JPG")) {
                                                        ImageIO.write(receivedImage, "jpg", file);
                                                    } else if (file.getAbsolutePath().endsWith("png") ||
                                                            file.getAbsolutePath().endsWith("PNG")) {
                                                        ImageIO.write(receivedImage, "jpg", file);
                                                    }
                                                } catch (FileAlreadyExistsException e) {
                                                    e.printStackTrace();
                                                }

                                            }


                                            profilePicture = parts[2].substring(13, parts[2].lastIndexOf("alt") - 2);
                                            file = new File(profilePicture);
                                            String path = file.getAbsolutePath();
                                            parts[2] = "<p><img src='file://" + path +
                                                    "' alt='' width='300' height='200'></p>";
                                            messageHistory = messageHistory + "<p>" + parts[0] + ":</p>" + parts[2];
                                        } else if (!parts[2].startsWith("<p><img src='")) {
                                            messageHistory = messageHistory + "<p>" + parts[0] + ": "
                                                    + parts[2] + "</p>";
                                        }
                                    }
                                }
                            }
                            output.write(messageHistory + "\n");
                            output.flush();
                            firstTime = false;
                        }

                        if (Thread.currentThread().isInterrupted()) {
                            exit = true;
                        }
                    }

                    br.close();
                    break;
                } else {
                    BufferedWriter bw = new BufferedWriter(new FileWriter(new File("files/" + fileName)));
                    bw.write(fileName.substring(0, fileName.length() - 4) + "-" + " " + "\n");
                    bw.flush();
                    bw.close();
                }
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}