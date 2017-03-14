package com.baixing.pigeon.config.entities;

import com.baixing.pigeon.config.InvalidABConfigException;

/**
 * Created by onesuper on 14/03/2017.
 */
public class ConfigPayload extends Config {

    private String payload;

    public ConfigPayload() {
    }

    @Override
    boolean validate() throws InvalidABConfigException {
        return true;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

}
