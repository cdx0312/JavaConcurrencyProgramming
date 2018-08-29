public class StaticSingleton {
    private StaticSingleton() {
        System.out.println("StaticSingleton is created1");
    }

    private static class SingletonHolder {
        private static StaticSingleton instance = new StaticSingleton();
    }

    public static StaticSingleton getInstance() {
        return SingletonHolder.instance;
    }
}
