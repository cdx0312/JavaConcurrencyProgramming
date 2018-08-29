public class StopThreadUnsafe {
    public static User user = new User();
    public static class User {
        private int id;
        private String name;

        public User() {
            this.id = 0;
            this.name = "0";
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "User{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    '}';
        }
    }

    public static class ChangeObjectThread extends Thread {
        volatile boolean stopme = false;

        @Override
        public void run() {
            while (true) {
                if (stopme) {
                    System.out.println("exit by stop me");
                    break;
                }
                synchronized (user) {
                    int v = (int)(System.currentTimeMillis()/1000);
                    user.setId(v);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    user.setName(String.valueOf(v));
                }
                Thread.yield();
            }
        }

        public void stopMe() {
            stopme = true;
        }
    }

    public static class ReadObjectThread extends Thread {
        @Override
        public void run() {
            while (true) {
                synchronized (user) {
                    if (user.getId() != Integer.parseInt(user.getName()))
                        System.out.println(user.toString());
                }
                Thread.yield();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new ReadObjectThread().start();
        while (true) {
            Thread thread = new ChangeObjectThread();
            thread.start();
            Thread.sleep(150);
            new ChangeObjectThread().stopMe();
        }
    }

}
