import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class PrawnClient {
    public static void main(String[] args) {
        try {
            Socket sc = new Socket("localhost", 8448);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(sc.getOutputStream()));
            BufferedReader br = new BufferedReader(new InputStreamReader(sc.getInputStream()));
            // FileOutputStream os = new FileOutputStream(sc.getOutputStream());
    
            Scanner scan = new Scanner(System.in);
            ScrollableTextFrame textFrame = new ScrollableTextFrame();
    
            while(true) {
                String s = scan.nextLine();
                // System.out.println(s);
    
                if(s.startsWith("msg")) {
                    bw.write(s + "\n");
                    bw.flush();
                } // } else if(s.startsWith("read msg")) {
                    bw.write("read msg" + "\n");
                    bw.flush();
                    // textFrame.reset();
                    // StringBuilder msg = new StringBuilder();
                    String mess = br.readLine();
                    while(!mess.isEmpty()) {
                        // msg.append(mess + "\n");
                        textFrame.addText(mess);
                        mess = br.readLine();
                    }
    
                    // System.out.print(msg.toString());
                if(s.startsWith("img")) {
                    bw.write("img\n");
                    bw.flush();

                    File f = new File(s.substring(3));


                    textFrame.addImage(s.substring(3));
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
