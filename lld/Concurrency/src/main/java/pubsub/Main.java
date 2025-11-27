package pubsub;


import BlockingQueue.CustomBlockingQueue;

import java.util.concurrent.SubmissionPublisher;

public class Main {
    public static void main(String[] args) {
        PubSub<Integer> pubSub = new PubSub<>();
        pubSub.consume(System.out::println);
        pubSub.consume((value) -> System.out.println(value + " diff"));
        new Thread(() -> {
            pubSub.offer(1);
            pubSub.offer(2);
            pubSub.offer(3);
        }).start();
    }
}