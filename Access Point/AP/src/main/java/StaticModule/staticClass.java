package StaticModule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * the static class
 * to estimate the task lantency , buffer size in the waiting buffers, and the application finish time
 */
public class staticClass {

    public static long latency = 0;
    public static int number;
    public static long startTime = 0;
    public static long endTime = 0;
    public static Map<Integer, ArrayList<Integer>> buffersize = new HashMap<Integer, ArrayList<Integer>>();

    public static void initstaticClass(){
        buffersize.put(1, new ArrayList<Integer>());
        buffersize.put(2, new ArrayList<Integer>());
        buffersize.put(3, new ArrayList<Integer>());
        buffersize.put(4, new ArrayList<Integer>());
        buffersize.put(5, new ArrayList<Integer>());
        buffersize.put(6, new ArrayList<Integer>());
        buffersize.put(7, new ArrayList<Integer>());
        startTime = System.currentTimeMillis();
        number = 0;
    }

}
