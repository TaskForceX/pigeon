package com.baixing.pigeon.config.entities;

import java.io.IOException;

/**
 * Created by onesuper on 14/03/2017.
 */
public interface Serializable {
    void parseFromString(String s) throws IOException;

    String serializeToPrettyString() throws IOException;
}
