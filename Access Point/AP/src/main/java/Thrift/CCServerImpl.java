package Thrift;

import controlModule.*;
import org.apache.thrift.TException;

import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

/**
 * the file receive function at the CC file server and CC system state server:
 * Function: 1) receive the file and store the data to the cache queue
 *           2) receive the system state
 */
class CCserverImpl implements  FileInfoExtractService.Iface{

    @Override
    public boolean uploadFile(FileData filedata) throws TException {
        String filePath = CCserver.node.getRootStorePath() + "/" + filedata.name;
        try
        {
            java.io.File file = new java.io.File(filePath);
            FileOutputStream fos = new FileOutputStream(file);
            FileChannel channel = fos.getChannel();
            channel.write(filedata.buff);
            channel.close();
            taskFile temp = taskFile.convertToTaskFile(filedata, filePath);


            if(CCserver.node.getLayer() == 1){
                if(temp.isTaskState()){
                    CCserver.schedule.insertTaskFileIntoResultQueue(temp);

                }else{
                    CCserver.schedule.insertTaskFileIntoComputingQueue(temp);
                }
                return true;
            }

            // layer 2
            if(temp.isTaskState())
            {
                CCserver.schedule.insertTaskFileIntoDeliverQueue(temp);
            }else {
                if (CCserver.schedule.InsertTransmissionBufferOrComputingBuffer()) {
                    CCserver.schedule.insertTaskFileIntoComputingQueue(temp);
                } else {
                    CCserver.schedule.insertTaskFileIntoDeliverQueue(temp);
                }
            }
        }
        catch (Exception x)
        {
            x.printStackTrace();
            return false;
        }

        return false;
    }
}
