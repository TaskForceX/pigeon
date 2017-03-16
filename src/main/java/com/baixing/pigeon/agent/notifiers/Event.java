package com.baixing.pigeon.agent.notifiers;

import com.baixing.pigeon.agent.zookeeper.ZData;

/**
 * Created by onesuper on 08/03/2017.
 */
public class Event {

    public static Event create(ZData data) {
        return new Event(EventType.CREATE, data);
    }

    public static Event update(ZData data) {
        return new Event(EventType.UPDATE, data);
    }

    public static Event delete(ZData data) {
        return new Event(EventType.DELETE, data);
    }

    private Event(EventType type, ZData data) {
        this.type = type;
        this.zdata = data;
    }

    private EventType type;
    private ZData zdata;

    public EventType getType() {
        return type;
    }

    public ZData getData() {
        return zdata;
    }
}
