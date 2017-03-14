package com.baixing.pigeon.config.controllers;

import com.baixing.pigeon.config.entities.ABConfig;
import com.baixing.pigeon.config.entities.ABGroup;
import com.baixing.pigeon.config.services.ABConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Created by onesuper on 29/12/2016.
 */
@RestController
public class ABConfigRestController {

    @Autowired
    private ABConfigService abConfigService;

    @RequestMapping(value = "/v1/_config", method = RequestMethod.GET)
    public List<String> listAllConfigs() throws Exception {
        return abConfigService.listAllConfigIds();
    }

    @RequestMapping(value = "/v1/_config/{name}", method = RequestMethod.GET)
    public ABConfig findConfig(@PathVariable String name) throws Exception {
        return abConfigService.getConfigById(name);
    }

    @RequestMapping(value = "/v1/_config/{name}", method = RequestMethod.DELETE)
    public void deleteConfig(@PathVariable String name) throws Exception {
        abConfigService.deleteConfig(name);
    }

    @RequestMapping(value = "/v1/_config/{name}", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ABConfig updateConfig(@RequestBody List<ABGroup> abGroups) throws Exception {
        ABConfig config = new ABConfig();
        config.setGroups(abGroups);
        return abConfigService.saveConfig(config);
    }
}
