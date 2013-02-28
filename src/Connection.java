import java.io.*;
import java.net.*;

public class Connection implements Runnable {

    private DataInputStream inFromClient;
    private DataOutputStream  outToClient;
    private Socket s;

    public Connection(Socket s) throws IOException {
        this.s = s;
        inFromClient =
            new DataInputStream(s.getInputStream());
        outToClient =
            new DataOutputStream(s.getOutputStream());
    }
    
    public void run() {
        try {
            String clientSentence = inFromClient.readUTF();

            while (clientSentence.length() > 0) {
                String capitalizedSentence =
                    clientSentence.toUpperCase() + '\n';
                outToClient.writeUTF(capitalizedSentence);
                clientSentence = inFromClient.readUTF();
            }
        } catch (IOException e) {
            System.out.println(e);
        } finally {
            try {
                s.close();
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }
}