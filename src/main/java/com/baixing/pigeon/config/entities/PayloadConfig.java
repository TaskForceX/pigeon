package com.baixing.pigeon.config.entities;

import com.baixing.pigeon.config.InvalidABConfigException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Created by onesuper on 14/03/2017.
 */
public class PayloadConfig extends Config {

    private String payload;

    public PayloadConfig() {
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }


    @Override
    boolean validate() throws InvalidABConfigException {
        return true;
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
        PayloadConfig another = mapper.readValue(s, PayloadConfig.class);

        setId(another.getId());
        this.payload = another.payload;
    }
}
