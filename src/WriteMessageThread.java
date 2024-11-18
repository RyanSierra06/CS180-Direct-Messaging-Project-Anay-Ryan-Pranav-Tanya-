import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * Project 4 -- WriteMessageThread
 * <p>
 * This is the WriteMessageThread which is used to send messages back to both clients as 2 people are
 * sending each other DMs
 * (See the ReadMe for more details)
 *
 * @author Pranav Neti, Ryan Sierra, Tanya Jain, Anay Misra - Lab Section 12
 * @version Nov 17, 2024
 */

public class WriteMessageThread implements Runnable {
    BufferedWriter bw = null;
    Scanner sc;
    String receiver;
    String type;
    int displayMessageHistoryCounter;


    public WriteMessageThread(BufferedWriter bw, String message, String receiver, String type,
                              int displayMessageHistoryCounter) {
        this.bw = bw;
        this.sc = sc;
        this.receiver = receiver;
        this.type = type;
        this.displayMessageHistoryCounter = displayMessageHistoryCounter;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String message = sc.nextLine();
                if (message.equalsIgnoreCase("quit")) {
                    break;
                }
                bw.write("Message: " + receiver + " " + message + " " + type + " " + displayMessageHistoryCounter + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
