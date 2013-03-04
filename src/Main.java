import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    public static void main(String[] args) throws Exception{
        try {
	        ServerSocket welcomeSocket = new ServerSocket(6001);
	
//	        while(true) {
		        Socket connectionSocket = null;
			    connectionSocket = welcomeSocket.accept();
	            Game game = new Game(connectionSocket);
	            Thread t = new Thread(game);
	            t.start();
                //t.run();
	            //askToPlay(welcomeSocket);
//	        }
	        
	        } catch (IOException e) {
	            System.out.println( e + " Error");
	            e.printStackTrace();
	        }

    }
    
//    private static void askToPlay(ServerSocket socket){
//        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
//        System.out.println("Would you like to play a celebrity guessing game?");
//        // COMMENTED OUT FOR DEVELOPMENT PURPOSES
//        try {
//            String response = bufferedReader.readLine();
//            if(response.toLowerCase().equals("yes")) {
//                playGame(bufferedReader, socket);       // replace with actual socket
//            }
//        } catch (IOException e) {
//            System.out.println("Failed to read response of user");
//            e.printStackTrace();
//        }
//
//    }


}
