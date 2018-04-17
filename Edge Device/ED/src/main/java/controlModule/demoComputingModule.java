package controlModule;

import utis.FileCopy;

import java.io.File;

/**
 * the demo computation implementation
 */
public class demoComputingModule implements ComputingInterface {

    NodeConfig node;  // the link of config class

    /**
     * the initiation function for the node Config
     * @param node
     */
    public demoComputingModule(NodeConfig node) {
        this.node = node;
    }

    /**
     *
     * @param nodeConfig
     */
    void initNodeConfig(NodeConfig nodeConfig){
        this.node = nodeConfig;
    }

    /**
     * the computation function implementation for the task data (temp)
     * @param temp
     */

    @Override
    public void computingFunction(taskFile temp) {
        /**
         *   load your computation function
         */
    }

}
