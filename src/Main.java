import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Main {

    // Map<CelebGuessed, Set<UsersWhoGuessedCeleb>>  - Stores celebs that have been guessed and are waiting to be sent to user who created them
    public static Map<String, Set<String>> celebsGuessedbyUsers = Collections.synchronizedMap(new HashMap<String, Set<String>>());

    // Map<IPAndPortOfCreator, Set<CelebsCreatedByUser>> - Stores all of the celebs who been created since the server started.
    // Mapping each user with a set of celebs that may have created
    public static Map<String, Set<String>> celebsCreated = Collections.synchronizedMap(new HashMap<String, Set<String>>());


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
