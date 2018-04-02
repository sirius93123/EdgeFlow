package controlModule;

import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.server.TThreadedSelectorServer;
import org.apache.thrift.transport.*;

public class mainserver {

    public void startServer() {
        try {
            System.out.println("HelloWorld TSimpleServer start ....");

            // 创建非阻塞的 Transport
            TNonblockingServerTransport serverSocket = new TNonblockingServerSocket(12345);

            // 创建 Processor
            TProcessor processor = new FileInfoExtractService.Processor<FileInfoExtractService.i>(new FileServiceImpl());

            // 创建 transport factory , Nonblocking 使用 TFramedTransport
            TTransportFactory transportFactory = new TFramedTransport.Factory();

            // 创建 protocol factory
            TBinaryProtocol.Factory protocolFactory = new TBinaryProtocol.Factory();

            // 创建 arguments
            TThreadedSelectorServer.Args tArgs = new TThreadedSelectorServer.Args(serverSocket);
            tArgs.processor(processor);
            tArgs.transportFactory(transportFactory);
            tArgs.protocolFactory(protocolFactory);

            // 创建 server
            TServer server = new TThreadedSelectorServer(tArgs);

            // 启动 server
            server.serve();

        } catch (Exception e) {
            System.out.println("Server start error!!!");
            e.printStackTrace();
        }
    }
    public static void main(String[] args){
        System.out.println("dddddd");
        mainserver ta = new mainserver();
        ta.startServer();
    }
}
