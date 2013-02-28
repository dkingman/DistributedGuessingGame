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
        play(new Node(), bufferedReader);
    }

    private static void play(Node node, BufferedReader bufferedReader) {
        String response = null;
        String celebrity = node.getNodeData().getCelebrity();
        String question = node.getNodeData().getQuestion();
        if(celebrity != null) {
            do {
                System.out.println(new StringBuilder().append("Is the celebrity you are thinking of ").append(celebrity).append("?").toString());
                try {
                    response = bufferedReader.readLine();
                    if(answeredYes(response)) {
                        System.out.println("I knew it!");
                        break;
                    } else if(answeredNo(response)) {

                        break;
                    } else {
                        System.out.println("Didn't understand response, please try again.");
                    }
                }  catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            while(true);
        }
         else if(question != null) {
            System.out.println(question);
            do {
                try {
                    response = bufferedReader.readLine();
                    if(answeredYes(response)) {
                        play(node.getYes(),bufferedReader);
                        break;
                    } else if (answeredNo(response)) {
                        play(node.getNo(),bufferedReader);
                        break;
                    } else {
                        System.out.println("Didn't understand response, please try again.");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            while(true);
        }
    }

    private static boolean answeredYes(String answer) {
        return answer.toLowerCase().equals('y') || answer.toLowerCase().equals("yes");
    }

    private static boolean answeredNo(String answer) {
        return answer.toLowerCase().equals('n') || answer.toLowerCase().equals("no");
    }
}
