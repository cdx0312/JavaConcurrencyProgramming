public class ThreadGroupName implements Runnable{
    @Override
    public void run() {
        String groupAndName = Thread.currentThread().getThreadGroup().getName() + "-" + Thread.currentThread().getName();
        while (true) {
            System.out.println("I am " + groupAndName);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        ThreadGroup threadGroup = new ThreadGroup("PrintGroup");
        Thread thread1 = new Thread(threadGroup, new ThreadGroupName(), "T1");
        Thread thread2 = new Thread(threadGroup, new ThreadGroupName(), "t2");
        thread1.start();
        thread2.start();
        System.out.println(threadGroup.activeCount());
        threadGroup.list();
    }
}
