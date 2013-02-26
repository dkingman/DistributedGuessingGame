import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import static java.lang.System.getProperty;

/**
 * User: David
 * Date: 2/26/13
 * Time: 11:12 AM
 */
public class Database {
    private RandomAccessFile db;
    private final String dbDir;

    private static final int RECORD_SIZE         = 300;
    private static final int NODE_ID_SIZE        = 4;
    private static final int CELEBRITY_NAME_SIZE = 40;
    private static final int USERNAME_SIZE       = 10;
    private static final int INET_ADDRESS_SIZE   = 40; // Find the correct size of the toString of an InetAddress

    public Database() {
        String currDir = getProperty("user.dir");
        dbDir = currDir + "\\database\\database.txt";
        System.out.println(dbDir);
    }
    public void initDb() {
        try {
            db = new RandomAccessFile(dbDir, "rw");
            try {
                db.seek(db.length());
                db.write("TESTING".getBytes());
                db.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
