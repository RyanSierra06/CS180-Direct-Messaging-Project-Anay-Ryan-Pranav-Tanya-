import java.io.*;
import java.net.*;

/**
 * Group PJ -- Application Server
 *
 * This is the Application Server class
 *
 * @author Pranav Neti, Ryan Sierra, Tanya Jain, Anay Misra - Lab Section 12
 *
 * @version Nov 8, 2024
 *
 */

public class ApplicationServer implements ApplicationServerInterface {
   // need to make ApplicationServerInterface
   // information stored on server side
   // information accessed via network IO using the client

   public static void main(String[] args) throws IOException {
      ServerSocket serverSocket = new ServerSocket(4242);
      System.out.println("waiting for client to connect");
      Socket socket = serverSocket.accept();
      System.out.println("client connected");

      BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      PrintWriter writer = new PrintWriter(socket.getOutputStream());

      String message = reader.readLine();
      System.out.printf("received from client:\n%s\n", message);

      String response = message.replaceAll(" ", ",");
      writer.write(response);
      writer.println();
      writer.flush();
      System.out.printf("sent to client:\n%s\n", response);

      writer.close();
      reader.close();
      socket.close();
      serverSocket.close();
   }   
}
