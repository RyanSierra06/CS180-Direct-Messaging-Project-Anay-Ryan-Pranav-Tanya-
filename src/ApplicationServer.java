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
      ServerSocket serverSocket = new ServerSocket(4242); // need universal port number - set to 4242 for now
      
      while (true) {
         Socket socket = serverSocket.accept();
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

         System.out.printf("recieved from client: username: %s, action: %s\n", username, actionCode);
         String response = handleAction(actionCode, username);

         writer.println(response);
         System.out.printf("sent to client: %s\n", response);

      } catch (IOException e) {
         System.err.println("error handling client: " + e.getMessage());
         e.printStackTrace();
      } finally {
         try {
            socket.close();
         } catch (IOException e) {
            System.err.println("error closing socket: " + e.getMessage());
         }
      }
   }

   private static String handleAction(String actionCode, String username) {
      if ("VIEW_PROFILE".equals(actionCode)) {
         return "profile for " + username;
      } else if ("UPDATE_PROFILE".equals(actionCode)) {
         return "profile updated for " + username;
      } else {
         return "unknown action";
      }
   }
}
