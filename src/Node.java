import java.util.List;

/**
 * User: David
 * Date: 2/25/13
 * Time: 10:12 PM
 */
public class Node<NodeData> {
    private Node parent;
    private Node<NodeData> left;
    private Node<NodeData> right;
    private NodeData nodeData;

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
}
