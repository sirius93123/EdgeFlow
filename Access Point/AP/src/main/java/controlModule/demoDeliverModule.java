package controlModule;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;

public class demoDeliverModule implements  DeliverInterface {

    int timeout = 60000;
    @Override
    public void deliverTaskFile(taskFile temp) {

    }

    void fileTransmission(taskFile temp){

        // 构造文件数据
        byte[] bytes = toByteArray(temp.dataFile);
        FileData fileData = new FileData();
        fileData.name = temp.dataFile.getName();
        fileData.buff = ByteBuffer.wrap(bytes);

        // 构造Thrift客户端，发起请求
        try
        {
            TSocket socket = new TSocket("localhost", 12345);
            socket.setTimeout(timeout);
            TFramedTransport framedTransport = new TFramedTransport(socket);
            framedTransport.open();
            TBinaryProtocol binaryProtocol = new TBinaryProtocol(framedTransport);
            FileInfoExtractService.Client client = new FileInfoExtractService.Client(binaryProtocol);
            client.uploadFile(fileData);
        }
        catch (Exception x)
        {
            x.printStackTrace();
        }

    }

    /**
     * 文件转化为字节数组
     * */
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
}
