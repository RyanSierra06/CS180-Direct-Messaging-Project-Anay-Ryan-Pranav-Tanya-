import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Group PJ -- Application Server
 *
 * This is the Application Server class.
 *
 * @version Nov 14, 2024
 *
 */

public class ApplicationServer implements ApplicationServerInterface, Runnable {
   private static AtomicInteger counter = new AtomicInteger(2);
   private static final int portNumber = 4242;

   public static void main(String[] args) throws IOException {
      ApplicationServer server = new ApplicationServer();
      server.run();
   }

   public void run() {
      try {
         ServerSocket serverSocket = new ServerSocket(portNumber); // need universal port number - set to 4242 for now
         while (true) {
            Socket socket = serverSocket.accept();
            new Thread(() -> handleClient(socket)).start(); // new thread for each client
         }
      } catch (IOException e) {
         System.err.println("server error: " + e.getMessage());
         e.printStackTrace();
      }
   }


   public void handleClient(Socket clientSocket) {
      //TODO CHANGE TO WRITE TO FILE
      try (ObjectInputStream input = new ObjectInputStream(clientSocket.getInputStream());
           ObjectOutputStream output = new ObjectOutputStream(clientSocket.getOutputStream())) {
         String choice = "";
         StringBuilder messages  = new StringBuilder();
         while((choice = input.readLine()) != null) {
            if(choice.length() == 1) {
               switch(choice) {
                  case "1", "2" -> {
                     messages.append("Username: ").append(input.readLine()).append("\n");
                     messages.append("Password: ").append(input.readLine()).append("\n");
                  }
               }

            }
         }

         if(!messages.isEmpty()) {
            String[] nameAndPass = messages.toString().split("\n");
            //name is 0, pass is 1
            BufferedWriter bw = new BufferedWriter(new FileWriter("files/" + nameAndPass[0] + ".txt"));

            String messageText = (String) input.readObject();
            System.out.println("SERVER: Received message text: " + messageText);

            output.writeObject(messageText);
            output.flush();
         }

      } catch (IOException | ClassNotFoundException e) {
         System.err.println("SERVER: Error handling client: " + e.getMessage());
      } finally {
         try {
            clientSocket.close();
         } catch (IOException e) {
            System.err.println("SERVER: Could not close socket: " + e.getMessage());
         }
      }
   }


}
