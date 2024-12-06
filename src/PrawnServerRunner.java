import java.net.ServerSocket;
import java.net.Socket;

public class PrawnServerRunner {
    public static void main(String[] args) {
        try {
            ServerSocket ss = new ServerSocket(8448);
            while(true) {
                Socket s = ss.accept();

                Thread ps = new Thread(new PrawnServer(s, "file.txt"));
                ps.start();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        
    }
}
