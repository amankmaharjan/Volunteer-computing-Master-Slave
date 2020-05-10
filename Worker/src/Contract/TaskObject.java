package Contract;


import java.io.Serializable;

/**
 * file name: TaskObject.java
 * Class that stores  taskID, credit, Task object
 * @author aman
 */
public class TaskObject implements Serializable {

    // Fields for  taskId, credit, taskobject
    private Integer TaskID = 0;
    private Integer Credit = 0;
    private Task TObject = null;

    //Constructor
    public TaskObject() {
    }
    
    // Getter
    public Integer getTaskID() {
        return TaskID;
    }
    // Setter
    public void setTaskID(Integer TaskID) {
        this.TaskID = TaskID;
    }

    public Integer getCredit() {
        return Credit;
    }

    public void setCredit(Integer Credit) {
        this.Credit = Credit;
    }

    public Task getTObject() {
        return TObject;
    }

    public void setTObject(Task TObject) {
        this.TObject = TObject;
    }

}
