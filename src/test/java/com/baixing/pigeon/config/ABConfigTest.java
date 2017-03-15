package com.baixing.pigeon.config;

import com.baixing.pigeon.config.entities.ABConfig;
import com.baixing.pigeon.config.entities.ABGroup;
import com.baixing.pigeon.config.entities.ConfigStatus;
import com.google.common.collect.ImmutableList;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by onesuper on 13/03/2017.
 */
public class ABConfigTest {
    @Test
    public void testTryWork1() throws Exception {
        ABConfig config = new ABConfig();

        config.setStatus(ConfigStatus.READY);
        config.setId("testTryWork");
        config.setStartTime(10L);
        config.setEndTime(20L);

        Assert.assertFalse(config.tryWork(9L));
        Assert.assertEquals(ConfigStatus.READY, config.getStatus());
    }

    @Test
    public void testTryWork2() throws Exception {
        ABConfig config = new ABConfig();

        config.setStatus(ConfigStatus.READY);
        config.setId("testTryWork");
        config.setStartTime(10L);
        config.setEndTime(20L);

        Assert.assertTrue(config.tryWork(15L));
        Assert.assertEquals(ConfigStatus.WORKING, config.getStatus());
    }

    @Test
    public void testTryWork3() throws Exception {
        ABConfig config = new ABConfig();

        config.setStatus(ConfigStatus.READY);
        config.setId("testTryWork");
        config.setStartTime(10L);
        config.setEndTime(20L);

        Assert.assertFalse(config.tryWork(30L));
        Assert.assertEquals(ConfigStatus.READY, config.getStatus());
    }

    @Test
    public void testTryWork4() throws Exception {
        ABConfig config = new ABConfig();

        config.setStatus(ConfigStatus.EXPIRED);
        config.setId("testTryWork");
        config.setStartTime(10L);
        config.setEndTime(20L);

        Assert.assertFalse(config.tryWork(30L));
        Assert.assertEquals(ConfigStatus.EXPIRED, config.getStatus());
    }

    @Test
    public void testTryWork5() throws Exception {
        ABConfig config = new ABConfig();

        config.setStatus(ConfigStatus.DEACTIVATED);
        config.setId("testTryWork");
        config.setStartTime(10L);
        config.setEndTime(20L);

        Assert.assertFalse(config.tryWork(30L));
        Assert.assertEquals(ConfigStatus.DEACTIVATED, config.getStatus());
    }

    @Test
    public void testTryExpire1() throws Exception {
        ABConfig config = new ABConfig();

        config.setStatus(ConfigStatus.READY);
        config.setId("testTryExpire");
        config.setStartTime(10L);
        config.setEndTime(20L);

        Assert.assertTrue(config.tryExpire(30L));
        Assert.assertEquals(ConfigStatus.EXPIRED, config.getStatus());
    }

    @Test
    public void testTryExpire2() throws Exception {
        ABConfig config = new ABConfig();

        config.setStatus(ConfigStatus.WORKING);
        config.setId("testTryExpire");
        config.setStartTime(10L);
        config.setEndTime(20L);

        Assert.assertTrue(config.tryExpire(30L));
        Assert.assertEquals(ConfigStatus.EXPIRED, config.getStatus());
    }

    @Test
    public void testTryExpire3() throws Exception {
        ABConfig config = new ABConfig();

        config.setStatus(ConfigStatus.READY);
        config.setId("testTryExpire");
        config.setStartTime(10L);
        config.setEndTime(20L);

        Assert.assertFalse(config.tryExpire(15));
        Assert.assertEquals(ConfigStatus.READY, config.getStatus());
    }

    @Test
    public void testTryExpire4() throws Exception {
        ABConfig config = new ABConfig();

        config.setStatus(ConfigStatus.WORKING);
        config.setId("testTryExpire");
        config.setStartTime(10L);
        config.setEndTime(20L);

        Assert.assertFalse(config.tryExpire(15));
        Assert.assertEquals(ConfigStatus.WORKING, config.getStatus());
    }

    @Test
    public void testTryExpire5() throws Exception {
        ABConfig config = new ABConfig();

        config.setStatus(ConfigStatus.DEACTIVATED);
        config.setId("testTryExpire");
        config.setStartTime(10L);
        config.setEndTime(20L);

        Assert.assertFalse(config.tryExpire(15));
        Assert.assertEquals(ConfigStatus.DEACTIVATED, config.getStatus());
    }

    @Test
    public void testActivate() throws Exception {
        ABConfig config = new ABConfig();

        config.setStatus(ConfigStatus.DEACTIVATED);
        config.setId("testTryActivate");

        config.activate();
        Assert.assertEquals(ConfigStatus.READY, config.getStatus());
    }

    @Test
    public void testDeactivate1() throws Exception {
        ABConfig config = new ABConfig();

        config.setStatus(ConfigStatus.READY);
        config.setId("testTryDeactivate");

        config.deactivate();
        Assert.assertEquals(ConfigStatus.DEACTIVATED, config.getStatus());
    }

    @Test
    public void testDeactivate2() throws Exception {
        ABConfig config = new ABConfig();

        config.setStatus(ConfigStatus.WORKING);
        config.setId("testTryDeactivate");

        config.deactivate();
        Assert.assertEquals(ConfigStatus.DEACTIVATED, config.getStatus());
    }

    @Test
    public void testDeactivate3() throws Exception {
        ABConfig config = new ABConfig();

        config.setStatus(ConfigStatus.EXPIRED);
        config.setId("testTryDeactivate");

        config.deactivate();
        Assert.assertEquals(ConfigStatus.DEACTIVATED, config.getStatus());
    }

    @Test
    public void testSerialize() throws Exception {
        ABConfig config = new ABConfig();

        config.setStatus(ConfigStatus.READY);
        config.setId("testId");
        config.setStartTime(0L);
        config.setEndTime(1L);

        Assert.assertEquals("{\"id\":\"testId\",\"groups\":[],\"startTime\":0,\"endTime\":1,\"status\":\"READY\"}", config.serializeToString());
    }

    @Test
    public void testDeserialize() throws Exception {
        ABConfig config = new ABConfig();
        config.parseFromString("{\"id\":\"testId\",\"groups\":[],\"startTime\":0,\"endTime\":1,\"status\":\"READY\"}");
        Assert.assertEquals("testId", config.getId());
        Assert.assertEquals(0, config.getStartTime().longValue());
        Assert.assertEquals(1, config.getEndTime().longValue());
        Assert.assertEquals(ConfigStatus.READY, config.getStatus());
    }

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