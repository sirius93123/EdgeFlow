package controlModule;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * the config class for the node
 */
public class NodeConfig {
    private int layer = 3; // the layer number : CC: 1, AP:2, ED:3
    int ThreadNumber = 3;  // system thread number
    int FilePort;          // the port for file server
    public String rootStorePath; // the root path to store the data cache
    long computeSpeed;           // the simulation computing speed which can control the speed of the computing function
    public String UplayerIp;     // the ip for the uplayer file server
    public int UplayerPort;      // the port for the uplayer file server

    public String CCIp;          // the IP for the system state server
    public int CCport;           // the port for the system state server
    public String nodeName;      // the node name
    public boolean testMode = false;

    public String computingPath;  // the path for the computation jar

    public String computingClass;
    public String deliverClass;

    public double  generationDelta; // the time delta for the data generation

    int fileServerPort = 8000;

    private boolean ComInterfaceLoad =false;  // the computing interface load state
    private boolean DelInterfaceLoad =false;  // the delivering interface load state
    private boolean genInterfaceLoad =false;  // the generating interface load state

    public String generationPath;   // the jar path of the data generation function
    public String generationClass;  // the name of the data generation function

    public boolean ApplicationState;  // the state of the application

    public boolean ApplicationStateFromDownLayers;  // the state of the down layer

    public static Logger logger = LogManager.getLogger(NodeConfig.class);

    public static void debug(Object message){
        logger.debug(message);
    }


    public int getThreadNumber() {
        return ThreadNumber;
    }

    public void setThreadNumber(int threadNumber) {
        ThreadNumber = threadNumber;
    }



    public String getRootStorePath() {
        return rootStorePath;
    }

    public void setRootStorePath(String rootStorePath) {
        this.rootStorePath = rootStorePath;
    }




    /**
     * API to set the layer of the node
     * @param layer
     */
    public void setLayer(int layer){
        this.layer = layer;
    }


    /**
     * API to get the node layer
     * @return
     */
    public int getLayer(){
        return this.layer;
    }



    public long getComputeSpeed() {
        return computeSpeed;
    }

    public void setComputeSpeed(long computeSpeed) {
        this.computeSpeed = computeSpeed;
    }



    public String getUplayerIp() {
        return UplayerIp;
    }

    public void setUplayerIp(String uplayerIp) {
        UplayerIp = uplayerIp;
    }

    public int getUplayerPort() {
        return UplayerPort;
    }

    public void setUplayerPort(int uplayerPort) {
        UplayerPort = uplayerPort;
    }

    public String getCCIp() {
        return CCIp;
    }

    public void setCCIp(String CCIp) {
        this.CCIp = CCIp;
    }

    public int getCCport() {
        return CCport;
    }

    public void setCCport(int CCport) {
        this.CCport = CCport;
    }



    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setFilePort(int port){
        this.FilePort = port;
    }

    public int getFilePort(){
        return this.FilePort;
    }

    public String getComputingPath() {
        return computingPath;
    }


    public String getComputingClass() {
        return computingClass;
    }

    public String getDeliverClass() {
        return deliverClass;
    }

    public String getGenerationClass() {
        return generationClass;
    }


    public void setGenerationClass(String generationClass) {
        this.generationClass = generationClass;
    }

    public int getFileServerPort() {
        return fileServerPort;
    }

    public void setFileServerPort(int fileServerPort) {
        this.fileServerPort = fileServerPort;
    }



    public NodeConfig(boolean testMode, int layer, int threadNumber, String rootStorePath, String uplayerIp, int uplayerPort, String CCIp, int CCport, double generationDelta) {
        this.testMode = testMode;
        this.layer = layer;
        ThreadNumber = threadNumber;
        this.rootStorePath = rootStorePath;
        UplayerIp = uplayerIp;
        UplayerPort = uplayerPort;
        this.CCIp = CCIp;
        this.CCport = CCport;
        this.generationDelta = generationDelta;
    }

    public NodeConfig(int layer, int filePort, String computingPath, String computingClass, String deliverClass, String generationClass) {
        this.layer = layer;
        FilePort = filePort;
        this.computingPath = computingPath;
        this.computingClass = computingClass;
        this.deliverClass = deliverClass;
        this.generationClass = generationClass;

        if(layer == 3){
            this.ApplicationState = false;
            this.ApplicationStateFromDownLayers = false;
        }
    }

    public void setComputingPath(String computingPath) {

        this.computingPath = computingPath;
    }

    public void setComputingClass(String computingClass) {
        this.computingClass = computingClass;
    }

    public void setDeliverClass(String deliverClass) {
        this.deliverClass = deliverClass;
    }



    public String getGenerationPath() {
        return generationPath;
    }

    public void setGenerationPath(String generationPath) {
        this.generationPath = generationPath;
    }

    public static NodeConfig loadXmlForConfig(String path){
        return null;
    }

    public boolean isApplicationState() {
        return ApplicationState;
    }

    public void setApplicationState(boolean applicationState) {
        ApplicationState = applicationState;
    }

    public boolean isComInterfaceLoad() {
        return ComInterfaceLoad;
    }

    public void setComInterfaceLoad(boolean comInterfaceLoad) {
        ComInterfaceLoad = comInterfaceLoad;
    }

    public boolean isDelInterfaceLoad() {
        return DelInterfaceLoad;
    }

    public void setDelInterfaceLoad(boolean delInterfaceLoad) {
        DelInterfaceLoad = delInterfaceLoad;
    }

    public boolean isGenInterfaceLoad() {
        return genInterfaceLoad;
    }

    public void setGenInterfaceLoad(boolean genInterfaceLoad) {
        this.genInterfaceLoad = genInterfaceLoad;
    }



    public boolean isApplicationStateFromDownLayers() {
        return ApplicationStateFromDownLayers;
    }

    public void setApplicationStateFromDownLayers(boolean applicationStateFromDownLayers) {
        ApplicationStateFromDownLayers = applicationStateFromDownLayers;
    }

    public double getGenerationDelta() {
        return generationDelta;
    }

    public void setGenerationDelta(double generationDelta) {
        this.generationDelta = generationDelta;
    }


}
