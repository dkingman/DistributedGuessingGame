import java.net.InetAddress;

/**
 * User: David
 * Date: 2/25/13
 * Time: 10:21 PM
 */
public class User {
    private InetAddress inetAddress;
    private String name;

    public User(InetAddress inetAddress, String name) {
        this.inetAddress = inetAddress;
        this.name = name;
    }
}
