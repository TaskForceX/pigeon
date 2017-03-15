package com.baixing.pigeon.config.entities;

/**
 * Created by onesuper on 14/03/2017.
 */
public abstract class TransientConfig extends Config {

    abstract boolean tryWork(long currentTime);

    abstract boolean tryExpire(long currentTime);

    abstract void deactivate();

    abstract void activate();
}
