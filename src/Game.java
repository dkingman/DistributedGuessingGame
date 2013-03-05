import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Game implements Runnable {
    private Socket s;
    private static Database db = new Database();
    private User user;

    public Game(Socket s) throws IOException {
        this.s = s;
    }
    
    public void run() {
        playGame();
    }

    public void playGame() {
        //TODO check if inetAddress has been seen before and display a message telling them if any of there answers have been guessed by others
//        User user = new User(socket.getInetAddress(), "Player");
//        NodeData nodeData = new NodeData(user);
//        Node root = new Node(nodeData);
        Database db = new Database();
        db.initDb();
  
        try {
        	BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(s.getInputStream()));
			BufferedWriter w = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
			w.write("What is your name?");
			w.newLine();
            w.flush();
            
            String response = bufferedReader.readLine();
            user = new User("192.111.111:6001", response); //TODO fill in first param with real address ie. "inetAddress.toString()"
        } catch (IOException e) {
            e.printStackTrace();
        }

        while(true) {
            while(true) {
                try {
                	BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(s.getInputStream()));
        			BufferedWriter w = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
        			w.write("Would you like to play a celebrity guessing game?");
        			w.newLine();
                    w.flush();
                	
                    String response = bufferedReader.readLine();
                    if(answeredYes(response)) {
                        Node node;
                        if(Database.recordCount == 1) node = db.readNode(1);
                        else node = db.readNode(3);
                        play(node, bufferedReader);
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

    private  void play(Node node, BufferedReader bufferedReader) {
        String response;
        String question = node.getNodeData().getQuestion();
        synchronized (db) {
            String celebrity = node.getNodeData().getCelebrity();
            if(!celebrity.trim().isEmpty()) {
                while(true) {
                    //System.out.println(new StringBuilder().append("Is the celebrity you are thinking of ").append(celebrity).append("?").toString());
                    try {
                        bufferedReader = new BufferedReader(new InputStreamReader(s.getInputStream()));
                        BufferedWriter w = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
                        w.write(new StringBuilder().append("Is the celebrity you are thinking of ").append(celebrity).append("?").toString());
                        w.newLine();
                        w.flush();

                        response = bufferedReader.readLine();
                        if(answeredYes(response)) {
                            w.write("I knew it!");
                            w.newLine();
                            w.flush();
                            break;
                        } else if(answeredNo(response)) {

                 //*********
                            w.write("Who are you thinking of?");
                            w.newLine();
                            w.flush();
                            addNewCeleb(response, node);
                            break;
                 //*********

                        } else {
                            printIncomprehensibleResponse();
                        }
                    }  catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
        if(!question.trim().isEmpty()) {
			try {
				BufferedWriter w = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
				w.write(question);
				w.newLine();
		        w.flush();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            do {
                try {
                    response = bufferedReader.readLine();
                    // Node could be stale, update before continuing
                    node = db.readNode(node.getId());
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
    
    private synchronized void addNewCeleb(String response, Node node){
    	try {
    		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(s.getInputStream()));
    		BufferedWriter w = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
	    	String celeb = bufferedReader.readLine();
			w.write(new StringBuilder().append("Ask a yes/no question that would distinguish between ").append(node.getNodeData().getCelebrity()).append(" and ").append(celeb).toString());
			w.newLine();
	        w.flush();
	        String quest = bufferedReader.readLine();
	        NodeData nodeDataCeleb = new NodeData(user,null,celeb);
	        NodeData nodeDataQuestion = new NodeData(user,quest,null);
			w.write(new StringBuilder("Would an answer of yes indicate ").append(celeb).toString());
			w.newLine();
	        w.flush();
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    private void printIncomprehensibleResponse() {
		BufferedWriter w;
		try {
			w = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
			w.write("Didn't understand response, please try again.");
			w.newLine();
	        w.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    private boolean answeredYes(String answer) {
        return answer.trim().toLowerCase().equals("y") || answer.trim().toLowerCase().equals("yes");
    }

    private boolean answeredNo(String answer) {
        return answer.trim().toLowerCase().equals("n") || answer.trim().toLowerCase().equals("no");
    }
}