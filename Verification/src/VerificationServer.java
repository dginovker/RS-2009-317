import com.sun.net.httpserver.HttpServer;
import util.Constants;
import util.PrintStreamLogger;

import java.io.FileNotFoundException;
import java.net.InetSocketAddress;

/**
 * @author Corey
 */
public class VerificationServer {

    public static void main(String[] args) throws Exception {
        try {
            initLoggers();
        } catch (FileNotFoundException e) {
            System.err.println("Failed to start Verification Server!");
            System.exit(1);
        }

        if (!RequestType.allFilesAvailable()) {
            System.err.println("Failed to start Verification Server - not all files available!");
            System.exit(1);
        }

        HttpServer server = HttpServer.create(new InetSocketAddress(Constants.HTTP_PORT), 0);
        server.createContext("/", new RequestHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
        System.out.println("Validation Server Initialised");
    }

    private static void initLoggers() throws FileNotFoundException {
        System.setOut(new PrintStreamLogger());
        System.setErr(new PrintStreamLogger());
    }

    /*public static void main(String[] args) {
        File file = new File("100MB.bin");
        long startTime = System.currentTimeMillis();
        try {
            MessageDigest md5Digest = MessageDigest.getInstance("SHA-256");
            String checksum = getFileChecksum(md5Digest, file);

            System.out.println(checksum);
            long timeTaken = System.currentTimeMillis() - startTime;
            System.out.println(timeTaken);

        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }

    }*/

}
