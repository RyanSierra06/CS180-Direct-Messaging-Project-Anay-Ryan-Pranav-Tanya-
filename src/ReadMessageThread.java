import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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

    boolean exit = false;

    public ReadMessageThread(String user1, String user2) {
        first = (user1.compareTo(user2) > 0 ? user2 : user1);
        second = (user1.equals(first) ? user2 : user1);
    }

    @Override
    public void run() {
        try {

            while (true) {
                File f = new File("files/" + first + "-" + second + ".txt");

                if (f.exists()) {
                    BufferedReader br = new BufferedReader(new FileReader("files/" + first + "-" + second + ".txt"));
                    while (!exit) {
                        String message;
                        while ((message = br.readLine()) != null) {
                            System.out.println(message);
                        }
                    }
                    br.close();
                    break;
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
