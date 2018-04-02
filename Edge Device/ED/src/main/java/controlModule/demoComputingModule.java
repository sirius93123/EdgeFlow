package controlModule;

public class demoComputingModule implements ComputingInterface {
    @Override
    public void computingFunction(taskFile temp) {
        try {
            Thread.sleep(10000);
            computingTaskFileImpl(temp );
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void computingTaskFileImpl(taskFile temp){
        System.out.println("task finish");
    }
}
