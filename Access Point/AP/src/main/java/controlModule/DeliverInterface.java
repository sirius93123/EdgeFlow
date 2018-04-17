package controlModule;

/**
 * delivering module interface for how to upload the data to the uplay
 */
public interface DeliverInterface {
    /**
     * the delivering interface for how to upload the taskFile to Server at (IP, port)
     * @param temp
     * @param ip
     * @param port
     */
    void deliverTaskFile(taskFile temp, String ip, int port);

    /**
     * the delivering interface for how to upload the System State to Server at (IP, port)
     * @param temp
     * @param ip
     * @param port
     */
    void deliverSystem(SystemState temp, String ip, int port);
}
