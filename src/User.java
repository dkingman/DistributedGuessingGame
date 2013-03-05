
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
        byte[] inetAddressBytes = new byte[INET_ADDRESS_SIZE];

        System.arraycopy(name.getBytes(),0,nameBytes,0,name.getBytes().length);
        if(inetAddress != null) {
            System.arraycopy(inetAddress.toString().getBytes(),0,inetAddressBytes,0,inetAddress.toString().getBytes().length);
        }
        System.out.println(new String(nameBytes));
        System.arraycopy(nameBytes,0,result,0,nameBytes.length);
        System.arraycopy(inetAddressBytes,0,result,USERNAME_SIZE,inetAddressBytes.length);

        return result;
    }
}
