package com.baixing.pigeon.config.entities;

import com.baixing.pigeon.config.InvalidABConfigException;

import java.io.IOException;

import static org.apache.commons.lang.RandomStringUtils.randomAlphanumeric;

/**
 * Created by onesuper on 14/03/2017.
 */
public abstract class Config {

    private String id = randomAlphanumeric(8);

    public Config() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    abstract boolean validate() throws InvalidABConfigException;

    abstract void parseFromString(String s) throws IOException;

    abstract String serializeToPrettyString() throws IOException;

    abstract String serializeToString() throws IOException;
}
