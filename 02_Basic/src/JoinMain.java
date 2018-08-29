public class JoinMain {
    public volatile static int i = 0;
    public static class AddThread extends Thread {
        @Override
        public void run() {
            i = 0;
            while (i < 1000000) {
                i++;
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        AddThread addThread = new AddThread();
        addThread.start();
        addThread.join();
        System.out.println(i);
    }
}
