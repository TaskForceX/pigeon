package com.baixing.pigeon.agent.notifiers;

import com.baixing.pigeon.agent.zookeeper.ZookeeperData;

/**
 * Created by onesuper on 08/03/2017.
 */
public class Event {

    public static Event create(ZookeeperData data) {
        return new Event(EventType.CREATE, data);
    }

    public static Event update(ZookeeperData data) {
        return new Event(EventType.UPDATE, data);
    }

    public static Event delete(ZookeeperData data) {
        return new Event(EventType.DELETE, data);
    }

    private Event(EventType type, ZookeeperData data) {
        this.type = type;
        this.zdata = data;
    }

    private EventType type;
    private ZookeeperData zdata;

    public EventType getType() {
        return type;
    }

    public ZookeeperData getData() {
        return zdata;
    }
}
