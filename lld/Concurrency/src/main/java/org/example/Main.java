package org.example;


public class Main {
    static class MyThread extends Thread {
        @Override
        public void run() {}
    }
    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(1000);
                System.out.print("Hello World!\n");
            } catch (InterruptedException ignore) {

            }
        });
        Thread myThread = new MyThread();
        myThread.start();
    }
}