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
/**
 * 5:Sun Mar 29 19:59:05 CST 2015
 * 9:Sun Mar 29 19:59:09 CST 2015
 * 11:Sun Mar 29 19:59:11 CST 2015
 * 10:Sun Mar 29 19:59:10 CST 2015
 * 6:Sun Mar 29 19:59:06 CST 2015
 * 8:Sun Mar 29 19:59:08 CST 2015
 * 14:Sun Mar 29 19:59:14 CST 2015
 * 15:Sun Mar 29 19:59:15 CST 2015
 * 16:Sun Mar 29 19:59:16 CST 2015
 * 17:Sun Mar 29 19:59:17 CST 2015
 * 19:Sun Mar 29 19:59:19 CST 2015
 * 20:Sun Mar 29 19:59:20 CST 2015
 * 21:Sun Mar 29 19:59:21 CST 2015
 * 1:Sun Mar 29 19:59:01 CST 2015
 * 22:Sun Mar 29 19:59:22 CST 2015
 * 24:Sun Mar 29 19:59:24 CST 2015
 * 25:Sun Mar 29 19:59:25 CST 2015
 * 26:Sun Mar 29 19:59:26 CST 2015
 * 23:Sun Mar 29 19:59:23 CST 2015
 * 27:Sun Mar 29 19:59:27 CST 2015
 * 28:Sun Mar 29 19:59:28 CST 2015
 * 29:Sun Mar 29 19:59:29 CST 2015
 * 31:Sun Mar 29 19:59:31 CST 2015
 * 32:Sun Mar 29 19:59:32 CST 2015
 * 3:Sun Mar 29 19:59:03 CST 2015
 * 33:Sun Mar 29 19:59:33 CST 2015
 * 34:Sun Mar 29 19:59:34 CST 2015
 * 35:Sun Mar 29 19:59:35 CST 2015
 * 0:Sun Mar 29 19:59:00 CST 2015
 * 38:Sun Mar 29 19:59:38 CST 2015
 * 39:Sun Mar 29 19:59:39 CST 2015
 * 7:Sun Mar 29 19:59:07 CST 2015
 * 41:Sun Mar 29 19:59:41 CST 2015
 * 42:Sun Mar 29 19:59:42 CST 2015
 * 43:Sun Mar 29 19:59:43 CST 2015
 * 44:Sun Mar 29 19:59:44 CST 2015
 * 45:Sun Mar 29 19:59:45 CST 2015
 * 40:Sun Mar 29 19:59:40 CST 2015
 * 46:Sun Mar 29 19:59:46 CST 2015
 * 18:Sun Mar 29 19:59:18 CST 2015
 * 48:Sun Mar 29 19:59:48 CST 2015
 * 37:Sun Mar 29 19:59:37 CST 2015
 * 51:Sun Mar 29 19:59:51 CST 2015
 * 36:Sun Mar 29 19:59:36 CST 2015
 * 52:Sun Mar 29 19:59:52 CST 2015
 * 53:Sun Mar 29 19:59:53 CST 2015
 * 54:Sun Mar 29 19:59:54 CST 2015
 * 56:Sun Mar 29 19:59:56 CST 2015
 * 55:Sun Mar 29 19:59:55 CST 2015
 * 57:Sun Mar 29 19:59:57 CST 2015
 * 58:Sun Mar 29 19:59:58 CST 2015
 * 60:Sun Mar 29 19:59:00 CST 2015
 * 61:Sun Mar 29 19:59:01 CST 2015
 * 62:Sun Mar 29 19:59:02 CST 2015
 * 30:Sun Mar 29 19:59:30 CST 2015
 * 13:Sun Mar 29 19:59:13 CST 2015
 * 63:Sun Mar 29 19:59:03 CST 2015
 * 64:Sun Mar 29 19:59:04 CST 2015
 * 12:Sun Mar 29 19:59:12 CST 2015
 * 68:Sun Mar 29 19:59:08 CST 2015
 * 66:Sun Mar 29 19:59:06 CST 2015
 * 59:Sun Mar 29 19:59:59 CST 2015
 * 50:Sun Mar 29 19:59:50 CST 2015
 * 70:Sun Mar 29 19:59:10 CST 2015
 * 49:Sun Mar 29 19:59:49 CST 2015
 * 47:Sun Mar 29 19:59:47 CST 2015
 * 73:Sun Mar 29 19:59:13 CST 2015
 * 72:Sun Mar 29 19:59:12 CST 2015
 * 75:Sun Mar 29 19:59:15 CST 2015
 * 76:Sun Mar 29 19:59:16 CST 2015
 * 78:Sun Mar 29 19:59:18 CST 2015
 * 79:Sun Mar 29 19:59:19 CST 2015
 * 71:Sun Mar 29 19:59:11 CST 2015
 * 81:Sun Mar 29 19:59:21 CST 2015
 * 83:Sun Mar 29 19:59:23 CST 2015
 * 82:Sun Mar 29 19:59:22 CST 2015
 * 80:Sun Mar 29 19:59:20 CST 2015
 * 84:Sun Mar 29 19:59:24 CST 2015
 * 86:Sun Mar 29 19:59:26 CST 2015
 * 87:Sun Mar 29 19:59:27 CST 2015
 * 88:Sun Mar 29 19:59:28 CST 2015
 * 89:Sun Mar 29 19:59:29 CST 2015
 * 85:Sun Mar 29 19:59:25 CST 2015
 * 90:Sun Mar 29 19:59:30 CST 2015
 * 91:Sun Mar 29 19:59:31 CST 2015
 * 93:Sun Mar 29 19:59:33 CST 2015
 * 69:Sun Mar 29 19:59:09 CST 2015
 * 95:Sun Mar 29 19:59:35 CST 2015
 * 96:Sun Mar 29 19:59:36 CST 2015
 * 65:Sun Mar 29 19:59:05 CST 2015
 * 98:Sun Mar 29 19:59:38 CST 2015
 * 67:Sun Mar 29 19:59:07 CST 2015
 * 99:Sun Mar 29 19:59:39 CST 2015
 * 97:Sun Mar 29 19:59:37 CST 2015
 * 94:Sun Mar 29 19:59:34 CST 2015
 * 92:Sun Mar 29 19:59:32 CST 2015
 * 77:Sun Mar 29 19:59:17 CST 2015
 * 74:Sun Mar 29 19:59:14 CST 2015
 * 2:Sun Mar 29 19:59:02 CST 2015
 * 4:Sun Mar 29 19:59:04 CST 2015
 */
