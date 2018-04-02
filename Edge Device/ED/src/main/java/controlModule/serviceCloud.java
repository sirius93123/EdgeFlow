package controlModule;

import org.apache.thrift.TException;

public class serviceCloud implements ThriftService.Iface {
    @Override
    public int add(int a, int b) throws TException {
        return 1234;
    }
}
