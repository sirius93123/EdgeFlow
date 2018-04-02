package controlModule;

import org.apache.thrift.TException;

import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

public class fileService implements  FileInfoExtractService.Iface {
    @Override
    public boolean uploadFile(FileData filedata) throws TException {
        String filePath = "w.file";
        System.out.println(filedata.name);
        try
        {
            java.io.File file = new java.io.File(filePath);
            FileOutputStream fos = new FileOutputStream(file);
            FileChannel channel = fos.getChannel();
            channel.write(filedata.buff);
            channel.close();
        }
        catch (Exception x)
        {
            x.printStackTrace();
            return false;
        }

        return false;
    }


}
