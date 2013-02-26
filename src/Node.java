import java.util.List;

/**
 * User: David
 * Date: 2/25/13
 * Time: 10:12 PM
 */
public class Node {
    private Node parent;
    private Node yes;
    private Node no;
    private NodeData nodeData;
    private Integer id;
    private static final int NODE_ID_SIZE = 4;
    private static final int NODE_SIZE    = (NODE_ID_SIZE + NodeData.RECORD_SIZE)*4;;
    public static final int RECORD_SIZE   = NODE_SIZE * 4;

    Node(Node parent, Node yes, Node no, NodeData nodeData, Integer id) {
        this.parent = parent;
        this.yes = yes;
        this.no = no;
        this.nodeData = nodeData;
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Node(NodeData nodeData) {
        this.nodeData = nodeData;
    }

    public Node() {
    }

    public NodeData getNodeData() {
        return nodeData;
    }

    public void setNodeData(NodeData nodeData) {
        this.nodeData = nodeData;
    }

    public byte[] getBytes() {
        byte[] result = new byte[RECORD_SIZE];
        byte[] idBytes = new byte[NODE_ID_SIZE];
        byte[] parentBytes = new byte[NODE_SIZE];
        byte[] yesBytes = new byte[NODE_SIZE];
        byte[] noBytes = new byte[NODE_SIZE];
        byte[] nodeDataBytes = new byte[NodeData.RECORD_SIZE];

        System.arraycopy(id.toString().getBytes(),0,idBytes,0,id.toString().getBytes().length);
        if(parent != null) System.arraycopy(parent.getBytes(),0,parentBytes,0,parent.toString().getBytes().length);
        if(no != null) System.arraycopy(no.getBytes(),0,noBytes,0,no.getBytes().length);
        if(yes != null) System.arraycopy(yes.getBytes(),0,yesBytes,0,yes.getBytes().length);
        System.arraycopy(nodeData.getBytes(),0,nodeDataBytes,0,nodeData.getBytes().length);

        System.arraycopy(idBytes,0,result,0,idBytes.length);
        System.arraycopy(parentBytes,0,result,NODE_ID_SIZE,parentBytes.length);
        System.arraycopy(yesBytes,0,result,NODE_ID_SIZE+NODE_SIZE,yesBytes.length);
        System.arraycopy(noBytes,0,result,NODE_ID_SIZE+NODE_SIZE*2,noBytes.length);
        System.arraycopy(nodeDataBytes,0,result,NODE_ID_SIZE+NODE_SIZE*3,nodeDataBytes.length);;

        return result;
    }
}
