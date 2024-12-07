import javax.swing.*;

import java.awt.Color;
import java.io.*;
import java.net.Socket;

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

    //TODO consider passing a boolean to the server to then go to the client which lets the client know to check for something new, like maybe append it????
    // also, another thing we could try is making another thread class that edits the file by writing it to the sever, and then the server calls the other class to run the thread looking for the new messages and such
    // ^ do this probably
    //server starts this to start reading, then ReadMessageThread passes that message history to the server, and from there the server takes and passes the values to a new thead,
    // which off in the distance keeps checking for new read lines on the same socket, allowing you to read line multiple times, with one of the threads only looking for a certain one
    // because of buffered reader stuff, we have to write to a different output stream for the displayMessageThread
    boolean exit = false;

    public GraphicalClientReader(BufferedReader br, JTextPane textPane, JFrame frame) {
        this.br = br;
        this.textPane = textPane;
        this.frame = frame;
    }

    @Override
    public void run() {
        try {
            // StringBuilder messages = new StringBuilder();
            // while(!line.equals("quit messaging")) {
            //     if(line.equals("first")) {
            //         messages = new StringBuilder();
            //     } else if(line.equals("last")) {
            //         textPane.setContentType("text/html");
            //         textPane.setText(messages.toString());
            //         textPane.setCaretPosition(textPane.getDocument().getLength());
            //         frame.repaint();
            //     } else if(line.startsWith("<p>")) {
            //         messages.append(line);
            //     }
                
            //     line = br.readLine();
            // }

            String line = br.readLine();
            System.out.println(line);
            while(!line.equals("quit messaging")) {
                textPane.setContentType("text/html");
                textPane.setCaretColor(Color.black);
                textPane.setText(line);
                System.out.println("Set text");
                textPane.setCaretPosition(textPane.getDocument().getLength());
                textPane.repaint();
                frame.repaint();
                line = br.readLine();
            }
        } catch (IOException e) {
            System.out.println(" ");
        }
    }
}