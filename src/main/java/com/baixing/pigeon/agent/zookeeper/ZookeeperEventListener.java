package com.baixing.pigeon.agent.zookeeper;

import com.baixing.pigeon.agent.entities.UTF8StringZData;
import com.baixing.pigeon.agent.entities.ZData;
import com.google.common.collect.Sets;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.curator.utils.ZKPaths;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
                String path;
                switch (watchedEvent.getType()) {

                    case NodeCreated:
                        break;
                    case NodeDeleted:
                        break;
                    case NodeChildrenChanged:
                        path = event.getPath();

                        List<String> children = nodeWatcher.getChildren();
                        List<String> watchedChildren = nodeWatcher.getWatchedChildren();

                        Set<String> oldChildren = new HashSet<>(children);
                        Set<String> newChildren = new HashSet<>(watchedChildren);

                        Sets.SetView<String> created = Sets.difference(newChildren, oldChildren);
                        Sets.SetView<String> deleted = Sets.difference(oldChildren, newChildren);

                        logger.debug("Created: {}, {}", path, created);
                        for (String child : created) {
                            String childPath =ZKPaths.makePath(nodeWatcher.nodePath(), child);
                            ZData data = new UTF8StringZData();
                            data.setPayload(client.getData().watched().forPath(childPath));
                            data.setPath(childPath);

                            nodeWatcher.processOnCreate(data, nodeWatcher.getNotifier());
                        }

                        logger.debug("Deleted: {}, {}", path, deleted);
                        for (String child : deleted) {
                            String childPath =ZKPaths.makePath(nodeWatcher.nodePath(), child);

                            ZData data = new UTF8StringZData();
                            data.setPath(childPath);

                            nodeWatcher.processOnDelete(data, nodeWatcher.getNotifier());
                        }

                        nodeWatcher.setChildren(watchedChildren);
                        break;
                    case NodeDataChanged:
                        path = watchedEvent.getPath();
                        ZData data = new UTF8StringZData();
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