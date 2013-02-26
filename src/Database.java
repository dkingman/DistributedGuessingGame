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
    public static int recordCount = 1;

    public Database() {
        String currDir = getProperty("user.dir");
        dbDir = currDir + "\\database\\database.txt";
        System.out.println(dbDir);
    }
    public void initDb() {
        try {
            db = new RandomAccessFile(dbDir, "rw");
            try {
                System.out.println("DBlen = " + db.length() + " record_Size = " + Node.RECORD_SIZE);
                db.seek(db.length()-Node.RECORD_SIZE);

                // Update the record count to the 1 + the last saved ID
                byte[] recordByte = new byte[1];
                recordByte[0] = db.readByte();
                recordCount = new Integer(new String(recordByte)) + 1;
            } catch (IOException e) {
                e.printStackTrace();

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void write(Node node) {
        try {
            db.seek(db.length());
            db.write(node.getBytes());
            db.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
