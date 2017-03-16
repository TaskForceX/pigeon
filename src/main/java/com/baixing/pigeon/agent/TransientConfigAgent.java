package com.baixing.pigeon.agent;

import com.baixing.pigeon.agent.entities.ZData;
import com.baixing.pigeon.agent.notifiers.Notifier;
import com.baixing.pigeon.agent.zookeeper.ZookeeperConfig;
import com.baixing.pigeon.agent.zookeeper.ZookeeperNodeAgent;

/**
 * Created by onesuper on 16/03/2017.
 */
public class TransientConfigAgent extends ZookeeperNodeAgent {

    public TransientConfigAgent(ZookeeperConfig zookeeperConfig, String node) {
        super(zookeeperConfig, node);
    }

    @Override
    public void process(ZData data, Notifier notifier) {
        System.out.println("in processData");
        System.out.println(data);
    }

    @Override
    public void processOnCreate(ZData data, Notifier notifier) {
        System.out.println("in processDataOnCreate");
        System.out.println(data);
    }


    @Override
    public void processOnDelete(ZData data, Notifier notifier) {
        System.out.println("in processDataOnDelete");
        System.out.println(data);
    }

    @Override
    public void processOnChange(ZData data, Notifier notifier) {
        System.out.println("in processDataOnChange");
        System.out.println(data);
    }
}
