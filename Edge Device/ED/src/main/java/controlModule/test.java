package controlModule;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * API test function
 */
public class test {

    public void load(String dir,String cname){
        String jarName = "file:"+dir;
        System.out.println(jarName);
        try{
            File file = new File(jarName);
            URL urls[] = new URL[] {new URL(jarName)};
            URLClassLoader loader = new URLClassLoader(urls);
            Class aClass = loader.loadClass(cname);
            ComputingInterface t1 = (ComputingInterface)aClass.newInstance();
            taskFile t4 = null;
            //taskFile t4 = new taskFile();
            t1.computingFunction(t4);
            //
            //t1.computingFunction(t4);
            //利用Java反射机制创建实例测试方法



        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void testExecution(){
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        Runnable generationOrReceiving = new Runnable() {
            @Override
            public void run() {
                System.out.println("123");
                // Log.e(TAG, Thread.currentThread().getName());

            }
        };

        Runnable computing = new Runnable() {
            @Override
            public void run() {

                System.out.println("234");
            }
        };

        Runnable delivering = new Runnable() {
            @Override
            public void run() {
                System.out.println("345");
            }
        };

        executorService.execute(generationOrReceiving);
        executorService.execute(computing);
        executorService.execute(delivering);

    }

    public void testExecution2(){
        testExecution();
    }

    public void testTaskState(taskFile temp){
        temp.setTaskState(false);
    }

    public void testTaskState(){
        taskFile t4 = null;
        // taskFile t4 = new taskFile();
        testTaskState(t4);
        System.out.println(t4.taskState);
    }

    public static void main(String[] args){
        test t1 = new test();
        t1.load("/Users/yaochao/Downloads/chaoyao/out/artifacts/123/123.jar", "rCompute");

        t1.testExecution2();

        t1.testTaskState();
    }
}
