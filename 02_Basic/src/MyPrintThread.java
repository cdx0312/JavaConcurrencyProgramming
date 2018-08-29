/**
 * Created by cdx0312
 * 2018/3/26
 */
public class MyPrintThread implements Runnable{
    private String name;
    private Object prev;
    private Object self;

    public MyPrintThread(String name, Object prev, Object self) {
        this.name = name;
        this.prev = prev;
        this.self = self;
    }

    @Override
    public void run() {
        int count = 10;
        while (count > 0) {
            synchronized (prev) {
                synchronized (self) {
                    System.out.println(name);
                    count--;
                    self.notify();
                }
                try {
                    prev.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Object a = new Object();
        Object b = new Object();
        Object c = new Object();
        Thread threadA = new Thread(new MyPrintThread("A", c, a));
        Thread threadB = new Thread(new MyPrintThread("B", a, b));
        Thread threadC = new Thread(new MyPrintThread("C", b, c));

        threadA.start();
        Thread.sleep(100);
        threadB.start();
        Thread.sleep(100);
        threadC.start();

    }
}
