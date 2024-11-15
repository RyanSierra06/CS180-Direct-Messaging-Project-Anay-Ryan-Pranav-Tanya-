import java.io.*;
import java.net.*;

/**
 * Project 4 -- ApplicationInterface
 *
 * This is the ApplicationInterface interface
 *
 * @author Pranav Neti, Ryan Sierra, Tanya Jain, Anay Misra - Lab Section 12
 *
 * @version Nov 14, 2024
 *
 */

public interface ApplicationServerInterface {
    static void handleClient(Socket socket){};
    static String handleAction(String actionCode, String username){ return "";};
}

