import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class ReadMessageThread implements Runnable {
    BufferedReader br = null;
    boolean exit = false;

    public ReadMessageThread(BufferedReader br) {
        this.br = br;
    }

    @Override
    public void run() {
        try {
            while(!exit) {
                String isBlocked = br.readLine();
                if(isBlocked.equals("Block Error: Failed to send message.")) {
                    return;
                } else if(isBlocked.equals("This User Doesnt Accept Messages from Non-Friends")) {
                    return;
                } else if(isBlocked.equalsIgnoreCase("quit")) {
                    break;
                } else {
                    String message;
                    // BufferedWriter messagesTerminal = new BufferedWriter(new FileWriter(new File("files/messagesTerminal.txt")));
                    while((message = br.readLine()) != null) {
                        // messagesTerminal.write(message + "\n");
                        System.out.println(message);
                    }
                }
            }

        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void stopThread() {
        exit = true;
    }
}
