public class AccountingVol implements Runnable{
    static AccountingVol instance = new AccountingVol();
    static volatile int i = 0;
    @Override
    public void run() {
        for (int j = 0; j < 10000000; j++)
            synchronized (instance) {
                increase();
            }
    }

    private void increase() {
        i++;
    }

    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new Thread(instance);
        Thread thread2 = new Thread(instance);
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        System.out.println(i);
    }
}
