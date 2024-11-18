import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Scanner;

public class WriteMessageThread implements Runnable {
    BufferedWriter bw = null;
    Scanner sc;
    String receiver;
    String type;
    int displayMessageHistoryCounter;
    

    public WriteMessageThread(BufferedWriter bw, String message, String receiver, String type, int displayMessageHistoryCounter) {
        this.bw = bw;
        this.sc = sc;
        this.receiver = receiver;
        this.type = type;
        this.displayMessageHistoryCounter = displayMessageHistoryCounter;
    }

    @Override
    public void run() {
        try {
            while(true) {
                String message = sc.nextLine();
                if(message.equalsIgnoreCase("quit")) {
                    break;
                }
                bw.write("Message: " + receiver + " " + message + " " + type + " " + displayMessageHistoryCounter + "\n");
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
