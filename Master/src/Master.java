import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * file name: Master.java
 *
 * @author aman 
 * Class for Object and file server
 *
 */
public class Master {
    //  Fields for object and file  port
    static int objectPort = 6789;
    static int filePOrt = 6790;

    // Main method
    public static void main(String[] args) {
        // Thread for  Object connnection
        System.out.println("------------------------------");
        new Thread(() -> {
            try {
                //  Creating Object server socket
                ServerSocket ss = new ServerSocket(objectPort);
                while (true) {
                    Socket socket = ss.accept(); // blocking call, this will wait until a connection is attempted on this port.
                    new ObjectConnection(socket);
                }
            } catch (IOException e) {
                System.out.println("Listen socket:" + e.getMessage());
            }
        }).start();
        System.out.println("The server is listening on port 6789 for Object transfer...");

        // Thread for file connection
        new Thread(() -> {
            try {
                // Creating file server socket
                ServerSocket listenSocket = new ServerSocket(filePOrt);
                while (true) {
                    Socket clientSocket = listenSocket.accept();
                    new FileConnection(clientSocket);
                }
            } catch (IOException e) {
                System.out.println("Listen socket:" + e.getMessage());
            }
        }).start();
        System.out.println("The server is listening on port 6790 for file transfer...");
        System.out.println("------------------------------");

    }

}
