package com.baixing.pigeon.agent;

import com.baixing.pigeon.agent.notifiers.Event;
import com.baixing.pigeon.agent.notifiers.Notifier;
import com.baixing.pigeon.agent.zookeeper.UTF8StringZookeeperData;
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
    public void process(UTF8StringZookeeperData data, Notifier notifier) {
        logger.debug("processData: {}", data.getAsString());

        try {
            ABConfig config = new ABConfig();
            config.parseFromString(data.getAsString());

            if (config.tryWork(currentTime())) {
                persistConfig(data.getPath(), config);
                notifier.notify(Event.create(data));
            }

            if (config.tryExpire(currentTime())) {
                persistConfig(data.getPath(), config);
                notifier.notify(Event.delete(data));
            }

        } catch (Exception ex) {
            throw new ABConfigAgentException(ex);
        }
    }

    @Override
    public void processOnDelete(UTF8StringZookeeperData data, Notifier notifier) {
        logger.debug("processDataOnDelete: {}", data.getAsString());
        notifier.notify(Event.delete(data));
    }

    @Override
    public void processOnChange(UTF8StringZookeeperData data, Notifier notifier) {
        logger.debug("processDataOnChange: {}", data.getAsString());

        try {
            ABConfig config = new ABConfig();
            config.parseFromString(data.getAsString());

            if (config.tryWork(System.currentTimeMillis())) {
                persistConfig(data.getPath(), config);
                notifier.notify(Event.create(data));
            }

        } catch (Exception ex) {
            throw new ABConfigAgentException(ex);
        }
    }

    private void persistConfig(String path, ABConfig config) throws Exception {
        UTF8StringZookeeperData newData = new UTF8StringZookeeperData();
        newData.setPath(path);
        newData.setUTF8String(config.serializeToString());
        persistZookeeperData(newData);
    }

    private long currentTime() {
        return System.currentTimeMillis() / 1000;
    }
}
