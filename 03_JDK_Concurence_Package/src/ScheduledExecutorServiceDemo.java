import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 计划任务
 */
public class ScheduledExecutorServiceDemo {
    public static void main(String[] args) {
        ScheduledExecutorService ses = Executors.newScheduledThreadPool(10);
        ses.scheduleAtFixedRate((Runnable) () -> {
            try {
                Thread.sleep(2000);
                System.out.println(System.currentTimeMillis()/1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, 0, 8, TimeUnit.SECONDS);
    }
}
