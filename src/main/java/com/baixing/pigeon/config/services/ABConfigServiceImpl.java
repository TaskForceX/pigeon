package com.baixing.pigeon.config.services;

import com.baixing.pigeon.agent.zookeeper.ZookeeperConfig;
import com.baixing.pigeon.config.entities.ABConfig;
import com.baixing.pigeon.config.InvalidABConfigException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.utils.ZKPaths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Created by onesuper on 13/03/2017.
 */
@Service
public class ABConfigServiceImpl implements ABConfigService {
    private static final Logger logger = LoggerFactory.getLogger(ABConfigServiceImpl.class);
    private CuratorFramework curator;
    private static String ZK_HOME = "/pigeon";

    @Value("${zookeeper.host}")
    String zookeeperHost;

    @PostConstruct
    public void initCurator() throws Exception {
        ZookeeperConfig zookeeperConfig = new ZookeeperConfig(zookeeperHost, ZK_HOME);
        logger.info("curator started at host: " + zookeeperHost);
        curator = CuratorFrameworkFactory.newClient(zookeeperConfig.getZkHost(), zookeeperConfig.getRetryPolicy());
        curator.start();
    }

    @Override
    public List<String> listAllConfigIds() {
        try {
            return curator.getChildren().forPath(ZK_HOME);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw new ABConfigServiceException(ex.getMessage());
        }
    }

    @Override
    public ABConfig getConfigById(String id) {

        String path = ZKPaths.makePath(ZK_HOME, id);
        ObjectMapper mapper = new ObjectMapper();

        try {
            if (curator.checkExists().forPath(path) == null) {
                logger.info("path not exists {}", path);
                throw new ABConfigServiceException("path not exists: " + path);
            }

            return mapper.readValue(new String(curator.getData().forPath(path), Charsets.UTF_8), ABConfig.class);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw new ABConfigServiceException(ex.getMessage());
        }
    }

    @Override
    public ABConfig saveConfig(ABConfig config) {
        try {
            try {
                config.validate();
            } catch (InvalidABConfigException ex) {
                logger.info("deny invalid config due to " + ex.getMessage() + ": " + config.serializeToPrettyString());
                throw new ABConfigServiceException("deny invalid config due to " + ex.getMessage() + ": " + config.serializeToPrettyString());
            }

            String path = ZKPaths.makePath(ZK_HOME, config.getId());

            ObjectMapper mapper = new ObjectMapper();
            String payload = mapper.writeValueAsString(config);

            if (curator.checkExists().forPath(path) == null) {
                curator.create().forPath(path, payload.getBytes());
            }
            curator.setData().forPath(path, payload.getBytes());
            logger.info("update config {} => {}", path, config);
            return config;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw new ABConfigServiceException(ex.getMessage());
        }
    }

    @Override
    public void deleteConfig(String name) {
        String path = ZKPaths.makePath(ZK_HOME, name);

        try {
            if (curator.checkExists().forPath(path) == null) {
                logger.info("path not exists {} {}", path);
                throw new ABConfigServiceException("path not exists: " + path);
            }
            curator.delete().guaranteed().forPath(path);
            logger.info("delete {}", path);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw new ABConfigServiceException(ex.getMessage());
        }
    }
}
