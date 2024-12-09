import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.nio.file.FileAlreadyExistsException;

/**
 * Group PJ -- ReadMessageThread.java
 * <p>
 * This is the ReadMessageThread
 * (see more in the ReadMe)
 *
 * @author Pranav Neti, Ryan Sierra, Tanya Jain, Anay Misra - Lab Section 12
 * @version Nov 17, 2024
 */

public class GraphicalClientReader implements Runnable {
    BufferedReader br;
    JFrame frame;
    JTextPane textPane;
    BufferedWriter bw;
    Socket socket;

    boolean exit = false;

    public GraphicalClientReader(BufferedReader br, BufferedWriter bw, JTextPane textPane, JFrame frame, Socket socket) {
        this.br = br;
        this.textPane = textPane;
        this.frame = frame;
        this.bw = bw;
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            String line = br.readLine();
            while(!line.equals("quit messaging")) {

                if(line.startsWith("read file: ")) {
                    String profilePicture = line.substring(11);
                    File file = new File(profilePicture);
                    DataInputStream dis = new DataInputStream(socket.getInputStream());
                    int length = dis.readInt();
                    byte[] imageBytes = new byte[length];
                    
                    // Read the image bytes
                    dis.readFully(imageBytes);

                    // Convert the byte array back to an image
                    ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes);
                    BufferedImage receivedImage = ImageIO.read(bais);
                    try {
                        // Save the image to verify
                        if(file.getAbsolutePath().endsWith("jpg") || file.getAbsolutePath().endsWith("JPG")) {
                            ImageIO.write(receivedImage, "jpg", file);
                        } else if(file.getAbsolutePath().endsWith("png") || file.getAbsolutePath().endsWith("PNG")) {
                            ImageIO.write(receivedImage, "jpg", file);
                        }
                    } catch(FileAlreadyExistsException e) {
                        // e.printStackTrace();
                    }
                    line = br.readLine();
                }

                System.out.println(line);

                textPane.setContentType("text/html");
                textPane.setCaretColor(Color.black);
                textPane.setText(line);
                textPane.setCaretPosition(textPane.getDocument().getLength());
                textPane.repaint();
                frame.repaint();
                line = br.readLine();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}