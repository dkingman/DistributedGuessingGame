import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    public static void main(String[] args) throws Exception{
        try {
	        ServerSocket welcomeSocket = new ServerSocket(6001);
	
	        while(true) {
		    Socket connectionSocket = null;
			connectionSocket = welcomeSocket.accept();
	                Connection c = new Connection(connectionSocket);
	                Thread t = new Thread(c);
	                t.start();
	                askToPlay(welcomeSocket);
	        }
	        
	        } catch (IOException e) {
	            System.out.println( e + " Error");
	            e.printStackTrace();
	        }

    }
    
    private static void askToPlay(ServerSocket socket){
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Would you like to play a celebrity guessing game?");
        // COMMENTED OUT FOR DEVELOPMENT PURPOSES
        try {
            String response = bufferedReader.readLine();
            if(response.toLowerCase().equals("yes")) {
                playGame(bufferedReader, socket);       // replace with actual socket
            }
        } catch (IOException e) {
            System.out.println("Failed to read response of user");
            e.printStackTrace();
        }
    
    }

    private static void playGame(BufferedReader bufferedReader,ServerSocket socket) {
//        User user = new User(socket.getInetAddress(), "Player");
//        NodeData nodeData = new NodeData(user);
//        Node root = new Node(nodeData);
        Database db = new Database();
        db.initDb();

        User user = new User(null,"JOHN BOB");
        NodeData nodeData = new NodeData(user, "Is the celebrity Barrack Obama?", null);
        Node node = new Node(null,null,null,nodeData,Database.recordCount++);
        db.write(node);
    }

    private Node initTree() {
        try {
            RandomAccessFile file = new RandomAccessFile("c:\\data\\file.txt", "rw");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //TODO init tree with data from database
        return new Node();
    }
}
