import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 倒计时器，让某一个线程等待倒计时器结束再开始执行
 */
public class CountDownLatchDemo implements Runnable{
    private static final CountDownLatch end = new CountDownLatch(10);
    private static final CountDownLatchDemo demo = new CountDownLatchDemo();

    @Override
    public void run(){
        try {
            Thread.sleep(new Random().nextInt(10) * 100);
            System.out.println("check complete");
            end.countDown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            executorService.submit(demo);
        }

        end.await();

        System.out.println("Fire");
        executorService.shutdown();
    }
}
