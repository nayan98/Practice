package ThreadPool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.locks.ReadWriteLock;

public class ThreadPool {

    private final BlockingQueue<Runnable> taskQueue;
    private Boolean isThreadPoolStopped;
    private final List<Thread> poolThreadList;

    public ThreadPool (int numOfThreads) {
        taskQueue = new ArrayBlockingQueue<>(10);
        isThreadPoolStopped = Boolean.FALSE;
        poolThreadList = new ArrayList<>();
        for (int i = 0; i< numOfThreads; i++) {
            Thread poolThread = new Thread(() -> {
                while (!isShutDown()) {
                    try {
                        Runnable task = taskQueue.take();
                        task.run();
                        System.out.println("Task Completed");
                    }
                    catch (InterruptedException exception) {
                        Thread.currentThread().interrupt();
                        System.out.println("Thread is interrupted");
                        if (isShutDown()) {
                            break;
                        }
                    }
                    catch (Exception exception) {
                        System.out.println("Exception in Executing the Task" + exception.getMessage());
                    }
                }
            });
            poolThreadList.add(poolThread);
            poolThread.start();
        }
    }
    public synchronized Boolean submit(Runnable task) {
        if (!isThreadPoolStopped) {
            try {
                taskQueue.put(task);
                System.out.println("Task Submitted");
                return true;
            }
            catch (InterruptedException interruptedException) {
                Thread.currentThread().interrupt();
                return false;
            }
        }
        else {
            throw new IllegalArgumentException("Thread pool is stopped");
        }
    }
    public synchronized List<Runnable> stop() {
        isThreadPoolStopped = Boolean.TRUE;
        List<Runnable> remainingTasks = new ArrayList<>();
        taskQueue.drainTo(remainingTasks);
        poolThreadList.forEach(Thread::interrupt);
        return remainingTasks;

    }
    private synchronized boolean isShutDown() {
        return isThreadPoolStopped;
    }
}
