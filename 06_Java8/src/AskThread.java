import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class AskThread implements Runnable{
    CompletableFuture<Integer> re = null;

    public AskThread(CompletableFuture<Integer> re) {
        this.re = re;
    }

    @Override
    public void run() {
        int myRe = 0;
        try {
            myRe = re.get() * re.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println(myRe);
    }

    public static Integer calc(Integer para) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return para*para;
    }

    public static Integer calc1 (Integer para) {
        return para/0;
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
//        final CompletableFuture<Integer> future = new CompletableFuture<>();
//        new Thread(new AskThread(future)).start();
//        Thread.sleep(1000);
//        future.complete(129);
//        CompletableFuture<Void> fu = CompletableFuture.supplyAsync(()->calc(50))
//                .thenApply((i)->Integer.toString(i))
//                .thenApply((str)->"\"" + str + "\"")
//                .thenAccept(System.out::println);
//        fu.get();
        //异常处理
        CompletableFuture<Void> future = CompletableFuture
                .supplyAsync(()->calc1(50))
                .exceptionally(ex->{
                    System.out.println(ex.toString());
                    return 0;
                })
                .thenApply((i)->Integer.toString(i))
                .thenApply((str)->"\"" + str + "\"")
                .thenAccept(System.out::println);
        future.get();
    }
}
