package controlModule;

import utis.FileCopy;

/**
 * the demo implementation to generate data
 */
public class demoGenerationModule implements generationInterface {
    int w = 0;
    NodeConfig node;
    public demoGenerationModule(NodeConfig temp) {
        this.node = temp;
    }

    /**
     * how to generate the task data
     * @return
     */
    @Override
    public taskFile generationData() {
        /**
         * achieve the generation function
         */
        return null;
    }
}
