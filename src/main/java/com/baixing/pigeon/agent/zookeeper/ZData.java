package com.baixing.pigeon.agent.zookeeper;

/**
 * Created by onesuper on 08/03/2017.
 */
public abstract class ZData {
    String path;
    byte[] payload;

    public void setPayload(byte[] payload) {
        this.payload = payload;
    }

    public byte[] getPayload() {
        return payload;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return this.path;
    }
}
