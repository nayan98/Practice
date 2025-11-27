package ReadWriteLock;

import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class FairReadWriteLock {

    private static class Waiter {
        final boolean isWriter;
        boolean granted = false;

        Waiter(boolean writer) {
            this.isWriter = writer;
        }
    }

    private final ReentrantLock lock = new ReentrantLock(true); // fair lock for queue ops
    private final Condition condition = lock.newCondition();

    private final Deque<Waiter> queue = new ArrayDeque<>();

    private int activeReaders = 0;
    private boolean writerActive = false;

    // ---------------- READ LOCK ----------------

    public void lockRead() throws InterruptedException {
        lock.lockInterruptibly();
        try {
            Waiter waiter = new Waiter(false);
            queue.addLast(waiter);

            while (!canGrantRead(waiter)) {
                condition.await();
            }

            waiter.granted = true;
            activeReaders++;

            // Remove from queue only when granted
            queue.remove(waiter);
        } finally {
            lock.unlock();
        }
    }

    public void unlockRead() {
        lock.lock();
        try {
            activeReaders--;
            if (activeReaders == 0) {
                // Wake next queued thread (maybe a writer or reader)
                condition.signalAll();
            }
        } finally {
            lock.unlock();
        }
    }
    public void lockWrite() throws InterruptedException {
        lock.lockInterruptibly();
        try {
            Waiter waiter = new Waiter(true);
            queue.addLast(waiter);

            while (!canGrantWrite(waiter)) {
                condition.await();
            }

            waiter.granted = true;
            writerActive = true;

            queue.remove(waiter);
        } finally {
            lock.unlock();
        }
    }
    public void unlockWrite() {
        lock.lock();
        try {
            writerActive = false;
            condition.signalAll(); // wake next one in queue
        } finally {
            lock.unlock();
        }
    }
    private boolean canGrantRead(Waiter waiter) {
        // Writer is active → no read allowed
        if (writerActive) return false;

        // Look at the head of the queue
        Waiter head = queue.peekFirst();

        // If I am not at head → only allowed if the head is also a reader
        // (reader batching)
        return head == waiter || (head != null && !head.isWriter);
    }

    private boolean canGrantWrite(Waiter waiter) {
        // No active readers & no active writer & MUST be at head
        return !writerActive && activeReaders == 0 && queue.peekFirst() == waiter;
    }
}

