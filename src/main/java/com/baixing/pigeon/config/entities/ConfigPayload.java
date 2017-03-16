package com.baixing.pigeon.config.entities;

import com.baixing.pigeon.config.InvalidABConfigException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

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


    @Override
    public String serializeToPrettyString() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
    }

    @Override
    public String serializeToString() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this);
    }

    public void parseFromString(String s) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ConfigPayload another = mapper.readValue(s, ConfigPayload.class);

        setId(another.getId());
        this.payload = another.payload;
    }
}
