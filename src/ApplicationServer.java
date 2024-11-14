import java.io.*;
import java.net.*;
import java.util.HashMap;

/**
 * Group PJ -- Application Server
 *
 * This is the Application Server class
 *
 * @author Pranav Neti, Ryan Sierra, Tanya Jain, Anay Misra - Lab Section 12
 *
 * @version Nov 14, 2024
 *
 */

public class ApplicationServer implements ApplicationServerInterface {
   private static HashMap<String, User> userProfiles = new HashMap<>();
   private static final Object gateKeep = new Object();

   public static void main(String[] args) throws IOException {
      ServerSocket serverSocket = new ServerSocket(4242); //need a universal port number - using 4242 for now
      
      while (true) {
         Socket socket = serverSocket.accept();
         System.out.println("Client connected.");

         new ClientHandler(socket).start();
      }
   }

   private static class ClientHandler extends Thread {
      private Socket socket;

      public ClientHandler(Socket socket) {
         this.socket = socket;
      }

      @Override
      public void run() {
         try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            String username = reader.readLine();
            String actionCode = reader.readLine();

            System.out.printf("received from client: username: %s, action: %s\n", username, actionCode);
            
            String response;
            synchronized (gateKeep) {
               User user = userProfiles.get(username);
               if (user == null) {
                  user = new User(username, "defaultPass");
                  userProfiles.put(username, user);
               }
               response = handleAction(actionCode, user, reader);
            }

            writer.println(response);
            System.out.printf("sent to client: %s\n", response);

            reader.close();
            writer.close();
            socket.close();
         } catch (IOException e) {
            e.printStackTrace();
         }
      }
   }
}
