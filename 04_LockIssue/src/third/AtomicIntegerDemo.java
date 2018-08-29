package third;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicIntegerDemo {
    static AtomicInteger integer = new AtomicInteger();
    public static class AddThread implements Runnable {

        @Override
        public void run() {
            for (int k = 0; k < 10000; k++)
                integer.incrementAndGet();
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
        System.out.println(integer);
    }
}
