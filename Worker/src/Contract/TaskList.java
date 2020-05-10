package Contract;


import java.io.Serializable;

/**
 * file name: TaskList.java
 * Class that stores task, taskclass names
 * @author aman
 */
public class TaskList implements Serializable{

    // fields that stores teh task name and class name
    private String AvailableTasks[];
    private String TaskClassName[];

    //getter
    public String[] getAvailableTasks() {
        return AvailableTasks;
    }
    //setter
    public void setAvailableTasks(String[] AvailableTasks) {
        this.AvailableTasks = AvailableTasks;
    }
    //getter
    public String[] getTaskClassName() {
        return TaskClassName;
    }
    //setter
    public void setTaskClassName(String[] TaskClassName) {
        this.TaskClassName = TaskClassName;
    }

}
