package controlModule;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * the function for load the jar function
 */
public class JarHelper {
    /**
     * the function to load other computing implementation
     * @param dir
     * @param cname
     * @return
     */
    public static ComputingInterface loadComputeInterface(String dir, String cname){
        String jarName = "file:"+dir;
        System.out.println(jarName);
        try{
            File file = new File(jarName);
            URL urls[] = new URL[] {new URL(jarName)};
            URLClassLoader loader = new URLClassLoader(urls);
            Class aClass = loader.loadClass(cname);
            ComputingInterface t1 = (ComputingInterface)aClass.newInstance();
            return t1;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * the function to load the other generation function
     * @param dir
     * @param cname
     * @return
     */
    public static generationInterface loadGenerationInterface(String dir, String cname){
        String jarName = "file:"+dir;
        System.out.println(jarName);
        try{
            File file = new File(jarName);
            URL urls[] = new URL[] {new URL(jarName)};
            URLClassLoader loader = new URLClassLoader(urls);
            Class aClass = loader.loadClass(cname);
            generationInterface t1 = (generationInterface)aClass.newInstance();
            return t1;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * the function to load other deliver implementation
     * @param dir
     * @param cname
     * @return
     */
    public static DeliverInterface loadDeliverInterface(String dir, String cname){
        String jarName = "file:"+dir;
        System.out.println(jarName);
        try{
            File file = new File(jarName);
            URL urls[] = new URL[] {new URL(jarName)};
            URLClassLoader loader = new URLClassLoader(urls);
            Class aClass = loader.loadClass(cname);
            DeliverInterface t1 = (DeliverInterface)aClass.newInstance();
            return t1;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


}
