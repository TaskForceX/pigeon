package com.baixing.pigeon.agent.zookeeper;

import com.baixing.pigeon.agent.notifiers.Notifier;
import com.baixing.pigeon.agent.notifiers.StdNotifier;
import com.google.common.base.Preconditions;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.curator.utils.ZKPaths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
                logger.trace("reload node: {}", node);
                reload();
            }
        }, 60000L, zookeeperConfig.getConsistencyCheckRate(), TimeUnit.MILLISECONDS);
    }

    public abstract void process(UTF8StringZookeeperData data, Notifier notifier);
    public abstract void processOnChange(UTF8StringZookeeperData data, Notifier notifier);
    public abstract void processOnDelete(UTF8StringZookeeperData data, Notifier notifier);

    public String nodePath() {
     return ZKPaths.makePath(zookeeperConfig.getRootNode(), node);
    }

    public void reload() {
        try {
            List<String> children = client.getChildren().watched().forPath(nodePath());

            for (String child: children) {
                String childPath = ZKPaths.makePath(nodePath(), child);
                byte[] payload = client.getData().watched().forPath(childPath);

                UTF8StringZookeeperData data = new UTF8StringZookeeperData();
                data.setPayload(payload);
                data.setPath(childPath);

                process(data, getNotifier());
            }

        } catch (Exception ex) {
            logger.error("zookeeper error: {}", ex);
        }
    }

    public void rewatch() {
        try {
            client.getChildren().watched().forPath(nodePath());
        } catch (Exception ex) {
            logger.error("zookeeper error: {}", ex);
        }
    }

    public Notifier getNotifier() {
        return notifier;
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

    public void persistZookeeperData(ZookeeperData data) {
        try {
            client.setData().forPath(data.getPath(), data.getPayload());
        } catch (Exception ex) {
            logger.error("zookeeper error: {}", ex);
        }
    }
}