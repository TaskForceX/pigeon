package com.baixing.pigeon.config.entities;

import com.baixing.pigeon.config.InvalidABConfigException;

import java.util.UUID;

/**
 * Created by onesuper on 14/03/2017.
 */
public abstract class Config {

    private String id = UUID.randomUUID().toString();

    public Config() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    abstract boolean validate() throws InvalidABConfigException;
}
