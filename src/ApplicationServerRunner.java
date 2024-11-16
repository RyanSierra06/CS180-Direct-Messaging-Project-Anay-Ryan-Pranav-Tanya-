import java.net.ServerSocket;
import java.net.Socket;

public class ApplicationServerRunner {
    public static void main(String[] args) {
        try {
            ServerSocket ss = new ServerSocket(4242);

            while(true) {
                Socket client = ss.accept();

                ApplicationServer appServer = new ApplicationServer(client);
                Thread clinetThread = new Thread(appServer);
                clinetThread.start();
                System.out.println("reached here");
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        
    }
}
