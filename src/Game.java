import java.io.*;
import java.net.*;

public class Game implements Runnable {
    private Socket s;
    private Database db;
    private User user;

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
        try {
            String response = bufferedReader.readLine();
            user = new User("192.111.111:6001", response);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while(true) {
            System.out.println("Would you like to play a celebrity guessing game?");
            while(true) {
                try {
                    String response = bufferedReader.readLine();
                    if(answeredYes(response)) {
                        Node node;
                        if(Database.recordCount == 1) node = db.readNode(1);
                        else node = db.readNode(3);
                        play(node, bufferedReader);
                        break;
                    } else if (answeredNo(response)) {
                        return;
                    } else{
                        printIncomprehensibleResponse();
                    }
                } catch (IOException e) {
                    printIncomprehensibleResponse();
                    e.printStackTrace();
                }
            }
        }
    }

    private  void play(Node node, BufferedReader bufferedReader) {
        String response;
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
                        System.out.println("Who are you thinking of?");
                        String celeb = bufferedReader.readLine();
                        System.out.println(new StringBuilder().append("Ask a yes/no question that would distinguish between ").append(node.getNodeData().getCelebrity()).append(" and ").append(celeb).toString());
                        String quest = bufferedReader.readLine();
                        NodeData nodeDataCeleb = new NodeData(user,null,celeb);
                        NodeData nodeDataQuestion = new NodeData(user,quest,null);
                        System.out.println(new StringBuilder("Would an answer of yes indicate ").append(celeb));
                        Node newCelebNode = new Node(null,null,null,nodeDataCeleb,++Database.recordCount);
                        Node questionNode = new Node(null,null,null,nodeDataQuestion, ++Database.recordCount);
                        newCelebNode.setParent(questionNode.getId());

                        while(true){
                            response = bufferedReader.readLine();
                            if(answeredYes(response)) {
                                if(node.getParent() != null) {
                                    questionNode.setParent(node.getParent());
                                    Node parentNode = db.readNode(node.getParent());
                                    if(parentNode.getYes().compareTo(node.getId()) == 0)
                                        parentNode.setYes(questionNode.getId());
                                    else
                                        parentNode.setNo(questionNode.getId());
                                    db.update(parentNode);
                                }
                                questionNode.setYes(newCelebNode.getId());
                                questionNode.setNo(node.getId());
                                break;
                            } else if (answeredNo(response)) {
                                if(node.getParent() != null) {
                                    questionNode.setParent(node.getParent());
                                    Node parentNode = db.readNode(node.getParent());
                                    if(parentNode.getYes().compareTo(node.getId()) == 0)
                                        parentNode.setYes(questionNode.getId());
                                    else
                                        parentNode.setNo(questionNode.getId());
                                    db.update(parentNode);
                                }
                                questionNode.setYes(node.getId());
                                questionNode.setNo(newCelebNode.getId());
                                break;
                            } else {
                                printIncomprehensibleResponse();
                            }
                        }
                        node.setParent(questionNode.getId());
                        db.update(node);
                        db.write(newCelebNode);
                        db.write(questionNode);
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
                        play(db.readNode(node.getYes()),bufferedReader);
                        break;
                    } else if (answeredNo(response)) {
                        if(node.getNo() != null && node.getNo().compareTo(0) != 0) {
                            play(db.readNode(node.getNo()),bufferedReader);
                            break;
                        }
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