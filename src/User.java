import java.net.InetAddress;

/**
 * User: David
 * Date: 2/25/13
 * Time: 10:21 PM
 */
public class User {
    private String inetAddress;
    private String name;

    public static final int INET_ADDRESS_SIZE   = 40; // Find the correct size of the toString of an InetAddress

    public static final int USERNAME_SIZE   = 40;
    public static final int RECORD_SIZE = INET_ADDRESS_SIZE + USERNAME_SIZE;

    public User(String inetAddress, String name) {
        this.inetAddress = inetAddress;
        this.name = name;
    }

    public byte[] getBytes() {
        byte[] result = new byte[RECORD_SIZE];

        byte[] nameBytes = new byte[USERNAME_SIZE];
        byte[] threadIdBytes = new byte[INET_ADDRESS_SIZE];

        System.arraycopy(name.getBytes(),0,nameBytes,0,name.getBytes().length);
        if(inetAddress != null) {
            System.arraycopy(inetAddress.toString().getBytes(),0,threadIdBytes,0,inetAddress.toString().getBytes().length);
        }
        System.arraycopy(nameBytes,0,result,0,nameBytes.length);
        System.arraycopy(threadIdBytes,0,result,USERNAME_SIZE,threadIdBytes.length);

        return result;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInetAddress() {
        return inetAddress;
    }

    public void setInetAddress(String inetAddress) {
        this.inetAddress = inetAddress;
    }

}
