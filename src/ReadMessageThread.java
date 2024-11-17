import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class ReadMessageThread implements Runnable {
    BufferedReader br = null;
    BufferedWriter bw = null;

    public ReadMessageThread(BufferedReader br) {
        this.bw = bw;
        this.br = br;
    }

    @Override
    public void run() {
        try {
            while(true) {
                String isBlocked = br.readLine();
                if(isBlocked.equals("Block Error: Failed to send message.")) {
                    return;
                } else if(isBlocked.equals("This User Doesnt Accept Messages from Non-Friends")) {
                    return;
                } else if(isBlocked.equalsIgnoreCase("quit")) {
                    break;
                } else {
                    String message;
                    while((message = br.readLine()) != null) {
                        System.out.println(message);
                    }
                }
            }

        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
