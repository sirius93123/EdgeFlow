package Thrift;

import controlModule.FileInfoExtractService;
import controlModule.NodeConfig;
import controlModule.ScheduleModule;
import controlModule.SystemInfoService;
import org.apache.thrift.TMultiplexedProcessor;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadedSelectorServer;
import org.apache.thrift.transport.*;

import static Thrift.APserverImpl.initImpl;

/**
 * the File Server at the AP layer
 * function: receive the task data from the connected EDs
 */
public class APserver{

    public static NodeConfig node;
    public static ScheduleModule schedule;

    public APserver(NodeConfig node, ScheduleModule schedule) {
        this.node = node;
        this.schedule = schedule;
    }

    /**
     * init file server at the Port
     * @param port
     */
    public void initServer(int port){
        try {
            TNonblockingServerTransport serverSocket = new TNonblockingServerSocket(port);

            TMultiplexedProcessor processor = new TMultiplexedProcessor();
            APserverImpl apImpl = new APserverImpl();
            SystemInfoImpl syImpl = new SystemInfoImpl();
            processor.registerProcessor("uploadFile",new FileInfoExtractService.Processor<FileInfoExtractService.Iface>(apImpl));
            TTransportFactory transportFactory = new TFramedTransport.Factory();
            TBinaryProtocol.Factory protocolFactory = new TBinaryProtocol.Factory();
            TThreadedSelectorServer.Args tArgs = new TThreadedSelectorServer.Args(serverSocket);
            tArgs.processor(processor);
            tArgs.transportFactory(transportFactory);
            tArgs.protocolFactory(protocolFactory);
            TServer server = new TThreadedSelectorServer(tArgs);
            server.serve();

        } catch (TTransportException e) {
            e.printStackTrace();
        }


    }


}
