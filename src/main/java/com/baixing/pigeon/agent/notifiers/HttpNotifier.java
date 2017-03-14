package com.baixing.pigeon.agent.notifiers;

import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by onesuper on 08/03/2017.
 */
public class HttpNotifier extends Notifier {

    private static final Logger logger = LoggerFactory.getLogger(HttpNotifier.class);

    String url;

    public HttpNotifier(String url) {
        Preconditions.checkState(url.startsWith("http://"));
        this.url = url;
    }

    public void notify(Event event) {
        logger.info("call web hook {}: {}", url, event);
    }

    @Override
    public String toString() {
        return "HttpNotifier{url=" + url + "}";
    }
}
