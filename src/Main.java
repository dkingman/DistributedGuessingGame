import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    public static void main(String[] args) throws Exception{
        try {
	        ServerSocket welcomeSocket = new ServerSocket(6001);
	
	        while(true) {
		        Socket connectionSocket = null;
			    connectionSocket = welcomeSocket.accept();
	            Game game = new Game(connectionSocket);
	            Thread t = new Thread(game);
	            t.start();
	        }
	        
	        } catch (IOException e) {
	            System.out.println( e + " Error");
	            e.printStackTrace();
	        }

    }
    
}
