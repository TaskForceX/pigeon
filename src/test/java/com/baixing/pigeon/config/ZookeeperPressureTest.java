package com.baixing.pigeon.config;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * Created by onesuper on 09/03/2017.
 */
public class ZookeeperPressureTest {


    static class ReadThread extends Thread {

        CuratorFramework client;

        public ReadThread(CuratorFramework client) {
            this.client = client;
        }

        @Override
        public void run()  {
            try {

                long start = System.currentTimeMillis();
                long acc = 0;

                for (int i = 0; i < 10000; i++) {
                    long start0 = System.currentTimeMillis();
                    client.getData().forPath("/test_qps");
                    acc +=  (System.currentTimeMillis() - start0);
                }
                System.out.printf("%.2f qps, %.2f ms\n", 10000.0 / (System.currentTimeMillis() - start) * 1000, 1.0 * acc / 10000);
            } catch (Exception ignore) {
                System.out.println(ignore.getMessage());
            }
        }
    }

    public static void main(String[] args) throws Exception {

        final CuratorFramework client = CuratorFrameworkFactory.newClient("",
                new ExponentialBackoffRetry(1000, 3));

        client.start();

        ReadThread t1 = new ReadThread(client);
        ReadThread t2 = new ReadThread(client);
        ReadThread t3 = new ReadThread(client);
        ReadThread t4 = new ReadThread(client);

        t1.start();
        t2.start();
        t3.start();
        t4.start();
    }
}
