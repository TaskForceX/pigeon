package com.baixing.pigeon.config.entities;

import com.baixing.pigeon.config.InvalidABConfigException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by onesuper on 13/03/2017.
 */
public class ABConfig extends TransientConfig {

    private static int DEFAULT_CONFIG_DELAY = 3600;

    private List<ABGroup> groups = new ArrayList<>();
    private Long startTime = System.currentTimeMillis() / 1000;
    private Long endTime = System.currentTimeMillis() / 1000 + DEFAULT_CONFIG_DELAY;
    private ConfigStatus status = ConfigStatus.READY;

    public ABConfig() {
    }

    public List<ABGroup> getGroups() {
        return groups;
    }

    public void setGroups(List<ABGroup> groups) {
        this.groups = groups;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public ConfigStatus getStatus() {
        return status;
    }

    public void setStatus(ConfigStatus status) {
        this.status = status;
    }

    @Override
    public boolean tryWork(long currentTime) {
        if (this.status == ConfigStatus.READY) {
            if (currentTime > startTime && currentTime < endTime) {
                this.status = ConfigStatus.WORKING;
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean tryExpire(long currentTime) {
        if (this.status == ConfigStatus.WORKING || this.status == ConfigStatus.READY) {
            if (currentTime > endTime) {
                this.status = ConfigStatus.EXPIRED;
                return true;
            }
        }
        return false;
    }

    @Override
    public void deactivate() {
        this.status = ConfigStatus.DEACTIVATED;
    }

    @Override
    public void activate() {
        this.status = ConfigStatus.READY;
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
        ABConfig another = mapper.readValue(s, ABConfig.class);

        setId(another.getId());
        this.groups = another.groups;
        this.startTime = another.startTime;
        this.endTime = another.endTime;
        this.status = another.status;
    }

    @Override
    public boolean validate() throws InvalidABConfigException {
        Set<Integer> coveredBuckets = new HashSet<>();
        Set<String> names = new HashSet<>();

        if (endTime <= startTime) {
            throw new InvalidABConfigException("startTime must < endTime");
        }

        for (ABGroup group : groups) {
            if (group.getBuckets() == null) {
                throw new InvalidABConfigException("must specify bucket range: " + group);
            }

            if (group.getBuckets().size() != 2) {
                throw new InvalidABConfigException("bucket range must be a pair: " + group);
            }

            if (group.first() >= group.last()) {
                throw new InvalidABConfigException("first bucket > last bucket: " + group);
            }

            if (group.getAlias() != null) {
                if (names.contains(group.getAlias())) {
                    throw new InvalidABConfigException("duplicated group alias " + group.getAlias() + ": " + group);
                }
                names.add(group.getAlias());
            }

            for (int i = group.first(); i < group.last(); i++) {
                if (coveredBuckets.contains(i)) {
                    throw new InvalidABConfigException("duplicated group bucket " + i + ": " + group);
                }
                coveredBuckets.add(i);
            }
        }

        if (coveredBuckets.size() < 2) {
            throw new InvalidABConfigException("AB Test requires at least two groups: " + coveredBuckets.size());
        }

        return true;
    }
}
