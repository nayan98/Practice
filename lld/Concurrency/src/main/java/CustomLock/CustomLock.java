package CustomLock;

import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicBoolean;

public class CustomLock {
    private final AtomicBoolean isLocked = new AtomicBoolean(false);
    public void lock() {
        System.out.println(Thread.currentThread().getName() + " Lock acquiring");
        System.out.println(Thread.currentThread().getName() + LocalTime.now());
        while (!isLocked.compareAndSet(false, true)){
            Thread.yield();
        }
        System.out.println(Thread.currentThread().getName() + LocalTime.now());
        System.out.println(Thread.currentThread().getName() + " Lock acquired");
    }
    public void unlock() {
        isLocked.set(false);
        System.out.println(Thread.currentThread().getName() + " Lock given up");
    }
}
