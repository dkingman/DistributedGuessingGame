import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Main {

    public static void main(String[] args) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Would you like to play a celebrity guessing game?");

        // COMMENTED OUT FOR DEVELOPMENT PURPOSES
//        try {
//            String response = bufferedReader.readLine();
//            if(response.toLowerCase().equals("yes")) {
                playGame(bufferedReader, new Socket());       // replace with actual socket
//            }
//        } catch (IOException e) {
//            System.out.println("Failed to read response of user");
//            e.printStackTrace();
//        }
    }

    private static void playGame(BufferedReader bufferedReader,Socket socket) {
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
