package controlModule;

import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;

public class demoGenerationOrReceivingModule implements GenerationAndReceivingInterface {

    NodeConfig node;

    final static int EdgeLayer = 1;

    public demoGenerationOrReceivingModule(NodeConfig node){
        this.node = node;
    }

    @Override
    public void generationOrReceivingModule() {
        if(node.getLayer() == EdgeLayer){
            generationTask();
        }else{
            receivingTask();
        }
    }

    @Override
    public void generationTask() {

        while(true){
            generationDataTaskApi();

        }
    }

    @Override
    public void receivingTask() {

    }

    public void generationDataTaskApi(){

    }

    public void receiveTaskFileImpl(){
        try {
            System.out.println("HelloWorld TSimpleServer start ....");

            FileInfoExtractService.Processor<FileInfoExtractService.Iface> tprocessor = new FileInfoExtractService.Processor<FileInfoExtractService.Iface>(new fileService());


            // 简单的单线程服务模型，一般用于测试
            TServerSocket serverTransport = new TServerSocket(9000);
            TServer.Args tArgs = new TServer.Args(serverTransport);
            tArgs.processor(tprocessor);
//            tArgs.protocolFactory(new TBinaryProtocol.Factory());
            tArgs.protocolFactory(new TCompactProtocol.Factory());
            // tArgs.protocolFactory(new TJSONProtocol.Factory());
            TServer server = new TSimpleServer(tArgs);
            server.serve();

        } catch (Exception e) {
            System.out.println("Server start error!!!");
            e.printStackTrace();
        }
    }

}
