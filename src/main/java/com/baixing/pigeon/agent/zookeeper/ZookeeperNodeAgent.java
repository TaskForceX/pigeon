package com.baixing.pigeon.agent.zookeeper;

import com.baixing.pigeon.agent.entities.UTF8StringZData;
import com.baixing.pigeon.agent.entities.ZData;
import com.baixing.pigeon.agent.notifiers.Notifier;
import com.baixing.pigeon.agent.notifiers.StdNotifier;
import com.google.common.base.Preconditions;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.curator.utils.ZKPaths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by onesuper on 03/03/2017.
 */
public abstract class ZookeeperNodeAgent {
    private static Logger logger = LoggerFactory.getLogger(ZookeeperNodeAgent.class);

    private CuratorListener listener = new ZookeeperEventListener(this);
    private CuratorFramework client;
    private ZookeeperConfig zookeeperConfig;
    private String node;

    private List<String> children = new LinkedList<>();

    private ScheduledExecutorService scheduler;

    private Notifier notifier;

    public ZookeeperNodeAgent(ZookeeperConfig zookeeperConfig, String node) {
        this.zookeeperConfig = Preconditions.checkNotNull(zookeeperConfig);
        this.node = Preconditions.checkNotNull(node);
        this.notifier = new StdNotifier();
        init();
    }

    private void init() {
        logger.info("start watching changes under: {}", nodePath());
        client = CuratorFrameworkFactory.newClient(zookeeperConfig.getZkHost(), zookeeperConfig.getRetryPolicy());
        client.getCuratorListenable().addListener(listener);
        client.start();

        reload();

        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                logger.trace("consistency check for node: {}", node);
//                setChildren(getWatchedChildren());
            }
        }, 60000L, zookeeperConfig.getConsistencyCheckRate(), TimeUnit.MILLISECONDS);
    }

    public abstract void process(ZData data, Notifier notifier);
    public abstract void processOnChange(ZData data, Notifier notifier);
    public abstract void processOnCreate(ZData data, Notifier notifier);
    public abstract void processOnDelete(ZData data, Notifier notifier);

    public String nodePath() {
     return ZKPaths.makePath(zookeeperConfig.getRootNode(), node);
    }

    public void reload() {
        setChildren(getWatchedChildren());
    }

    public List<String> getWatchedChildren() {
        try {
            logger.info("watch node: {}", nodePath());

            List<String> children = client.getChildren().watched().forPath(nodePath());

            for (String child: children) {
                String childPath = ZKPaths.makePath(nodePath(), child);
                byte[] payload = client.getData().watched().forPath(childPath);

                ZData data = new UTF8StringZData();
                data.setPayload(payload);
                data.setPath(childPath);

                process(data, getNotifier());
            }

            return children;
        } catch (Exception ex) {
            logger.error("zookeeper error: {}", ex);
            return new LinkedList<>();
        }
    }



    public Notifier getNotifier() {
        return notifier;
    }

    public List<String> getChildren() {
        return children;
    }

    public void setChildren(final List<String> children) {
        this.children = children;
    }

    public void close() {
        if (scheduler != null) {
            scheduler.shutdown();
        }
        if (client != null) {
            logger.debug("remove node listener for node: {}", node);
            client.getCuratorListenable().removeListener(listener);
            client.close();
        }
    }
}