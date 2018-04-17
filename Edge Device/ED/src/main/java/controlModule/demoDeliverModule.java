package controlModule;

import org.apache.thrift.TApplicationException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransportException;

import javax.swing.plaf.nimbus.State;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;

/**
 * the demo delivering implementation
 */
public class demoDeliverModule implements  DeliverInterface {

    int timeout = 60000; // socket timeout
    TFramedTransport framedTransportFile;  // the binder for file upload
    TFramedTransport framedTransportUpdate; // the binder for system state upload
    FileInfoExtractService.Client clientFile; // the client for file upload
    SystemInfoService.Client clientService;   // the client for system state upload
    boolean flag = false;                     // flag to judge the file transmission state
    boolean StateFlag = false;                // flag to judge the data for systemState Updata
    SystemState tState;
    String ip;                                // uplayer ip
    int port;                                 // uplayer port

    /**
     * init the binder for file update to the server at (IP, Port)
     * @param ip
     * @param port
     */
    public void initFile(String ip, int port){
        try
        {
            this.ip = ip;
            this.port = port;
            TSocket socket = new TSocket(ip, port);
            socket.setTimeout(timeout);
            framedTransportFile = new TFramedTransport(socket);
            framedTransportFile.open();
            TBinaryProtocol binaryProtocol = new TBinaryProtocol(framedTransportFile);
            TMultiplexedProtocol mp = new TMultiplexedProtocol(binaryProtocol, "uploadFile");
            clientFile = new FileInfoExtractService.Client(mp);
        }
        catch (Exception x)
        {
            x.printStackTrace();
        }
    }

    /**
     * init the system update binder
     * @param CCip
     * @param CCport
     */
    public void initServerAP(String CCip, int CCport){
        try
        {

            framedTransportUpdate = framedTransportFile;
            TBinaryProtocol binaryProtocol = new TBinaryProtocol(framedTransportUpdate);
            TMultiplexedProtocol mp2 = new TMultiplexedProtocol(binaryProtocol, "upSystemState");
            clientService = new SystemInfoService.Client(mp2);
        }
        catch (Exception x)
        {
            x.printStackTrace();
        }
    }

    /**
     * init the file upload binder
     * @param CCip
     * @param CCport
     */
    public void initServer(String CCip, int CCport){
        try
        {
            TSocket socket = new TSocket(CCip, CCport);
            socket.setTimeout(timeout);
            framedTransportUpdate = new TFramedTransport(socket);
            framedTransportUpdate.open();
            TBinaryProtocol binaryProtocol = new TBinaryProtocol(framedTransportUpdate);
            TMultiplexedProtocol mp2 = new TMultiplexedProtocol(binaryProtocol, "upSystemState");
            clientService = new SystemInfoService.Client(mp2);
        }
        catch (Exception x)
        {
            x.printStackTrace();
        }
    }


    /**
     * transmit the taskFile to the uplayer Server
     * @param temp
     * @param ip
     * @param port
     */
    void fileTransmission(taskFile temp, String ip, int port){
        flag = true;
        long time1 = System.currentTimeMillis();
        if(!temp.dataFile.exists()){
            flag = false;
            return;
        }
        byte[] bytes = toByteArray(temp.dataFile);
        if(bytes.length > 100000){
            flag = false;
            return;
        }
        FileData fileData = new FileData();
        fileData.name = temp.dataFile.getName();
        fileData.buff = ByteBuffer.wrap(bytes);
        fileData.state = temp.isTaskState();
        fileData.generationTime = temp.generationTime + System.currentTimeMillis() - time1;

        try
        {
            try {
                if (framedTransportFile.isOpen()) {
                    clientFile.uploadFile(fileData);
                    if (temp.dataFile.exists()) {
                        temp.dataFile.delete();
                    }
                }
            }
            catch(TTransportException x){
                framedTransportFile.close();
                initFile(this.ip, this.port);
                initServerAP(this.ip, this.port);
                return;
            }
        }
        catch (Exception x)
        {
            x.printStackTrace();
        }

        flag = false;
        if(StateFlag == true){
            testSystemInfo(ip, port, tState);
            StateFlag = false;
        }



    }


    /**
     * transmit the system state to the CC server
     * @param ip
     * @param port
     * @param state
     */
    public void testSystemInfo(String ip , int port, SystemState state){
        try
        {
            if(framedTransportUpdate.isOpen()) {
                try {
                    clientService.upSystemState(state);
                }
                catch(TApplicationException x)
                {
                    return;
                    //if(framedTransportUpdate.isOpen()){

                    //}
                }
            }
        }
        catch (Exception x)
        {
            x.printStackTrace();
        }
    }


    /**
     * load the file to the bytes[]
     * @param file
     * @return
     */
    private static byte[] toByteArray(File  file){
        byte[] buffer = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer;
    }

    /**
     * the implementation for uploading the taskFile to the uplayer server
     * @param temp
     * @param ip
     * @param port
     */
    @Override
    public void deliverTaskFile(taskFile temp, String ip, int port) {
        fileTransmission(temp, ip ,port);
    }

    /**
     * the implementation for uploading the systemState to the CC server
     * @param temp
     * @param ip
     * @param port
     */
    @Override
    public void deliverSystem(SystemState temp, String ip, int port) {
        if(flag == false) {
            testSystemInfo(ip, port, temp);
        }else{
            StateFlag = true;
            tState = temp;
        }
    }
}
