package controlModule;

import sun.rmi.runtime.Log;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class ScheduleModule implements ManagerInterface {

    double strategyPercentage = 0.5;

    int priority_computing = 0;
    int priority_delivering = 0;

    boolean computing_thread_state = false;
    boolean delivering_thread_state = false;

    // task waiting queue
    final BlockingQueue<taskFile> cQueue = new LinkedBlockingQueue();
    final BlockingQueue<taskFile> dQueue = new LinkedBlockingQueue();

    // result queue
    final BlockingQueue<taskFile> rQueue = new LinkedBlockingQueue();
    ComputingInterface computingInterface;
    DeliverInterface deliverInterface;
    GenerationAndReceivingInterface generationAndReceivingInterface;;


    NodeConfig node;

    boolean state;

    int threadNumber = 3;

    int getComputingThreadPriority(){
        return 0;
    }

    int getDeliveringThreadPriority(){
        return 0;
    }



    taskFile getTaskFromComputingQueue(){
        try {
            return cQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    void updatePriority(){
        priority_computing = (int) (priority_computing + 100 * strategyPercentage);
        priority_delivering = (int) (priority_delivering + 100*(1-strategyPercentage));
    }


    taskFile getTaskFromDeliverQueue(){
        try {
            return dQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    void insertTaskFileIntoComputingQueue(taskFile temp){
        cQueue.add(temp);
    }

    void insertTaskFileIntoDeliverQueue(taskFile temp){
        dQueue.add(temp);
    }

    void insertTaskFileIntoResultQueue(taskFile temp){
        rQueue.add(temp);
    }



    String TAG = "EdgeFlow";

    Boolean Running = true;

    // thread pool
    ExecutorService executorService = Executors.newFixedThreadPool(threadNumber);

    Runnable generationOrReceiving = new Runnable() {
        @Override
        public void run() {
            generationAndReceivingInterface.generationOrReceivingModule();
            // Log.e(TAG, Thread.currentThread().getName());

        }
    };

    Runnable computing = new Runnable() {
        @Override
        public void run() {

            while(Running){
                taskFile computeTask = getTaskFromComputingQueue();
                computingInterface.computingFunction(computeTask);
                computeTask.setTaskState(true);
            }
            // Log.e(TAG, Thread.currentThread().getName());
        }
    };

    Runnable delivering = new Runnable() {
        @Override
        public void run() {
            // Log.e(TAG, Thread.currentThread().getName());
            while(Running){
                taskFile deliverTask = getTaskFromDeliverQueue();
                deliverInterface.deliverTaskFile(deliverTask);
            }
        }
    };

    void loadComputeJar() {
    }
    /**
     *  init the interface for the receiving, computing, delivering thread
     */
    public void initConfig(){
        loadComputeJar();
        computingInterface = new demoComputingModule();
        executorService.execute(generationOrReceiving);
        executorService.execute(computing);
        executorService.execute(delivering);
    }

    // first computing buffer

    // transmission buffer get from the compting buffer
    @Override
    public boolean InsertTransmissionBufferOrComputingBuffer() {

        if(JudgeNodeBlockState()){
            uploadBlockingMessage();
            BlockingStateStrategy();
        }else{
            NoBlockingStateStategy();
        }



        return false;
    }

    void BlockingStateStrategy(){

    }

    void NoBlockingStateStategy(){

    }

    void uploadBlockingMessage(){

    }

    void uploadNodeMessage(){

    }



    // buffer exceed three
    // align block start
    @Override
    public boolean JudgeNodeBlockState() {

        if (dQueue.size() + cQueue.size() >= 5){
            return true;
        }
        return false;
    }


    // three work thread

    // thread for receive and generation

    // thread for computing

    // thread for

    // dQueue get the transport file from the cQueue

}
