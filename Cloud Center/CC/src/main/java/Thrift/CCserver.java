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
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TNonblockingServerTransport;
import org.apache.thrift.transport.TTransportFactory;

/**
 * the File and System State Server at the CC layer
 * function:
 *         1) receive the task data from the connected CCs
 *         2) receive the system state from the CCs and EDs
 */
public class CCserver {

    public static NodeConfig node;
    public static ScheduleModule schedule;

    public CCserver(NodeConfig node, ScheduleModule schedule) {
        this.node = node;
        this.schedule = schedule;
    }

    /**
     * init the file server and system state server at the CC (Port: port)
     * @param port
     */

    public static void initServer(int port){
        try
        {

            TNonblockingServerTransport serverSocket = new TNonblockingServerSocket(port);
            TMultiplexedProcessor processor = new TMultiplexedProcessor();
            CCserverImpl ccImpl = new CCserverImpl();
            SystemInfoImpl syImpl = new SystemInfoImpl();
            processor.registerProcessor("uploadFile",new FileInfoExtractService.Processor<FileInfoExtractService.Iface>(ccImpl));
            processor.registerProcessor("upSystemState",new SystemInfoService.Processor<SystemInfoService.Iface>(syImpl));
            TTransportFactory transportFactory = new TFramedTransport.Factory();
            TBinaryProtocol.Factory protocolFactory = new TBinaryProtocol.Factory();
            TThreadedSelectorServer.Args tArgs = new TThreadedSelectorServer.Args(serverSocket);
            tArgs.processor(processor);
            tArgs.transportFactory(transportFactory);
            tArgs.protocolFactory(protocolFactory);


            TServer server = new TThreadedSelectorServer(tArgs);

            server.serve();

        }
        catch (Exception x)
        {
            x.printStackTrace();
        }


    }
}
