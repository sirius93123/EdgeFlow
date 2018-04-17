package controlModule;

import StaticModule.staticClass;
import Thrift.APserver;
import Thrift.CCserver;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import sun.rmi.runtime.Log;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * the function to schedule the computing , delivering function
 */
public class ScheduleModule implements ManagerInterface {

    double strategyPercentage = 0.5;

    int priority_computing = 0;
    int priority_delivering = 0;

    // task waiting queue
    final BlockingQueue<taskFile> cQueue = new LinkedBlockingQueue();
    final BlockingQueue<taskFile> dQueue = new LinkedBlockingQueue();

    // result queue
    final BlockingQueue<taskFile> rQueue = new LinkedBlockingQueue();


    ComputingInterface computingInterface;  // the interface for computing function
    DeliverInterface deliverInterface;      // the interface for delivering function
    generationInterface generationInterface;  // the interface for the generation function


    NodeConfig node;

    boolean state;

    int threadNumber = 3;

    /**
     * the init function
     * @param node
     * @param threadNumber
     */
    public ScheduleModule(NodeConfig node, int threadNumber) {
        this.node = node;
        this.threadNumber = threadNumber;
    }


    /**
     * get the data task from the computing queue
     * @return
     */
    taskFile getTaskFromComputingQueue(){
        try {
            return cQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * get the data task from the delivering queue
     * @return
     */
    taskFile getTaskFromDeliverQueue(){
        try {
            return dQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * insert the data task to the computing queue
     * @param temp
     */
    public void insertTaskFileIntoComputingQueue(taskFile temp){
        cQueue.add(temp);
    }

    /**
     * insert the data task to the delivering queue
     * @param temp
     */
    public void insertTaskFileIntoDeliverQueue(taskFile temp){
        dQueue.add(temp);
    }

    /**
     * at the CC layer,
     * insert the result to the result queue
     * and static the system state
     * @param temp
     */
    public void insertTaskFileIntoResultQueue(taskFile temp){
        rQueue.add(temp);
        staticClass.number = staticClass.number + 1;
        staticClass.latency = System.currentTimeMillis() - temp.generationTime + staticClass.latency;
        if(staticClass.number == 1){
            staticClass.startTime = temp.generationTime;
        }
        NodeConfig.debug("Number   "+staticClass.number);
        if(staticClass.number == 396){
            staticClass.endTime = System.currentTimeMillis();
            NodeConfig.debug("end Time  "+ (staticClass.endTime-staticClass.startTime));
            NodeConfig.debug("latency  "+ (staticClass.latency/396));
            int s1 = staticClass.buffersize.get(1).size();
            int s2 = staticClass.buffersize.get(2).size();
            int s3 = staticClass.buffersize.get(3).size();
            int s = s1 < s2 ?s1 : s2;
            s = s< s3?s:s3;
            int a = 0;
            int max = 0;
            for(int i  = 0 ; i < s; i ++){
                a = 0;
                a += staticClass.buffersize.get(1).get(i);
                a += staticClass.buffersize.get(2).get(i);
                a += staticClass.buffersize.get(3).get(i);
                a += staticClass.buffersize.get(4).get(i);
                a += staticClass.buffersize.get(5).get(i);
                a += staticClass.buffersize.get(6).get(i);
                a += staticClass.buffersize.get(7).get(i);
                if ( a > max){
                    max = a;
                }
            }
            NodeConfig.debug("buffersize  "+ max);
        }

    }



    String TAG = "EdgeFlow";

    Boolean Running = true;

    /**
     * judge the state for the  computing thread
     * @return
     */
    public boolean computingState(){
        if(!node.isComInterfaceLoad()){
            return false;
        }
        if(node.isApplicationState() == false && cQueue.size() == 0 && node.isApplicationStateFromDownLayers() == false){
            return false;
        }
        return true;
    }

    /**
     * judge the state for the delivering state
     * @return
     */
    public boolean deliverState(){
        if(!node.isDelInterfaceLoad()){
            return false;
        }
        if(node.isApplicationState() == false && cQueue.size() == 0 && dQueue.size() == 0 && node.isApplicationStateFromDownLayers() == false){
            return false;
        }

        return true;
    }

    // thread pool
    ExecutorService executorService = Executors.newFixedThreadPool(5);

    /**
     * the data generation thread
     */
    Runnable generationOrReceiving = new Runnable() {
        @Override
        public void run() {
            int num = 0;
            while(node.isApplicationState() && node.isGenInterfaceLoad()) {
                try {
                    Thread.sleep((long) (node.getGenerationDelta()));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                taskFile temp = generationInterface.generationData();
                temp.setGenerationTime(System.currentTimeMillis());
                if (InsertTransmissionBufferOrComputingBuffer()) {
                    insertTaskFileIntoComputingQueue(temp);
                } else {
                    insertTaskFileIntoDeliverQueue(temp);
                }
                num ++;
                if(num == 100){
                    break;
                }
            }
        }
    };

    /**
     * the computing thread
     */
    Runnable computing = new Runnable() {
        @Override
        public void run() {

            while(computingState()){
                taskFile computeTask = getTaskFromComputingQueue();
                computingInterface.computingFunction(computeTask);
                computeTask.setTaskState(true);
                if(node.getLayer() == 1){
                    insertTaskFileIntoResultQueue(computeTask);
                }else {
                    insertTaskFileIntoDeliverQueue(computeTask);
                }
                NodeConfig.debug("compute one task");
            }
            // Log.e(TAG, Thread.currentThread().getName());
        }
    };

    /**
     * get the system state including the buffersize, computing speed , transmission speed
     * @return
     */
    public SystemState getSystemInformation(){
        SystemState temp = new SystemState();
        temp.name = node.nodeName;
        temp.buffersize = cQueue.size() + dQueue.size();
        temp.computingSpeed = 0;
        temp.transmissionSpeed = 0;
        return temp;
    }

    /**
     * the thread for update the system state to the CC server
     */
    Runnable updateMessage = new Runnable() {
        @Override
        public void run() {

            while(computingState()){
                try {
                    Thread.sleep(1000);
                    deliverInterface.deliverSystem(getSystemInformation(),node.getCCIp(), node.getCCport());
                    //testSystemInfo(node.getCCIp(), node.getCCport(),getSystemInformation());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    };

    /**
     * the thread to delivering the task data
     */
    Runnable delivering = new Runnable() {
        @Override
        public void run() {
            NodeConfig.debug("start deliver3");
            while(deliverState()){
                NodeConfig.debug("deliver start");
                NodeConfig.debug(dQueue.size());
                taskFile deliverTask = getTaskFromDeliverQueue();
                deliverInterface.deliverTaskFile(deliverTask, node.getUplayerIp(), node.getUplayerPort());
            }

        }
    };

    /**
     * the function to load the computing, delivering, generation interface
     */
    void testLoadInterface(){
        computingInterface = new demoComputingModule(node);
        node.setComInterfaceLoad(true);
        if(node.getLayer() == 3){
            generationInterface = new demoGenerationModule(node);
            node.setGenInterfaceLoad(true);
        }
        if(node.getLayer() > 1){
            // load the deliver interface
            demoDeliverModule t1 =  new demoDeliverModule();
            t1.initFile(node.getUplayerIp(),node.getUplayerPort());
            if(node.getLayer() == 3) {
                t1.initServer(node.getCCIp(), node.getCCport());
            }else{
                t1.initServerAP(node.getCCIp(), node.getCCport());
            }
            deliverInterface = t1;
            node.setDelInterfaceLoad(true);
        }
        node.setApplicationState(true);
       // node.setGenInterfaceLoad(true);
    }

    /**
     * the jar load function for the computing , delivering and generating interface
     * if there are other implementation function from the jar
     */
    void loadJar() {


        /**
         * test function
         */
        if(loadTestJar){
           testLoadInterface();
            return;
        }


        computingInterface = JarHelper.loadComputeInterface(node.getComputingPath(), node.getComputingClass());
        if(node.getLayer() == 3) {
            generationInterface = JarHelper.loadGenerationInterface(node.getGenerationPath(), node.getGenerationClass());
        }

        if(node.getLayer() < 3){
            // load the deliver interface
            deliverInterface = JarHelper.loadDeliverInterface(node.getGenerationPath(), node.getDeliverClass());
        }

    }

    private APserver apServer;
    private CCserver ccServer;

    boolean loadTestJar = true;
    void initFileServer(){
        switch (node.getLayer()){
            case 1:
                ccServer = new CCserver(node, this);
                ccServer.initServer(node.getFilePort());
                break;
            case 2:
                apServer = new APserver(node, this);
                apServer.initServer(node.getFilePort());
                break;
        }
    }

    /**
     *  init the interface for the receiving, computing, delivering thread
     */
    public void initConfig(){
        // load task jar
        loadJar();
        if(node.getLayer() == 3) {
            executorService.execute(generationOrReceiving);
            NodeConfig.debug("start generation thread");
        }

        executorService.execute(computing);

        if(node.getLayer() != 1) {
            executorService.execute(delivering);
            executorService.execute(updateMessage);
        }
        initFileServer();


    }

    double step = 0;
    double transmitFlag = 0;

    /**
     * with the transmit flag
     * to achieve the data schedule algorithm
     * judge the data to delivering queue or the computing queue
     * @return
     */
    @Override
    public boolean InsertTransmissionBufferOrComputingBuffer() {

        transmitFlag = transmitFlag + step;
        if(transmitFlag >= 1){
            transmitFlag = transmitFlag - 1;
            return false;
        }

        return true;
    }


    /**
     * judge the queue is Block or Not
     * @return
     */
    @Override
    public boolean JudgeNodeBlockState() {

        if (dQueue.size() + cQueue.size() >= 5){
            return true;
        }
        return false;
    }


}
