package third;

import java.util.Random;
import java.util.concurrent.*;

public class RadomInMultiThreads {
    //每个线程产生的随机数
    public static final int GEN_COUNT = 10000000;
    //线程数
    public static final int THREAD_COUNT = 4;
    //线程池
    static ExecutorService es = Executors.newFixedThreadPool(THREAD_COUNT);
    //多个线程共享的Random实例
    public static Random rnd = new Random(123);
    //ThreadLocal封装的Random
    public static ThreadLocal<Random> tRnd = ThreadLocal.withInitial(() -> new Random(123));

    public static class RndTask implements Callable<Long> {
        private int mode = 0;

        public RndTask(int mode) {
            this.mode = mode;
        }

        public Random getRandom() {
            if (mode == 0){
                return rnd;
            } else if (mode == 1) {
                return tRnd.get();
            } else {
                return null;
            }
        }

        @Override
        public Long call() throws Exception {
            long start = System.currentTimeMillis();
            for (long i = 0; i < GEN_COUNT; i++) {
                getRandom().nextInt();
            }
            long end = System.currentTimeMillis();
            System.out.println(Thread.currentThread().getName() + "spend" + (end - start) + "ms");
            return end - start;
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Future<Long>[] futures = new Future[THREAD_COUNT];
        for (int i = 0; i< THREAD_COUNT; i++) {
            futures[i] = es.submit(new RndTask(0));
        }
        long totalTime = 0;
        for (int i = 0; i < THREAD_COUNT; i++) {
            totalTime += futures[i].get();
        }
        System.out.println("Multi Thread share one instance case : " + totalTime + "ms");
        //ThreadLocal case
        for (int i = 0; i < THREAD_COUNT; i++) {
            futures[i] = es.submit(new RndTask(1));
        }
        totalTime = 0;
        for (int i = 0; i < THREAD_COUNT; i++) {
            totalTime += futures[i].get();
        }
        System.out.println("Multi Thread holds own instance case : " + totalTime + "ms");
        es.shutdown();
    }
}
