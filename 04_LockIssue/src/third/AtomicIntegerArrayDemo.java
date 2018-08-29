package third;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class AtomicIntegerArrayDemo {
    static AtomicIntegerArray integers = new AtomicIntegerArray(10);
    public static class AddThread implements Runnable {
        @Override
        public void run() {
            for (int k = 0; k < 10000; k++)
                integers.getAndIncrement(k % integers.length());
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread[] threads = new Thread[10];
        for (int k = 0; k < 10; k++) {
            threads[k] = new Thread(new AddThread());
        }
        for (int k = 0; k < 10; k++) {
            threads[k].start();
        }
        for (int k = 0; k < 10; k++) {
            threads[k].join();
        }
        System.out.println(integers);
    }
}
