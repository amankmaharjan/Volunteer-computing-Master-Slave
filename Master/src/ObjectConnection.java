

import Contract.CalculateGCD;
import Contract.CalculatePi;
import Contract.CalculatePrime;
import Contract.Task;
import Contract.TaskList;
import Contract.TaskObject;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * file name: ObjectConnection.java Thread class that performs task and tasklist
 *
 * @author aman
 */
public class ObjectConnection extends Thread {

    // Field declaration for tasklist, stream, taskname, taskclass name
    TaskList taskList;
    ObjectInputStream objectInputStream;
    ObjectOutputStream objectOutputStream;
    String[] taskName = {"Calculating Pi to 50 decimal digits", "Calculating Prime from 1 to 70", "Calculating GCD of 128 and 76", "calculating  pi to 70 decimal digits", "Calculating prime from 1 to 100", "Calculating GCD of 252 and 24"};
    String[] taskClassName = {"CalculatePi.class", "CalculatePrime.class", "CalculateGCD.class", "CalculatePi.class", "CalculatePrime.class", "CalculateGCD.class"};
    TaskObject taskObject;

    // Constructor
    public ObjectConnection(Socket aClientSocket) {
        try {
            objectInputStream = new ObjectInputStream(aClientSocket.getInputStream());
            objectOutputStream = new ObjectOutputStream(aClientSocket.getOutputStream());
            this.start();
        } catch (IOException e) {
            System.out.println("Connection:" + e.getMessage());
        }
    }

    // Thread class that performs task
    @Override
    public void run() {
        try {
            // Send task list
            sendTaskList();
            while (true) {

                // 3. The Master receives the TaskObject, creates the compute-task object (e.g. CalculatePi) according to the compute-task ID.
                taskObject = (TaskObject) objectInputStream.readObject();
                System.out.println("The task:<" + taskName[taskObject.getTaskID()] + " " + "is asked by a worker");
                System.out.println("The class file of " + taskClassName[taskObject.getTaskID()] + " " + "has been  transferred to  Worker.");
                Task iTask = null;

                // checking TaskID to make type of class
                if (taskObject.getTaskID() == 0) {
                    iTask = new CalculatePi(50);
                } else if (taskObject.getTaskID() == 1) {
                    iTask = new CalculatePrime(70);
                } else if (taskObject.getTaskID() == 2) {
                    iTask = new CalculateGCD(128, 76);
                } else if (taskObject.getTaskID() == 3) {
                    iTask = new CalculatePi(70);
                } else if (taskObject.getTaskID() == 4) {
                    iTask = new CalculatePrime(100);
                } else if (taskObject.getTaskID() == 5) {
                    iTask = new CalculateGCD(252, 24);
                }
                // Sending task object
                taskObject.setTObject(iTask);

                //4. The Master sets the compute-task object on the TaskObject and sends the TaskObject to the Worker.
                objectOutputStream.writeObject(taskObject);

                //8. The Master receives the TaskObject and retrieves the results.                System.out.println("The task:"+""+taskClassName[taskObject.getTaskID()]+" is performed by a worker, thre result is:"+task.);
                taskObject = (TaskObject) objectInputStream.readObject();
                System.out.println("The task:" + "" + taskClassName[taskObject.getTaskID()] + " is performed by a worker, the result is:" + taskObject.getTObject().getResult());

                //9. The Master sets a credit on the TaskObject and sends it to the Worker by checking the object type
                if (iTask instanceof CalculatePi) {
                    taskObject.setCredit(30);
                }
                if (iTask instanceof CalculateGCD) {
                    taskObject.setCredit(20);
                }
                if (iTask instanceof CalculatePrime) {
                    taskObject.setCredit(10);
                }
                //Printing and sending the credit to the client
                System.out.println("Awarded credit of " + taskObject.getCredit() + " " + "to a Worker.");
                objectOutputStream.writeObject(taskObject);
                System.out.println("------------------------------");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // Method that sends tasklist to the client
    private void sendTaskList() throws ClassNotFoundException, IOException {
        // First read the taskList object from the client
        Object object = objectInputStream.readObject();

        if (object instanceof TaskList) {
            taskList = (TaskList) object;
            taskList.setAvailableTasks(taskName);
            taskList.setTaskClassName(taskClassName);
            objectOutputStream.writeObject(taskList);
            System.out.println("------------------------------");
            System.out.println("List of available  compute-tasks has been transferred to a Worker.");

        }
    }
}
