import java.net.ServerSocket;
import java.net.Socket;

/**
 * Project 4 -- ApplicationServerRunner
 * <p>
 * This is the ApplicationServerRunner which is used to run the Application Server class
 *
 * @author Pranav Neti, Ryan Sierra, Tanya Jain, Anay Misra - Lab Section 12
 * @version Nov 17, 2024
 */

public class GraphicalServerRunner {
    public static void main(String[] args) {
        try {
            ServerSocket ss = new ServerSocket(4243);

            while (true) {
                Socket client = ss.accept();

                GraphicalServer appServer = new GraphicalServer(client);
                Thread clinetThread = new Thread(appServer);
                clinetThread.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
