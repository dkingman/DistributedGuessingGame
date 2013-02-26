import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Main {

    public static void main(String[] args) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Would you like to play a celebrity guessing game?");
        try {
            String response = bufferedReader.readLine();
            if(response.toLowerCase().equals("yes")) {
                playGame(bufferedReader, new Socket());       // replace with actual socket
            }
        } catch (IOException e) {
            System.out.println("Failed to read response of user");
            e.printStackTrace();
        }
    }

    private static void playGame(BufferedReader bufferedReader,Socket socket) {
        User user = new User(socket.getInetAddress(), "Player");
        NodeData nodeData = new NodeData(user);
        Node root = new Node(nodeData);

        try {
            RandomAccessFile file = new RandomAccessFile("c:\\data\\file.txt", "rw");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Node<NodeData> initTree() {
        return new Node<NodeData>();

        //TODO init tree with data from database
    }
}
