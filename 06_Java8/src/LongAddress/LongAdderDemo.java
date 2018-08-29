package LongAddress;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;

public class LongAdderDemo {
    private static final int MAX_THREAD = 3;
    private static final int TASK_COUNT = 3;
    private static final int TARGET_COUNT = 1000000000;

    private AtomicInteger acount = new AtomicInteger(0);
    private LongAdder laccount = new LongAdder();
    private long count = 0;

    static CountDownLatch cdlsync = new CountDownLatch(TASK_COUNT);
    static CountDownLatch cdlatomic = new CountDownLatch(TASK_COUNT);
    static CountDownLatch cdladdr = new CountDownLatch(TASK_COUNT);

    //同步锁
    protected synchronized long inc(){
        return ++count;
    }

    private synchronized long getCount() {
        return count;
    }

    public class SyncThread implements Runnable {
        protected String name;
        protected long starttime;
        LongAdderDemo out;

        public SyncThread(long starttime, LongAdderDemo out) {
            this.starttime = starttime;
            this.out = out;
        }

        @Override
        public void run() {
            long v = out.getCount();
            while (v < TARGET_COUNT){
                v = out.inc();
            }
            long endtime = System.currentTimeMillis();
            System.out.println("SyncThread spend： " + (endtime - starttime) + "ms" + " v= " + v);
            cdlsync.countDown();
        }
    }

    public void testSync() throws InterruptedException {
        ExecutorService es = Executors.newFixedThreadPool(MAX_THREAD);
        long starttime = System.currentTimeMillis();
        SyncThread sync = new SyncThread(starttime, this);
        for (int i = 0 ; i < TASK_COUNT; i++) {
            es.submit(sync);
        }
        cdlsync.await();
        es.shutdown();
    }
    //原子实现
    public class AtomicThread implements Runnable {
        protected String name;
        protected long starttime;

        public AtomicThread(long starttime) {
            this.starttime = starttime;
        }

        @Override
        public void run() {
            long v = acount.get();
            while (v < TARGET_COUNT)
                v = acount.incrementAndGet();
            long endtime = System.currentTimeMillis();
            System.out.println("AtomicThread spends:" + (endtime - starttime) +"ms" + "v = " + v);
            cdlatomic.countDown();
        }

    }

    public void testAtomic () throws InterruptedException {
        ExecutorService es = Executors.newFixedThreadPool(MAX_THREAD);
        long starttime = System.currentTimeMillis();
        AtomicThread atomic = new AtomicThread(starttime);
        for (int i = 0 ; i < TASK_COUNT; i++) {
            es.submit(atomic);
        }
        cdlatomic.await();
        es.shutdown();
    }
    //LongAddr
    public class LongAddrThread implements Runnable {
        protected String name;
        protected long starttime;

        public LongAddrThread(long starttime) {
            this.starttime = starttime;
        }

        @Override
        public void run() {
            long v = laccount.sum();
            while (v < TARGET_COUNT) {
                laccount.increment();
                v = laccount.sum();
            }
            long endtime = System.currentTimeMillis();
            System.out.println("AddrThread spends:" + (endtime - starttime) +"ms" + "v = " + v);
            cdladdr.countDown();
        }
    }
    public void testAddr () throws InterruptedException {
        ExecutorService es = Executors.newFixedThreadPool(MAX_THREAD);
        long starttime = System.currentTimeMillis();
        LongAddrThread addr = new LongAddrThread(starttime);
        for (int i = 0 ; i < TASK_COUNT; i++) {
            es.submit(addr);
        }
        cdladdr.await();
        es.shutdown();
    }
    public static void main(String[] args) throws InterruptedException {
        LongAdderDemo longAdderDemo = new LongAdderDemo();
        longAdderDemo.testSync();
        longAdderDemo.testAtomic();
        longAdderDemo.testAddr();
    }

}
