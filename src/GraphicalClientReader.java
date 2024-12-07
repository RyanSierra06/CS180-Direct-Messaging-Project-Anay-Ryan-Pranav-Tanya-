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
    BufferedWriter bw;

    boolean exit = false;

    public GraphicalClientReader(BufferedReader br, BufferedWriter bw, JTextPane textPane, JFrame frame) {
        this.br = br;
        this.textPane = textPane;
        this.frame = frame;
        this.bw = bw;
    }

    @Override
    public void run() {
        try {
            String line = br.readLine();
            System.out.println(line);
            while(!line.equals("quit messaging")) {
                textPane.setContentType("text/html");
                textPane.setCaretColor(Color.black);
                textPane.setText(line);
                textPane.setCaretPosition(textPane.getDocument().getLength());
                textPane.repaint();
                frame.repaint();
                line = br.readLine();
                System.out.println("HERE " + line);
                bw.write(line + "\n");
                bw.flush();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}