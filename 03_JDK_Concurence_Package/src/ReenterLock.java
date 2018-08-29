import java.util.concurrent.locks.ReentrantLock;

public class ReenterLock implements Runnable{
    public static ReentrantLock lock = new ReentrantLock();
    public static int i = 0;

    @Override
    public void run() {
        for (int j = 0; j < 10000000; j++) {
            lock.lock();
            try {
                i++;
            }finally {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ReenterLock reenterLock = new ReenterLock();
        Thread thread = new Thread(new ReenterLock());
        Thread thread1 = new Thread(new ReenterLock());
        thread.start();
        thread1.start();
        thread.join();
        thread1.join();
        System.out.println(i);
    }
}
