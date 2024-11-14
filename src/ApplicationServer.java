import java.io.*;
import java.net.*;

/**
 * Group PJ -- Application Server
 *
 * This is the Application Server class.
 *
 * @version Nov 14, 2024
 *
 */

public class ApplicationServer implements ApplicationServerInterface {
   
   public static void main(String[] args) throws IOException {
      ServerSocket serverSocket = new ServerSocket(4242); // Universal port number set to 4242
      
      System.out.println("Server started and waiting for clients...");
      
      while (true) {
         Socket socket = serverSocket.accept();
         System.out.println("Client connected.");

         new Thread(() -> handleClient(socket)).start();
      }
   }

   private static void handleClient(Socket socket) {
      try (
         BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
         PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)
      ) {
         String username = reader.readLine();
         String actionCode = reader.readLine();

         System.out.printf("Received from client: username: %s, action: %s\n", username, actionCode);


         String response = handleAction(actionCode, username);

         writer.println(response); // Send response back to client
         System.out.printf("Sent to client: %s\n", response);
         
      } catch (IOException e) {
         System.err.println("Error handling client: " + e.getMessage());
         e.printStackTrace();
      } finally {
         try {
            socket.close();
         } catch (IOException e) {
            System.err.println("Error closing socket: " + e.getMessage());
         }
      }
   }

   
   private static String handleAction(String actionCode, String username) {
      // Example response generation based on actionCode
      if ("VIEW_PROFILE".equals(actionCode)) {
         return "Profile for " + username;
      } else if ("UPDATE_PROFILE".equals(actionCode)) {
         return "Profile updated for " + username;
      } else {
         return "Unknown action";
      }
   }
}