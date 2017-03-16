package com.baixing.pigeon.agent.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by onesuper on 03/03/2017.
 */
public final class ZookeeperEventListener implements CuratorListener {

    private static final Logger logger = LoggerFactory.getLogger(ZookeeperEventListener.class);

    private ZookeeperNodeAgent nodeWatcher;

    public ZookeeperEventListener(ZookeeperNodeAgent nodeWatcher) {
        this.nodeWatcher = nodeWatcher;
    }

    @Override
    public void eventReceived(CuratorFramework client, CuratorEvent event) throws Exception {

        final WatchedEvent watchedEvent = event.getWatchedEvent();
        if (watchedEvent != null) {

            if (logger.isDebugEnabled()) {
                logger.debug("{}", watchedEvent);
            }

            if (watchedEvent.getState() == Watcher.Event.KeeperState.SyncConnected) {
                String path = watchedEvent.getPath();
                UTF8StringZookeeperData data = new UTF8StringZookeeperData();
                switch (watchedEvent.getType()) {

                    case NodeDeleted:
                        path = watchedEvent.getPath();
                        data.setPath(path);
                        nodeWatcher.processOnDelete(data, nodeWatcher.getNotifier());
                        break;
                    case NodeChildrenChanged:
                        // any change to the children will cause the reload
                        nodeWatcher.rewatch();
                        break;
                    case NodeDataChanged:
                        data.setPayload(client.getData().watched().forPath(path));
                        data.setPath(path);
                        nodeWatcher.processOnChange(data, nodeWatcher.getNotifier());
                        break;
                    default:
                        break;
                }
            }
        }
    }

}