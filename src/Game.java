import java.io.*;
import java.net.*;

public class Game implements Runnable {

    private DataInputStream inFromClient;
    private DataOutputStream  outToClient;
    private Socket s;
    private Database db;

    public Game(Socket s) throws IOException {
        this.s = s;
        db = new Database();
    }
    
    public void run() {
        playGame();
    }

    public void playGame() {
//        User user = new User(socket.getInetAddress(), "Player");
//        NodeData nodeData = new NodeData(user);
//        Node root = new Node(nodeData);
        Database db = new Database();
        db.initDb();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("What is your name?");
        User user = null;
        try {
            String response = bufferedReader.readLine();
            user = new User("192.111.111:6001", response);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Would you like to play a celebrity guessing game?");
        while(true) {
            try {
                String response = bufferedReader.readLine();
                if(answeredYes(response)) {
                    Node node = db.readNode(1);
                    play(node, bufferedReader);
                    break;
                } else {
                    printIncomprehensibleResponse();
                }
            } catch (IOException e) {
                printIncomprehensibleResponse();
                e.printStackTrace();
            }
        }
    }

    private  void play(Node node, BufferedReader bufferedReader) {
        String response = null;
        String celebrity = node.getNodeData().getCelebrity();
        String question = node.getNodeData().getQuestion();
        if(!celebrity.trim().isEmpty()) {
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
                        printIncomprehensibleResponse();
                    }
                }  catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            while(true);
        }
        else if(!question.trim().isEmpty()) {
            System.out.println(question);
            do {
                try {
                    response = bufferedReader.readLine();
                    if(answeredYes(response)) {
                          //get left node first from db
//                        play(node.getYes(),bufferedReader);
                        break;
                    } else if (answeredNo(response)) {
                        // get right node first from db
//                        play(node.getNo(),bufferedReader);
                        break;
                    } else {
                        printIncomprehensibleResponse();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            while(true);
        }
    }

    private void printIncomprehensibleResponse() {
        System.out.println("Didn't understand response, please try again.");
    }

    private boolean answeredYes(String answer) {
        return answer.trim().toLowerCase().equals("y") || answer.trim().toLowerCase().equals("yes");
    }

    private boolean answeredNo(String answer) {
        return answer.trim().toLowerCase().equals("n") || answer.trim().toLowerCase().equals("no");
    }
}