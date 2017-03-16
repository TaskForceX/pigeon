package com.baixing.pigeon;

/**
 * Created by onesuper on 03/03/2017.
 */
public class Pigeon {

    public static void main(String[] args) {
        try {
            final PigeonContext context = new PigeonContext();

            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    context.terminate();
                }
            });

            context.start();
        } catch ( Exception e ) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
