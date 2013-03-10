import static java.lang.System.getProperty;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * User: David
 * Date: 2/26/13
 * Time: 11:12 AM
 */
public class Database {
    private RandomAccessFile db;
    private final String dbDir;
    public static int recordCount = 1;

    private static final int PARENT_OFFSET = Node.NODE_ID_SIZE;
    private static final int YES_OFFSET = PARENT_OFFSET + Node.NODE_ID_SIZE;
    private static final int NO_OFFSET = YES_OFFSET + Node.NODE_ID_SIZE ;
    private static final int USERNAME_OFFSET = NO_OFFSET + Node.NODE_ID_SIZE;
    private static final int INETADDRESS_OFFSET = USERNAME_OFFSET + User.USERNAME_SIZE;
    private static final int QUESTION_OFFSET = INETADDRESS_OFFSET + User.INET_ADDRESS_SIZE;
    private static final int CELEBRITY_OFFSET = QUESTION_OFFSET + NodeData.QUESTION_SIZE;

    public Database() {
        String currDir = getProperty("user.dir");
        dbDir = currDir + "\\database\\database.txt";
        System.out.println(dbDir);
    }
    public void initDb() {
        try {
            db = new RandomAccessFile(dbDir, "rws");
            try {
                if(db.length() == 0) {
                    db.close();
                    User user = new User(new Long(-1),"Initial User");
                    NodeData nodeData = new NodeData(user,null,"Barrack Obama");
                    Node node = new Node(null,null,null,nodeData,Database.recordCount);
                    write(node);
                } else {
                    recordCount = new Long(db.length() / Node.RECORD_SIZE).intValue();
                }

                // Update the record count to the 1 + the last saved ID
            } catch (IOException e) {
                e.printStackTrace();

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public synchronized void write(Node node) {
        try {
            db = new RandomAccessFile(dbDir, "rws");
            db.seek(db.length());
            db.write(node.getBytes());
            db.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void update(Node node) {
        try {
            db = new RandomAccessFile(dbDir, "rws");
            db.seek((node.getId() - 1) * Node.RECORD_SIZE);
            db.write(node.getBytes());
            db.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Node readNode(long nodeNum) throws NumberFormatException {
        try {
            db = new RandomAccessFile(dbDir, "rws");
            byte[] idBytes = new byte[Node.NODE_ID_SIZE];
            byte[] parentIdBytes = new byte[Node.NODE_ID_SIZE];
            byte[] yesIdBytes = new byte[Node.NODE_ID_SIZE];
            byte[] noIdBytes = new byte[Node.NODE_ID_SIZE];
            byte[] usernameBytes = new byte[User.USERNAME_SIZE];
            byte[] threadIdBytes = new byte[User.INET_ADDRESS_SIZE];
            byte[] questionBytes = new byte[NodeData.QUESTION_SIZE];
            byte[] celebrityBytes = new byte[NodeData.CELEBRITY_NAME_SIZE];

            long recordOffset = (nodeNum-1)*Node.RECORD_SIZE;

            db.seek(recordOffset);
            db.read(idBytes,0,Node.NODE_ID_SIZE);
            db.seek(recordOffset + PARENT_OFFSET);
            db.read(parentIdBytes,0,Node.NODE_ID_SIZE);
            db.seek(recordOffset + YES_OFFSET);
            db.read(yesIdBytes,0,Node.NODE_ID_SIZE);
            db.seek(recordOffset + NO_OFFSET);
            db.read(noIdBytes,0,Node.NODE_ID_SIZE);
            db.seek(recordOffset + USERNAME_OFFSET);
            db.read(usernameBytes,0,User.USERNAME_SIZE);
            db.seek(recordOffset + INETADDRESS_OFFSET);
            db.read(threadIdBytes,0,User.INET_ADDRESS_SIZE);
            db.seek(recordOffset + QUESTION_OFFSET);
            db.read(questionBytes,0,NodeData.QUESTION_SIZE);
            db.seek(recordOffset + CELEBRITY_OFFSET);
            db.read(celebrityBytes,0,NodeData.CELEBRITY_NAME_SIZE);

            Integer id = Integer.parseInt(new String(idBytes).trim());
            String username = new String(usernameBytes).trim();
            String i = new String(threadIdBytes).toString().trim();
            Long threadId = Long.valueOf(i).longValue();
            String question = new String(questionBytes).trim();
            String celebrity = new String(celebrityBytes).trim();
            User user = new User(threadId,username);
            NodeData nodeData = new NodeData(user,question,celebrity);
            Node node = new Node(null,null,null,nodeData,id);

            String temp = new String(parentIdBytes).trim();
            if(!temp.isEmpty()) {
                Integer parentId = Integer.parseInt(temp);
                node.setParent(parentId);
            }
            temp = new String(yesIdBytes).trim();
            if(!temp.isEmpty()) {
                Integer yesId = Integer.parseInt(temp);
                node.setYes(yesId);
            }
            temp = new String(noIdBytes).trim();
            if(!temp.isEmpty()) {
                Integer noId = Integer.parseInt(temp);
                node.setNo(noId);
            }
            db.close();
            return node;
        } catch (IOException e) {
                e.printStackTrace();
            }
        return null;
    }

    private Integer readNodeId(long nodeNum) {
        byte[] idBytes = new byte[Node.NODE_ID_SIZE];
        try {
            db.seek(nodeNum * Node.RECORD_SIZE);
            db.read(idBytes,0,Node.NODE_ID_SIZE);

            String id = new String(idBytes).trim();
            return Integer.parseInt(id);
        } catch (IOException e) {
            e.printStackTrace();

        }
        return null;

    }
}
