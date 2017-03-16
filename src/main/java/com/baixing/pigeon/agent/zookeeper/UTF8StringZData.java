package com.baixing.pigeon.agent.zookeeper;

import com.google.common.base.Charsets;

/**
 * Created by onesuper on 08/03/2017.
 */
public class UTF8StringZData extends ZData {

    public String getAsString() {
        if (payload == null) {
            return null;
        }

        return new String(payload, Charsets.UTF_8);
    }

    @Override
    public String toString() {
        return "UTF8StringZData{" +
                "path=" + getPath()  + ", " +
                "data=" + getAsString()  +
                '}';
    }
}
