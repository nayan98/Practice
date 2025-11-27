package pubsub;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Consumer;

public class PubSub<T> {
    List<Consumer<T>> consumerList;
    BlockingQueue<T> blockingQueue;
    ExecutorService executorService;
    Integer numOfThreads;
    PubSub() {
        blockingQueue = new LinkedBlockingQueue<>();
        consumerList = new CopyOnWriteArrayList<>();
        numOfThreads = 3;
        executorService = Executors.newFixedThreadPool(numOfThreads);
        for(int i = 0; i<numOfThreads; i++) {
            executorService.execute(
                () -> {
                    while (true) {
                        //revisit while condition
                        try {
                            T element = blockingQueue.take();
                            consumerList.forEach(consumer -> {
                                try {
                                    consumer.accept(element);
                                }
                                catch (Exception exception) {
                                    System.out.println("Exception occurred while running a consumer.. ");
                                }
                            });
                        }
                        catch (InterruptedException ignore) {

                        }
                    }
                }
            );
        }
    }
    public boolean offer(T t) {
        return blockingQueue.offer(t);
    }
    public void consume(Consumer<T> consumer) {
        // Think of concurrency here
        consumerList.add(consumer);
    }
}
