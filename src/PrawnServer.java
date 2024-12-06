import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


public class PrawnServer implements Runnable {
    Socket clientSocket;
    BufferedWriter clientWriter;
    BufferedReader clientReader;
    PrintWriter fileChanger;
    BufferedReader fileReader;
    String fileName;
    static Object lock = new Object();

    public PrawnServer(Socket clientSocket, String fileName) {
        try {
            this.clientSocket = clientSocket;
            this.clientReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            this.clientWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            this.fileChanger = new PrintWriter(new File(fileName));
            this.fileReader = new BufferedReader(new FileReader(new File(fileName)));
            this.fileName = fileName;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        String choice;
        while(true) {
            try {
                choice = clientReader.readLine();
                System.out.println(choice);

                if(choice.startsWith("msg")) {
                    // synchronized(lock) {
                        String trueMsg = choice.substring(6);
                        fileChanger.write(trueMsg + "\n");
                        fileChanger.flush();
                        System.out.println("wroet");
                    // }
                } else if(choice.startsWith("read msg")) {
                    StringBuilder sb = new StringBuilder();
                    fileReader = new BufferedReader(new FileReader(new File(fileName)));
                    String curLine = fileReader.readLine();
                    while(curLine != null) {
                        sb.append(curLine + "\n");
                        curLine = fileReader.readLine();
                    }

                    if(sb.toString().isEmpty()) {
                        clientWriter.write("empty\n");
                        clientWriter.flush();
                        clientWriter.write("");
                        clientWriter.flush();
                    }

                    clientWriter.write(sb.toString()+"\n");
                    clientWriter.flush();
                    clientWriter.write("");
                    clientWriter.flush();
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
    
}
