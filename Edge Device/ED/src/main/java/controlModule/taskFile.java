package controlModule;

import java.io.File;
import java.io.Serializable;

/**
 * the class for the task data
 * generation time : the time of the data generation
 * finish time: the time of the task finish
 * taskState: task Finish: true, task unfinish: false
 * dataFile: the file link for the task data
 */
public class taskFile implements Serializable {

    public taskFile(String path, long generationTime, long finishTime, boolean taskState) {
        this.generationTime = generationTime;
        this.finishTime = finishTime;
        this.taskState = taskState;
        this.dataFile = new File(path);
    }

    public long getGenerationTime() {
        return generationTime;
    }

    public void setGenerationTime(long generationTime) {
        this.generationTime = generationTime;
    }

    long generationTime;
    long finishTime;

    public boolean isTaskState() {
        return taskState;
    }

    boolean taskState;

    File dataFile;

    void setTaskState(boolean state){
        taskState = state;
    }

    public static FileData convertFromTaskData(taskFile temp){
        return null;
    }

    public static taskFile convertToTaskFile(FileData temp, String path){
        //taskFile te = new taskFile(path,temp.generationTime,temp.state);
        taskFile tp = new taskFile(temp.name, temp.generationTime, - 1, temp.state);
        File tFile = new File(path);
        if(tFile.exists()){
            tp.dataFile = tFile;
            //tp.
        }
        return tp;
    }

}
