package com.baixing.pigeon.config;

import com.baixing.pigeon.config.entities.ABConfig;
import com.baixing.pigeon.config.entities.ABGroup;
import com.google.common.collect.ImmutableList;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by onesuper on 13/03/2017.
 */
public class JsonABConfigTest {
    @Test
    public void validatePass() throws Exception {
        ABConfig config = new ABConfig();

        List<ABGroup> groups = new ArrayList<>();

        groups.add(new ABGroup("A", ImmutableList.of(0, 12)));
        groups.add(new ABGroup("B", ImmutableList.of(12, 24)));

        config.setGroups(groups);
        Assert.assertTrue(config.validate());
    }

    @Test(expected = InvalidABConfigException.class)
    public void validateFailOnDuplicateName() throws Exception {
        ABConfig config = new ABConfig();

        List<ABGroup> groups = new ArrayList<>();

        groups.add(new ABGroup("A", ImmutableList.of(0, 12)));
        groups.add(new ABGroup("A", ImmutableList.of(12, 24)));

        config.setGroups(groups);
        config.validate();
    }

    @Test(expected = InvalidABConfigException.class)
    public void validateFailOnDuplicateBuckets() throws Exception {
        ABConfig config = new ABConfig();

        List<ABGroup> groups = new ArrayList<>();

        groups.add(new ABGroup("A", ImmutableList.of(0, 12)));
        groups.add(new ABGroup("B", ImmutableList.of(11, 24)));

        config.setGroups(groups);
        config.validate();
    }

    @Test(expected = InvalidABConfigException.class)
    public void validateFailOnOneGroup() throws Exception {
        ABConfig config = new ABConfig();

        List<ABGroup> groups = new ArrayList<>();

        groups.add(new ABGroup("A", ImmutableList.of(0, 12)));
        config.setGroups(groups);
        config.validate();
    }

    @Test(expected = InvalidABConfigException.class)
    public void validateFailOnWrongBuckets() throws Exception {
        ABConfig config = new ABConfig();

        List<ABGroup> groups = new ArrayList<>();

        groups.add(new ABGroup("A", ImmutableList.of(0, 0)));
        groups.add(new ABGroup("B", ImmutableList.of(11, 24)));
        config.setGroups(groups);
        config.validate();
    }

    @Test(expected = InvalidABConfigException.class)
    public void validateFailOnNullBuckets() throws Exception {
        ABConfig config = new ABConfig();

        List<ABGroup> groups = new ArrayList<>();

        groups.add(new ABGroup("A", null));
        groups.add(new ABGroup("B", ImmutableList.of(11, 24)));
        config.setGroups(groups);
        config.validate();
    }
}