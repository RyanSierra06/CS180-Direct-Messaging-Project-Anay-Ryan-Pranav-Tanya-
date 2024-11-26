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

    boolean exit = false;

    public ReadMessageThread(String user1, String user2, Socket socket) {
        first = (user1.compareTo(user2) > 0 ? user2 : user1);
        second = (user1.equals(first) ? user2 : user1);
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            while (true) {
                File f = new File("files/" + first + "-" + second + ".txt");

                if (f.exists()) {
                    BufferedReader br = new BufferedReader(new FileReader("files/" + first + "-" + second + ".txt"));
                    BufferedWriter output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    while (!exit) {
                        String message;
                        while ((message = br.readLine()) != null) {
                            output.write("In The Thread: " + message + "\n");
                            output.flush();
                            System.out.println(message);
                        }
                    }
                    br.close();
                    output.close();
                    System.out.println("closing socket");
                    break;
                } else {
                    BufferedWriter bw = new BufferedWriter(new FileWriter("files/" + first + "-" + second + ".txt"));
                    bw.write(first + "-" + second +  " " + "\n");
                    bw.flush();
                    bw.close();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopThread() {
        exit = true;
    }
}
