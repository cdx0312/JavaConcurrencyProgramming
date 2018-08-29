public class BasicOperation {
    public static void main(String[] args) {
        Thread thread = new Thread(() -> System.out.println("running!!!"));
        thread.start();
    }
}
