package com.baixing.pigeon.config.controllers;

import com.baixing.pigeon.config.entities.ABConfig;
import com.baixing.pigeon.config.entities.ConfigPayload;
import com.baixing.pigeon.config.services.ABConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * Created by onesuper on 29/12/2016.
 */
@Controller
public class ABConfigController {

    @Autowired
    private ABConfigService abConfigService;

    @RequestMapping(value = "configs", method = RequestMethod.GET)
    public String listAllConfigs(Model model) throws Exception {
        model.addAttribute("configs", abConfigService.listAllConfigIds());
        return "configs";
    }

    @RequestMapping(value = "config/{id}")
    public String getConfig(@PathVariable String id, Model model) throws Exception {
        ABConfig abConfig = abConfigService.getConfigById(id);

        ConfigPayload config = new ConfigPayload();
        config.setId(id);
        config.setPayload(abConfig.serializeToPrettyString());
        model.addAttribute("config", config);
        return "configshow";
    }

    @RequestMapping(value = "config/edit/{id}")
    public String editConfig(@PathVariable String id, Model model) throws Exception {
        ABConfig abconfig = abConfigService.getConfigById(id);

        ConfigPayload config = new ConfigPayload();
        config.setId(id);
        config.setPayload(abconfig.serializeToPrettyString());
        model.addAttribute("config", config);
        return "configform";
    }

    @RequestMapping("config/new")
    public String newConfig(Model model) {
        ConfigPayload config = new ConfigPayload();
        model.addAttribute("config", config);
        return "configform";
    }

    @RequestMapping(value = "config", method = RequestMethod.POST)
    public String saveConfig(@ModelAttribute ConfigPayload config) throws Exception {
        ABConfig abconfig = new ABConfig();
        abconfig.parseFromString(config.getPayload());
        abconfig.setId(config.getId());
        abConfigService.saveConfig(abconfig);
        return "redirect:/config/" + config.getId();
    }

    @RequestMapping(value = "/config/delete/{id}")
    public String deleteConfig(@PathVariable String id) throws Exception {
        abConfigService.deleteConfig(id);
        return "redirect:/configs";
    }
}
