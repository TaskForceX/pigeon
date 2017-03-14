package com.baixing.pigeon.agent.zookeeper;

import com.google.common.base.Preconditions;
import org.apache.curator.RetryPolicy;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * Created by onesuper on 03/03/2017.
 */
public class ZookeeperConfig {

    private static final ExponentialBackoffRetry DEFAULT_RETRY_POLICY = new ExponentialBackoffRetry(1000, 3);

    private final String zkHost;
    private final String rootNode;
    private final RetryPolicy retryPolicy;
    private long consistencyCheckRate = 60 * 1000;

    public ZookeeperConfig(final String zkHost, final String rootNode) {
        this(zkHost, rootNode, DEFAULT_RETRY_POLICY);
    }

    public ZookeeperConfig(final String zkHost, final String rootNode, final RetryPolicy retryPolicy) {
        this.zkHost = Preconditions.checkNotNull(zkHost);
        this.rootNode = Preconditions.checkNotNull(rootNode);
        this.retryPolicy = Preconditions.checkNotNull(retryPolicy);
    }

    public String getZkHost() {
        return zkHost;
    }

    public String getRootNode() {
        return rootNode;
    }

    public RetryPolicy getRetryPolicy() {
        return retryPolicy;
    }

    public final long getConsistencyCheckRate() {
        return consistencyCheckRate;
    }

    public final void setConsistencyCheckRate(long consistencyCheckRate) {
        this.consistencyCheckRate = consistencyCheckRate;
    }

    @Override
    public String toString() {
        return "ZookeeperConfig [zkHost=" + zkHost + ", rootNode=" + rootNode + ", retryPolicy=" + retryPolicy
                + ", consistencyCheckRate=" + consistencyCheckRate + "]";
    }
}
