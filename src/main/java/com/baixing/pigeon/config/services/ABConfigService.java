package com.baixing.pigeon.config.services;

import com.baixing.pigeon.config.entities.ABConfig;

import java.util.List;

/**
 * Created by onesuper on 13/03/2017.
 */
public interface ABConfigService {

    List<String> listAllConfigIds();

    ABConfig getConfigById(String id);

    ABConfig saveConfig(ABConfig config);

    void deleteConfig(String name);
}
