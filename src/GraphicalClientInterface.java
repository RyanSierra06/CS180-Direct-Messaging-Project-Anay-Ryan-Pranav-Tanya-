import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.net.Socket;

/**
 * Project 4 -- ApplicationClientInterface
 * <p>
 * This is the ApplicationClientInterface which is used for ApplicationClient
 *
 * @author Pranav Neti, Ryan Sierra, Tanya Jain, Anay Misra - Lab Section 12
 * @version Nov 17, 2024
 */

public interface GraphicalClientInterface {
    void actionsAfterLogin(BufferedWriter bw, BufferedReader br, Socket socket, GraphicalClient client, String userNAME);
    void actionsWithinDM(BufferedWriter bw, BufferedReader br, Socket socket, String reciver, GraphicalClient client, String userNAME);

}
