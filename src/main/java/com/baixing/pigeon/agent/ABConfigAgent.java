package com.baixing.pigeon.agent;

import com.baixing.pigeon.agent.entities.UTF8StringZData;
import com.baixing.pigeon.agent.notifiers.Notifier;
import com.baixing.pigeon.agent.zookeeper.ZookeeperConfig;
import com.baixing.pigeon.agent.zookeeper.ZookeeperNodeAgent;
import com.baixing.pigeon.config.entities.ABConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by onesuper on 16/03/2017.
 */
public class ABConfigAgent extends ZookeeperNodeAgent {
    private static Logger logger = LoggerFactory.getLogger(ABConfigAgent.class);

    public ABConfigAgent(ZookeeperConfig zookeeperConfig, String node) {
        super(zookeeperConfig, node);
    }

    @Override
    public void process(UTF8StringZData data, Notifier notifier) {
        logger.info("processData: {}", data.getAsString());

        try {
            ABConfig config = new ABConfig();
            config.parseFromString(data.getAsString());

            if (config.tryWork(currentTime())) {
            }

            if (config.tryExpire(currentTime())) {
            }

        } catch (Exception ex) {
            throw new ABConfigAgentException(ex);
        }
    }

    @Override
    public void processOnDelete(UTF8StringZData data, Notifier notifier) {
        logger.info("processDataOnDelete: {}", data.getAsString());
    }

    @Override
    public void processOnChange(UTF8StringZData data, Notifier notifier) {
        logger.info("processDataOnChange: {}", data.getAsString());

        try {
            ABConfig config = new ABConfig();
            config.parseFromString(data.getAsString());

            if (config.tryWork(System.currentTimeMillis())) {
            }

        } catch (Exception ex) {
            throw new ABConfigAgentException(ex);
        }
    }

    private long currentTime() {
        return System.currentTimeMillis() / 1000;
    }
}
