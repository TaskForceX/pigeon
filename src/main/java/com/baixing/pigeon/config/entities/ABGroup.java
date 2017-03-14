package com.baixing.pigeon.config.entities;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by onesuper on 13/03/2017.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ABGroup {

    String alias; // optional
    List<Integer> buckets = new ArrayList<>();
    Map<String, Object> params = new HashMap<>();

    public ABGroup() {
    }

    public ABGroup(String alias, List<Integer> buckets) {
        this.alias = alias;
        this.buckets = buckets;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public List<Integer> getBuckets() {
        return buckets;
    }

    public void setBuckets(List<Integer> buckets) {
        this.buckets = buckets;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public int first() {
        return buckets.get(0);
    }

    public int last() {
        return buckets.get(1);
    }

}
