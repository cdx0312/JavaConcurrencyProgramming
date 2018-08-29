public class BadSuspend {
    public static final Object u = new Object();
    private static ChangeObjectThread thread1 = new ChangeObjectThread("t1");
    private static ChangeObjectThread thread2 = new ChangeObjectThread("t2");

    public static class ChangeObjectThread extends Thread {
        public ChangeObjectThread(String name){
            super.setName(name);
        }

        @Override
        public void run() {
            synchronized (u){
                System.out.println("in " + getName());
                Thread.currentThread().suspend();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        thread1.start();
        Thread.sleep(100);
        thread2.start();
        thread1.resume();
        thread2.resume();
        thread1.join();
        thread2.join();
    }
}
