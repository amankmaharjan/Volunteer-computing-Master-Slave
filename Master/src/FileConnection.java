

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

/**
 * file name: FileConnection.java
 * This class downloads class file to the client.
 * @author aman Class for file download
 */
public class FileConnection extends Thread {

    // Field declaration of streams, socket and task
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;
    Socket clientSocket;

    // Constructor
    public FileConnection(Socket aClientSocket) {
        try {
            
            //Initializing socket and streams
            clientSocket = aClientSocket;
            dataInputStream = new DataInputStream(clientSocket.getInputStream());
            dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
            
            //calling the run method
            this.start();
        } catch (IOException e) {
            System.out.println("Connection:" + e.getMessage());
        }
    }

    @Override
    public void run() {
        // Variable for storing file name
        String ClassName = new String();
        while (true) {
            try {
                //Read the file name
                ClassName = dataInputStream.readUTF();
                //Send the file  to  client
                File ClassFile = new File("Contract\\" + ClassName);
                byte[] buffer = new byte[(int) ClassFile.length()];
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(ClassFile));
                bis.read(buffer, 0, buffer.length);
                dataOutputStream.write(buffer, 0, buffer.length);
                dataOutputStream.flush();
                bis.close();
            } catch (EOFException e) {
                System.out.println("EOF" + e.getMessage());
                break;
            } catch (FileNotFoundException e) {
                System.out.println("File " + ClassName + " cannot find.");
                break;
            } catch (SocketException e) {
                System.out.println("Client closed.");
                break;
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
