package com.baixing.pigeon.agent.zookeeper;

import com.google.common.base.Charsets;

/**
 * Created by onesuper on 08/03/2017.
 */
public class UTF8StringZookeeperData extends ZookeeperData {

    public String getAsString() {
        if (payload == null) {
            return null;
        }

        return new String(payload, Charsets.UTF_8);
    }

    public void setUTF8String(String s) {
        setPayload(s.getBytes());
    }

    @Override
    public String toString() {
        return "UTF8StringZookeeperData{" +
                "path=" + getPath()  + ", " +
                "data=" + getAsString()  +
                '}';
    }
}
