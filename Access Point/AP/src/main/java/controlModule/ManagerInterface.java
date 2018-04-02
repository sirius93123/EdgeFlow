package controlModule;

public interface ManagerInterface {

    /**
     * interface for the computing schedule
     * @return
     */
    boolean InsertTransmissionBufferOrComputingBuffer();

    boolean JudgeNodeBlockState();

}
