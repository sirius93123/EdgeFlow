import StaticModule.staticClass;
import controlModule.*;
import org.apache.commons.cli.*;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.*;
import utis.FileCopy;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Scanner;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.xml.soap.Node;


public class main {
    /**
     * test function
     * change the file to the bytes[]
     * */
    private static byte[] toByteArray(File file){
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
     * @param userName
     */
    public static void startClient(String userName) {
       // long time1 = System.currentTimeMillis();

        File t5 = new File(userName);
        // 构造文件数据
        byte[] bytes = toByteArray(t5);
        FileData fileData = new FileData();
        fileData.name = t5.getName();
        fileData.buff = ByteBuffer.wrap(bytes);

        // 构造Thrift客户端，发起请求
        try
        {
            TSocket socket = new TSocket("192.168.2.1", 1236);
            socket.setTimeout(60000);
            TFramedTransport framedTransport = new TFramedTransport(socket);
            framedTransport.open();
            TBinaryProtocol binaryProtocol = new TBinaryProtocol(framedTransport);
            TMultiplexedProtocol mp = new TMultiplexedProtocol(binaryProtocol, "uploadFile");
            FileInfoExtractService.Client client = new FileInfoExtractService.Client(mp);
            long time1 = System.currentTimeMillis();
            client.uploadFile(fileData);
            NodeConfig.debug("Timee   "+ (System.currentTimeMillis() - time1));
            time1 = System.currentTimeMillis();
            client.uploadFile(fileData);
            NodeConfig.debug("Timee   "+ (System.currentTimeMillis() - time1));
            time1 = System.currentTimeMillis();
            client.uploadFile(fileData);
            NodeConfig.debug("Timee   "+ (System.currentTimeMillis() - time1));
            Thread.sleep(1000);
            time1 = System.currentTimeMillis();
            client.uploadFile(fileData);
            NodeConfig.debug("Timee   "+ (System.currentTimeMillis() - time1));
            TMultiplexedProtocol mp2 = new TMultiplexedProtocol(binaryProtocol, "upSystemState");
            SystemInfoService.Client client2 = new SystemInfoService.Client(mp2);
            SystemState temp2 = new SystemState();
            temp2.buffersize = 10;
            temp2.name="ED11";
            temp2.computingSpeed=0;
            temp2.transmissionSpeed = 0;
            long time3 = System.currentTimeMillis();
            client2.upSystemState(temp2);
            NodeConfig.debug("Timee   "+ (System.currentTimeMillis() - time3));
            framedTransport.close();
        }
        catch (Exception x)
        {
            x.printStackTrace();
        }

    }

    public static void testNodeConfig(NodeConfig node){
        node.setGenerationDelta(1000);
    }


    /**
     * @Description: the main function, load the node config
     * @Param:
     * @return: void
     */

    public static void main(String[] args) throws ParseException {

        NodeConfig.logger.debug("System Start");
        CommandLineParser parser = new BasicParser( );
        Options options = new Options( );
        options.addOption("h", "help", false, "Print this usage information");
        options.addOption("v", "verbose", false, "Print out VERBOSE information" );
        options.addOption("f", "file", true, "File to save program output to");
        // Parse the program arguments
        CommandLine commandLine = parser.parse(options, args );
        // Set the appropriate variables based on supplied options
        boolean verbose = false;
        String file = "";

        if( commandLine.hasOption('h') ) {
            System.out.println( "Help Message");
            System.exit(0);
        }
        if( commandLine.hasOption('v') ) {
            verbose = true;
        }
        if( commandLine.hasOption('f') ) {
            file = commandLine.getOptionValue('f');
        }

        int mode = 0;

        System.out.print("Choose Mode");
        Scanner input = new Scanner(System.in);
        mode = input.nextInt();
        System.out.println(mode);

        switch(mode){
            case 3:
                NodeConfig nodec = new  NodeConfig(true, 3 , 3, "/home/t3/ED","192.168.2.2", 1234, "192.168.2.1",1236, 500);
                NodeConfig.logger.debug("Init default node");
                main.testNodeConfig(nodec);
                nodeCC.setStrategyPercentage(0.5);
                nodec.setGenerationDelta(250);
                nodec.setComputeSpeed(500);
                nodec.setNodeName("ED1");
                NodeConfig.debug(nodec.getGenerationDelta());
                ScheduleModule schedule = new ScheduleModule(nodec, nodec.getThreadNumber());
                schedule.initConfig();
                break;
            case 2:
                NodeConfig nodeA = new  NodeConfig(true, 2 , 2, "/home/t4/AP","192.168.2.1", 1236, "192.168.2.1",1236, 1000);
                nodeA.setFilePort(1234);
                nodeCC.setStrategyPercentage(0.5);
                NodeConfig.logger.debug("Init default node");
                main.testNodeConfig(nodeA);
                nodeA.setComputeSpeed(167);
                nodeA.setNodeName("AP1");
                NodeConfig.debug(nodeA.getGenerationDelta());
                ScheduleModule schedulec = new ScheduleModule(nodeA, nodeA.getThreadNumber());
                schedulec.initConfig();
                break;
            case 1:
                staticClass.initstaticClass();
                NodeConfig nodeCC = new  NodeConfig(true, 1 , 2, "/home/t5/CC","127.0.0.1", 1235, "127.0.0.1",2000, 1000);
                nodeCC.setFilePort(1236);
                nodeCC.setStrategyPercentage(0.5);
                NodeConfig.logger.debug("Init default node");
                main.testNodeConfig(nodeCC);
                nodeCC.setComputeSpeed(50);
                NodeConfig.debug(nodeCC.getGenerationDelta());
                ScheduleModule scheduleCC = new ScheduleModule(nodeCC, nodeCC.getThreadNumber());
                scheduleCC.initConfig();
                break;
            case 0:
                main.startClient("/home/t4/Desktop/q.tar");
                break;
        }

    }
}
