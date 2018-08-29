package LongAddress;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

public class AtomicTest
{

    public  static final  int MAX_THREAD_COUNT = 10;
    private static final int TASK_COUNT = 10;
    private static final int TARGET_COUNT = 100000000;

    private AtomicLong atomicLongVal = new AtomicLong(0);
    private LongAdder longAdderVal = new LongAdder();

    private static CountDownLatch latchForAtomicLong = new CountDownLatch(TASK_COUNT);
    private static CountDownLatch latchForLongAdder = new CountDownLatch(TASK_COUNT);

    public class AtomicLongThread implements Runnable {
        protected String name;
        protected long startTime;

        public AtomicLongThread(long startTime) {
            this.startTime = startTime;
        }

        @Override
        public void run()
        {
            long v = atomicLongVal.get();
            while(v < TARGET_COUNT) {
                v = atomicLongVal.incrementAndGet();
            }
            long endTime = System.currentTimeMillis();
            System.out.println("AtomicLongThread Spend:" + (endTime - startTime) + "ms, v = " + v);
            latchForAtomicLong.countDown();
        }

    }

    public class LongAddderThread implements Runnable {
        protected String name;
        protected long startTime;

        public LongAddderThread(long startTime) {
            this.startTime = startTime;
        }

        @Override
        public void run()
        {
            long v = longAdderVal.sum();
            while(v < TARGET_COUNT) {
                longAdderVal.increment();
                v = longAdderVal.sum();
            }
            long endTime = System.currentTimeMillis();
            System.out.println("LongAddderThread Spend:" + (endTime - startTime) + "ms, v = " + v);
            latchForLongAdder.countDown();
        }

    }

    public void testAtomicLongThread() throws InterruptedException {
        ExecutorService exe = Executors.newFixedThreadPool(MAX_THREAD_COUNT);
        long startTime = System.currentTimeMillis();
        AtomicLongThread atomicIntegerThread = new AtomicLongThread(startTime);
        for(int i = 0; i < TASK_COUNT; i++) {
            exe.submit(atomicIntegerThread);
        }
        latchForAtomicLong.await();
        exe.shutdown();
    }

    public void testLongAdderThread() throws InterruptedException {
        ExecutorService exe = Executors.newFixedThreadPool(MAX_THREAD_COUNT);
        long startTime = System.currentTimeMillis();
        LongAddderThread longAddderThread = new LongAddderThread(startTime);
        for(int i = 0; i < TASK_COUNT; i++) {
            exe.submit(longAddderThread);
        }
        latchForLongAdder.await();
        exe.shutdown();
    }

    /**
     * 下面的测试表明，AtomicLong的并发性能比LongAdder要差，在线程竞争越剧烈的场合，其表现出来的性能会更加优越
     * @param args
     * @throws InterruptedException
     * @author lhever 2017年4月4日 下午11:10:06
     * @since v1.0
     */
    public static void main(String[] args) throws InterruptedException {

        /**
         * MAX_THREAD_COUNT、TASK_COUNT的值都为3时候测试结果如下：
         */
        AtomicTest test = new AtomicTest();
        test.testAtomicLongThread();
        test.testLongAdderThread();

        /*
        在本人的win7 64位系统 运行结果如下
        AtomicLongThread Spend:2258ms, v = 100000001
        AtomicLongThread Spend:2258ms, v = 100000002
        AtomicLongThread Spend:2258ms, v = 100000000
        LongAddderThread Spend:2155ms, v = 100000000
        LongAddderThread Spend:2155ms, v = 100000000
        LongAddderThread Spend:2155ms, v = 100000000
        */

        /**
         * MAX_THREAD_COUNT、TASK_COUNT的值都为10时候测试结果如下：
         */
        AtomicTest test1 = new AtomicTest();
        test1.testAtomicLongThread();
        test1.testLongAdderThread();
       /*
                     在本人的win7 64位系统 运行结果如下
        AtomicLongThread Spend:2889ms, v = 100000005
        AtomicLongThread Spend:2890ms, v = 100000002
        AtomicLongThread Spend:2890ms, v = 100000006
        AtomicLongThread Spend:2889ms, v = 100000003
        AtomicLongThread Spend:2889ms, v = 100000004
        AtomicLongThread Spend:2890ms, v = 100000007
        AtomicLongThread Spend:2889ms, v = 100000000
        AtomicLongThread Spend:2889ms, v = 100000001
        AtomicLongThread Spend:2891ms, v = 100000008
        AtomicLongThread Spend:2892ms, v = 100000009
        LongAddderThread Spend:2054ms, v = 100000005
        LongAddderThread Spend:2054ms, v = 100000003
        LongAddderThread Spend:2054ms, v = 100000003
        LongAddderThread Spend:2054ms, v = 100000007
        LongAddderThread Spend:2054ms, v = 100000002
        LongAddderThread Spend:2054ms, v = 100000006
        LongAddderThread Spend:2054ms, v = 100000007
        LongAddderThread Spend:2054ms, v = 100000004
        LongAddderThread Spend:2054ms, v = 100000003
        LongAddderThread Spend:2057ms, v = 100000008
        */


    }



}
