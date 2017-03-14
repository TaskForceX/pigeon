package com.baixing.pigeon.agent.notifiers;

import com.baixing.pigeon.agent.entities.ZData;

/**
 * Created by onesuper on 08/03/2017.
 */
public class Event {

    public static enum Type {
        CREATE,
        UPDATE,
        DELETE,
    }

    public static Event create(ZData data) {
        return new Event(Type.CREATE, data);
    }

    public static Event update(ZData data) {
        return new Event(Type.UPDATE, data);
    }

    public static Event delete(ZData data) {
        return new Event(Type.DELETE, data);
    }

    private Event(Type type, ZData data) {
        this.type = type;
        this.zdata = data;
    }

    private Type type;
    private ZData zdata;

    public Type getType() {
        return type;
    }

    public ZData getData() {
        return zdata;
    }
}
