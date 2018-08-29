package future.inJDK;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class FutureMain {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //construct  FutureTask
        FutureTask<String> futureTask = new FutureTask<String>(new RealData("a"));
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        //start thread and perform the call method
        executorService.submit(futureTask);

        System.out.println("request is over!");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //wait until the call method is over
        System.out.println("data = " + futureTask.get());
    }
}
