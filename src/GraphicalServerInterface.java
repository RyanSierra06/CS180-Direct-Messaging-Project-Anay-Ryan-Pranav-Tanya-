import java.io.*;
import java.net.*;

/**
 * Group Project -- ApplicationServerInterface
 * <p>
 * This is the ApplicationServerInterface which is used for the Application Server class
 *
 * @author Pranav Neti, Ryan Sierra, Tanya Jain, Anay Misra - Lab Section 12
 * @version Dec 8, 2024
 */

public interface GraphicalServerInterface {
    void handleClient(Socket clientSocket);
}

