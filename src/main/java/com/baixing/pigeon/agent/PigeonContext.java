package com.baixing.pigeon.agent;

import com.baixing.pigeon.agent.zookeeper.ZookeeperConfig;
import com.baixing.pigeon.agent.zookeeper.ZookeeperNodeAgent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by onesuper on 03/03/2017.
 */
public class PigeonContext implements Runnable {

    private static Logger logger = LoggerFactory.getLogger(PigeonContext.class);

    private ZookeeperNodeAgent watcher;

    public void run() {
        try {
            start();
        } catch (Exception e) {
            logger.error("Pigeon encountered an exception", e);
        }
    }

    public void terminate() {
        logger.info("shutdown pigeon");
        watcher.close();
    }

    public void start() throws Exception {
        logger.info("start pigeon");
        ZookeeperConfig zookeeperConfig =new ZookeeperConfig("storm01:2181,storm02:2181", "/pigeon");
        logger.info("zookeeper config: {}", zookeeperConfig);

        watcher = new ZookeeperNodeAgent(zookeeperConfig, "/");
    }

}
