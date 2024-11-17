import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class ReadMessageThread implements Runnable {
    BufferedReader br = null;
    

    public ReadMessageThread(BufferedReader br) {
        this.br = br;
    }

    @Override
    public void run() {
        try {
            String isBlocked = br.readLine();
            if(isBlocked.equals("Block Error: Failed to send message.")) {
                return;
            } else {
                String message;
                while( (message = br.readLine()) != null) {
                    System.out.println(message);
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
