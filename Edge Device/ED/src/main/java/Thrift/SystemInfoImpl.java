package Thrift;

import StaticModule.staticClass;
import controlModule.NodeConfig;
import controlModule.SystemInfoService;
import controlModule.SystemState;
import org.apache.thrift.TException;

import javax.xml.soap.Node;

public class SystemInfoImpl implements SystemInfoService.Iface {
    @Override
    public boolean upSystemState(SystemState systemdata) throws TException {
        if(systemdata.name.equals("CC")){
            staticClass.buffersize.get(1).add(systemdata.buffersize);
        }

        if(systemdata.name.equals("AP1")){
            staticClass.buffersize.get(2).add(systemdata.buffersize);
            staticClass.buffersize.get(1).add(CCserver.schedule.getSystemInformation().buffersize);
        }

        if(systemdata.name.equals("AP2")){
            staticClass.buffersize.get(3).add(systemdata.buffersize);
        }

        if(systemdata.name.equals("ED1")){
            staticClass.buffersize.get(4).add(systemdata.buffersize);
        }


        if(systemdata.name.equals("ED2")){
            staticClass.buffersize.get(5).add(systemdata.buffersize);
        }


        if(systemdata.name.equals("ED3")){
            staticClass.buffersize.get(6).add(systemdata.buffersize);
        }


        if(systemdata.name.equals("ED4")){
            staticClass.buffersize.get(7).add(systemdata.buffersize);
        }
        return false;
    }
}
