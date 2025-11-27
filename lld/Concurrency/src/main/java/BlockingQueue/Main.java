package BlockingQueue;


import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        CustomBlockingQueue<Integer> customBlockingQueue = new CustomBlockingQueue<Integer>(5);
        new Thread(() -> {
                customBlockingQueue.put(1);
                customBlockingQueue.put(2);
                customBlockingQueue.put(3);
                customBlockingQueue.put(4);
                customBlockingQueue.put(5);
                customBlockingQueue.put(6);
                customBlockingQueue.put(7);

        }).start();
        new Thread(() -> {
                Integer element = customBlockingQueue.take();
        }).start();
    }
}