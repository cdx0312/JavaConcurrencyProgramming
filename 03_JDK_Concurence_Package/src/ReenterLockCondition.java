import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ReenterLockCondition implements Runnable{
    public static ReentrantLock lock = new ReentrantLock();
    //condition是和lock相绑定的条件
    public static Condition condition = lock.newCondition();
    @Override
    public void run() {
        try {
            lock.lock();
            //await会使得当前的线程等待，同时释放当前锁,接收到signal信号后会重新获得锁并执行
            condition.await();
            System.out.println("Thread is going on");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ReenterLockCondition reenterLockCondition = new ReenterLockCondition();
        Thread t1 = new Thread(reenterLockCondition);
        //执行到condition。await()，释放锁并进入Condition等待队列
        t1.start();
        Thread.sleep(2000);
        //获取condition相关的锁
        lock.lock();
        //随机唤醒一个处于等待序列中的线程
        condition.signal();
        //释放condition相关的锁使得唤醒线程获得锁并继续执行
        lock.unlock();
    }
}
