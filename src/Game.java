import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

public class Game implements Runnable {
    private Socket s;
    private static final Database db = new Database();
    private BufferedReader bufferedReader;
    private BufferedWriter w;
    private User user;
    private static final Integer lock = new Integer(1);
    private Lock loc;

    public Game(Socket s) throws IOException {
        this.s = s;
    }
    
    public void run() {
        playGame();
    }

    public void playGame() {
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(s.getInputStream()));
            w = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        //TODO check if inetAddress has been seen before and display a message telling them if any of there answers have been guessed by others
//        User user = new User(socket.getInetAddress(), "Player");
//        NodeData nodeData = new NodeData(user);
//        Node root = new Node(nodeData);
        db.initDb();
  
        try {
            writeAndFlush(w,"What is your name?");
            String response = bufferedReader.readLine();
            //user = new User("192.111.111:6001", response); //TODO fill in first param with real address ie. "inetAddress.toString()"
            user = new User(Thread.currentThread().getId(), response); //TODO fill in first param with real address ie. "inetAddress.toString()"
        } catch (IOException e) {
            e.printStackTrace();
        }

        while(true) {
            while(true) {
                try {

                    writeAndFlush(w, "Would you like to play a celebrity guessing game?");
                	
                    String response = bufferedReader.readLine();
                    if(answeredYes(response)) {
                        Node node;
                        if(Database.recordCount == 1) node = db.readNode(1);
                        else node = db.readNode(3);
                        play(node);
                        break;
                    } else if (answeredNo(response)) {
                    	s.close();
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

    private void play(Node node) {
        String response;
        String celebrity = node.getNodeData().getCelebrity();
        Integer oldParent = node.getParent();
        if(!celebrity.trim().isEmpty()) {
            while(true) {
                synchronized (lock) {
                    node = db.readNode(node.getId());
                    // Check if the parent has changed while we were waiting for the lock
                    if((oldParent != null && oldParent.equals(node.getParent())) || (oldParent == null && node.getParent() == null)){
                        try {
                            writeAndFlush(w, new StringBuilder().append("Is the celebrity you are thinking of ").append(celebrity).append("?").toString());
                            response = bufferedReader.readLine();
                            if(answeredYes(response)) {
                                w.write("I knew it!");
                                w.newLine();
                                w.flush();
                                break;
                            } else if(answeredNo(response)) {
                                writeAndFlush(w, "Who are you thinking of?");
                                addNewCeleb(response, node);
                                break;

                            } else {
                                printIncomprehensibleResponse();
                            }
                        }  catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    // Parent changed, another node was added so we reset ourselves to the parent
                    }else {
                        node = db.readNode(node.getParent());
                        break;
                    }
                }
            }
        }

        String question = node.getNodeData().getQuestion();
        if(!question.trim().isEmpty()) {
			try {
				BufferedWriter w = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
                writeAndFlush(w, question);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
            do {
                try {
                    response = bufferedReader.readLine();
                    // Node could be stale, update before continuing
                    node = db.readNode(node.getId());
                    if(answeredYes(response)) {
                        play(db.readNode(node.getYes()));
                        break;
                    } else if (answeredNo(response)) {
                        if(node.getNo() != null && node.getNo().compareTo(0) != 0) {
                            play(db.readNode(node.getNo()));
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
    
    private void addNewCeleb(String response, Node node){
    	try {
	    	String celeb = bufferedReader.readLine();
            writeAndFlush(w, new StringBuilder().append("Ask a yes/no question that would distinguish between ").append(node.getNodeData().getCelebrity()).append(" and ").append(celeb).toString());
	        String quest = bufferedReader.readLine();
	        NodeData nodeDataCeleb = new NodeData(user,null,celeb);
	        NodeData nodeDataQuestion = new NodeData(user,quest,null);
            writeAndFlush(w, new StringBuilder("Would an answer of yes indicate ").append(celeb).toString());
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
        
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    private void printIncomprehensibleResponse() {
		BufferedWriter w;
		try {
			w = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
            writeAndFlush(w, "Didn't understand response, please try again.");
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    private boolean answeredYes(String answer) {
        return answer.trim().toLowerCase().equals("y") || answer.trim().toLowerCase().equals("yes");
    }

    private boolean answeredNo(String answer) {
        return answer.trim().toLowerCase().equals("n") || answer.trim().toLowerCase().equals("no");
    }

    private void writeAndFlush(BufferedWriter bufferedWriter, String toWrite) {
        try {
            bufferedWriter.write(toWrite);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}