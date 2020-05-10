
import Contract.Task;
import Contract.TaskList;
import Contract.TaskObject;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * file name: Worker.java
 *
 * @author aman Class for Object and file server
 *
 */
public class Worker {

    // Fields declaration strams, tasklist and filenames
    private static Socket objectSocket;
    static ObjectInputStream objectInputStream;
    static ObjectOutputStream objectOutputStream;
    static DataInputStream dataInputStream;
    static DataOutputStream dataOutputStream;
    static TaskList taskList;
    static String[] fileNames = {"Task.class", "TaskList.class", "TaskObject.class"};

    // Method that connect to the file and object server
    public static void connect(String hostName, int objectPort, int filePort) {

        //Object Connection
        objectConnection(hostName, objectPort, filePort);

        // file connection
        fileConnection(hostName, filePort);
    }

    // Method that establish file connnection
    private static void fileConnection(String hostName, int filePort) {

        for (String fileName : fileNames) {
            try {
                //arguments supply message, hostname of destination and client ID
                objectSocket = new Socket(hostName, filePort);
                dataOutputStream = new DataOutputStream(objectSocket.getOutputStream());
                dataInputStream = new DataInputStream(objectSocket.getInputStream());
                //file download
                downloadFile(fileName);
            } catch (IOException ex) {
                Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    // Method that  establish object connection 
    private static void objectConnection(String hostName, int objectPort, int filePort) {
        // for object connection
        try {
            //arguments supply message, hostname of destination and client ID
            System.out.println("hostname:" + hostName + "object Port:" + objectPort + "file port:" + filePort);
            objectSocket = new Socket(hostName, objectPort);
            objectOutputStream = new ObjectOutputStream(objectSocket.getOutputStream());
            objectInputStream = new ObjectInputStream(objectSocket.getInputStream());

        } catch (IOException ex) {
            Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Method that perform task operation
    public TaskObject performTask(int taskID) throws IOException, ClassNotFoundException {

        // Downlaod the class files
        String[] taskClassNmae = taskList.getTaskClassName();
        downloadFile(taskClassNmae[taskID]);
        //2.The worker creates a TaskObject and sets the selected task ID on the TaskObject and then send the TaskObject to the Master.
        TaskObject taskObject = new TaskObject();
        taskObject.setTaskID(taskID);
        objectOutputStream.writeObject(taskObject);
        //5.The Worker receives the TaskObject and gets the compute-task (e.g. CalculatePi) from the TaskObject.
        taskObject = (TaskObject) objectInputStream.readObject();
        Task iTask = taskObject.getTObject();
        //6.The compute-task object (e.g. CalculatePi) is cast (be deserialized) into the Task interface type and its executeTask() is called.
        iTask.executeTask();
        taskObject.setTObject(iTask);
        //7. The Worker sends the same TaskObject to the Master, which includes the computing results now.
        objectOutputStream.writeObject(taskObject);
        //10. The Worker receives the TaskObject and retrieves the awarded credit.
        taskObject = (TaskObject) objectInputStream.readObject();
        return taskObject;

    }

    // Method for getting task list
    public String[] getTaskList() throws IOException, ClassNotFoundException {

        //creating empty tasklist
        taskList = new TaskList();
        //sending empty task list
        objectOutputStream.writeObject(taskList);
        //read the task list from server
        taskList = (TaskList) objectInputStream.readObject();
        System.out.println(taskList.getAvailableTasks());
        return taskList.getAvailableTasks();
    }

    // Method that downloads file
    public static void downloadFile(String fileName) {

        try {
            //Send the file name
            dataOutputStream.writeUTF(fileName);
            //Receive the file
            File cf = new File("Contract\\" + fileName);
            FileOutputStream fo = new FileOutputStream(cf);
            BufferedOutputStream bos = new BufferedOutputStream(fo);
            byte[] buffer = new byte[8089];
            int byteRead = dataInputStream.read(buffer, 0, buffer.length);
            bos.write(buffer, 0, byteRead);
            bos.flush();
            bos.close();
            System.out.println("File " + fileName + " downloaded.");
        } catch (IOException ex) {
            Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
