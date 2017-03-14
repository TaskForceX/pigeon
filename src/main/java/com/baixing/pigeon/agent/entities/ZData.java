package com.baixing.pigeon.agent.entities;

/**
 * Created by onesuper on 08/03/2017.
 */
public abstract class ZData {
    String path;

    public abstract void setPayload(byte[] payload);

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return this.path;
    }
}
