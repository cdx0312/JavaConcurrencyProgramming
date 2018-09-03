import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ThreadPoolDemo {
    public static class MyTask implements Runnable {

        @Override
        public void run() {
            System.out.println(System.currentTimeMillis() + ":Thread ID: " + Thread.currentThread().getId());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        MyTask task = new MyTask();
//        ExecutorService service = Executors.newFixedThreadPool(5);
//        ExecutorService service = Executors.newCachedThreadPool();
//        ExecutorService service = Executors.newSingleThreadExecutor();
        ScheduledExecutorService service = Executors.newScheduledThreadPool(5);
        for (int i = 0; i < 10; i++) {
            service.schedule(task, 1000, TimeUnit.MILLISECONDS);
        }
    }
}
