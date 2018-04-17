package Thrift;

import controlModule.*;
import org.apache.thrift.TException;

import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

/**
 * the file receive function at the AP file server : receive the file and store the data to the cache queue
 */
class APserverImpl implements  FileInfoExtractService.Iface{
    public static NodeConfig node;
    public static ScheduleModule schedule;

    /**
     * init function
     * @param node
     * @param schedule
     */
    public static void initImpl(NodeConfig node, ScheduleModule schedule){
        node = node;
        schedule = schedule;
    }

    /**
     * file receive function
     * @param filedata
     * @return
     * @throws TException
     */
    @Override
    public boolean uploadFile(FileData filedata) throws TException {
        String filePath = APserver.node.getRootStorePath() + "/" + filedata.name;
        String filePath2 = APserver.node.getRootStorePath() +"/ED2"+filedata.name;
        try
        {
            java.io.File file = new java.io.File(filePath);
            FileOutputStream fos = new FileOutputStream(file);
            FileChannel channel = fos.getChannel();
            channel.write(filedata.buff);
            channel.close();
            taskFile temp = taskFile.convertToTaskFile(filedata, filePath);

            /**
             * judge to data task to which queue, computing or delivering
             */
            // layer 1

            if(APserver.node.getLayer() == 1){
                if(temp.isTaskState()){
                    APserver.schedule.insertTaskFileIntoResultQueue(temp);
                }else{
                    APserver.schedule.insertTaskFileIntoComputingQueue(temp);
                }
                return true;
            }

            // layer 2
            if(temp.isTaskState())
            {
                //NodeConfig.debug("insert into the deliver queue");
                APserver.schedule.insertTaskFileIntoDeliverQueue(temp);
            }else {
                if (APserver.schedule.InsertTransmissionBufferOrComputingBuffer()) {
                    APserver.schedule.insertTaskFileIntoComputingQueue(temp);
                } else {
                    APserver.schedule.insertTaskFileIntoDeliverQueue(temp);
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