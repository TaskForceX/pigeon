package com.baixing.pigeon.agent.entities;

import com.google.common.base.Charsets;

/**
 * Created by onesuper on 08/03/2017.
 */
public class UTF8StringZData extends ZData {
    String data;

    public void setPayload(byte[] payload) {
        data  =  new String(payload, Charsets.UTF_8);
    }

    public String getAsString() {
        return data;
    }

    @Override
    public String toString() {
        return "UTF8StringZData{" +
                "path=" + getPath()  + ", " +
                "data=" + data  +
                '}';
    }
}
