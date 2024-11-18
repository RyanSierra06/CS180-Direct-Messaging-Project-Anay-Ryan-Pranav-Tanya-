import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.Scanner;

/**
 * Project 4 -- ApplicationClientInterface
 *
 * This is the ApplicationClientInterface which is used for ApplicationClient
 *
 * @author Pranav Neti, Ryan Sierra, Tanya Jain, Anay Misra - Lab Section 12
 *
 * @version Nov 17, 2024
 *
 */

public interface ApplicationClientInterface {
    void actionsAfterLogin(BufferedWriter bw, BufferedReader br, Scanner sc);

}
