package ThreadPool;


import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Main {
    public static void main(String[] args) {
        // Create a Threadpool - Args - Number of threads
        /*
            Functions
                submit(Runnable Task)
                stop()
         */
        ThreadPool threadPool = new ThreadPool(2);
        threadPool.submit(() -> System.out.println("Task A"));
        threadPool.submit(() -> System.out.println("Task B"));

        for (int i = 0; i<1000; i++) {
            int taskNo = i;
            threadPool.submit(() -> System.out.println("Task" + taskNo));
        }
        List<Runnable> remainingTasks = threadPool.stop();
        System.out.println("Remaining Tasks:" + remainingTasks);
        ExecutorService executorService = Executors.newFixedThreadPool(2);
    }
}