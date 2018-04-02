import controlModule.FileData;
import controlModule.FileInfoExtractService;
import controlModule.ThriftService;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;

public class main {

    /**
     * 文件转化为字节数组
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
    public void startClient(String userName) {

        File t5 = new File(userName);
        // 构造文件数据
        byte[] bytes = toByteArray(t5);
        FileData fileData = new FileData();
        fileData.name = t5.getName();
        fileData.buff = ByteBuffer.wrap(bytes);

        // 构造Thrift客户端，发起请求
        try
        {
            TSocket socket = new TSocket("localhost", 12345);
            socket.setTimeout(60000);
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

    public static void main(String[] args){
        System.out.println("dddddd");
        main ta = new main();
        ta.startClient("1233");

    }
}
