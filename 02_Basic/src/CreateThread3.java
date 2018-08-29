public class CreateThread3 implements Runnable{
    @Override
    public void run() {
        System.out.println("runnable interface");
    }

    public static void main(String[] args) {
        Thread thread = new Thread(new CreateThread3());
        thread.start();
    }
}
