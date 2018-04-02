package controlModule;

public class NodeConfig {

    /**
     *  layer 1: edge Device
     *  layer 2: Access Point
     *  layer 3: Cloud Center
     */
    private int layer;

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

    int FilePort;

    public void setFilePort(int port){
        this.FilePort = port;
    }

    public int getFilePort(){
        return this.FilePort;
    }




}
