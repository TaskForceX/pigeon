package com.baixing.pigeon.agent.notifiers;

/**
 * Created by onesuper on 08/03/2017.
 */
public class StdNotifier extends Notifier {

    public StdNotifier() {
    }

    public void notify(Event event) {
        switch (event.getType()) {
            case CREATE:
                System.out.println("create: " + event.getData());
                break;
            case UPDATE:
                System.out.println("update: " + event.getData());
                break;
            case DELETE:
                System.out.println("delete: " + event.getData());
                break;
        }
    }
}
