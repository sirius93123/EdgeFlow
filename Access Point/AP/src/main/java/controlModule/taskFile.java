package controlModule;

import java.io.File;
import java.io.Serializable;

public class taskFile implements Serializable {

    long generationTime;
    long finishTime;

    boolean taskState;

    File dataFile;

    void setTaskState(boolean state){
        taskState = state;
    }


}
