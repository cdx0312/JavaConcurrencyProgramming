public class Interrupt {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            while (true)
                if (Thread.currentThread().isInterrupted()){
                    System.out.println("Interrupted!");
                    break;
                }
                Thread.yield();
        });

        thread.start();
        Thread.sleep(2000);
        thread.interrupt();
    }
}
