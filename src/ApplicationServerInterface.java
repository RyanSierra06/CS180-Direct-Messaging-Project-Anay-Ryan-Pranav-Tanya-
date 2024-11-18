import java.io.*;
import java.net.*;

/**
 * Project 4 -- ApplicationServerInterface
 * <p>
 * This is the ApplicationServerInterface which is used for the Application Server class
 *
 * @author Pranav Neti, Ryan Sierra, Tanya Jain, Anay Misra - Lab Section 12
 * @version Nov 17, 2024
 */

public interface ApplicationServerInterface {
    void handleClient(Socket clientSocket);
}

