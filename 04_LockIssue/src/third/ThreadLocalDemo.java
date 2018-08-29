package third;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadLocalDemo {
    //SimpleDateFormat线程安全
//    private static final SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final ThreadLocal<SimpleDateFormat> t1= new ThreadLocal<>();

    public static class ParseDate implements Runnable {
        int i = 0;

        public ParseDate(int i) {
            this.i = i;
        }

        @Override
        public void run() {
            try {
//                Date t = sdf.parse("2015-03-29 19:59:" + i%60);
                if (t1.get() == null)
                    t1.set(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
                Date t = t1.get().parse("2015-03-29 19:59:" + i%60);
                System.out.println(i+ ":" + t);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        ExecutorService ex = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 100; i++) {
            ex.execute(new ParseDate(i));
        }
    }
}
