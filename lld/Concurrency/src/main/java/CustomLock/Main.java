package CustomLock;


public class Main {
    static class MyThread extends Thread {
        @Override
        public void run() {}
    }
    public static void main(String[] args) {
        CustomLock customLock = new CustomLock();
        new Thread(() -> {
            customLock.lock();
            System.out.println(Thread.currentThread().getName() + " : Inside Lock");
            try {
                Thread.sleep(5000);
            }
            catch (InterruptedException ignore) {

            }
            customLock.unlock();
        }).start();
        new Thread(() -> {
            customLock.lock();
            System.out.println(Thread.currentThread().getName() + " : Inside Lock");
            try {
                Thread.sleep(5000);
            }
            catch (InterruptedException ignore) {

            }
            customLock.unlock();
        }).start();
    }
}