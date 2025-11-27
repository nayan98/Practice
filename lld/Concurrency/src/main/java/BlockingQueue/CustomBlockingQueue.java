package BlockingQueue;

import java.util.ArrayDeque;
import java.util.Locale;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CustomBlockingQueue<T> {
    private final Queue<T> queue;
    private final int maxCapacity;
    private final Condition producer;
    private final Condition consumer;
    private final Lock lock;
    CustomBlockingQueue(int maxCapacity) {
        this.maxCapacity = maxCapacity;
        this.queue = new ArrayDeque<>(maxCapacity);
        lock = new ReentrantLock();
        producer = lock.newCondition();
        consumer = lock.newCondition();
    }
    public void put(T t) {
        lock.lock();
        System.out.println(Thread.currentThread().getName());
        try {
            // overflow condition

            while (isFull()) {
                System.out.println("Producer Waiting ... for element " + t);
                producer.await();
            }
            // Non overflow condition
            queue.offer(t);
            System.out.println("Element Offered in queue: " + t);
            consumer.signal();
        }
        catch (InterruptedException ignore) {

        }
        finally {
            lock.unlock();
        }
    }
    public T take() {
        lock.lock();
        System.out.println(Thread.currentThread().getName());
        try {
            // underflow Condition
            while (queue.isEmpty()) {
                System.out.println("Consumer waiting ...");
                consumer.await();
            }
            // Non underflow condition
            T element = queue.poll();
            System.out.println("Element removed from queue: " + element);
            producer.signal();
            return element;
        }
        catch (InterruptedException ignore) {
            return null;
        }
        finally {
            lock.unlock();
        }
    }
    private boolean isFull() {
        return queue.size() == maxCapacity;
    }
}
