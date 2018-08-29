package future;

public class Main {
    public static void main(String[] args) {
        Client client = new Client();
        //会立即返回，得到FutureData
        Data data = client.request("name");
        System.out.println("请求完毕");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("数据 = " + data.getResult());
    }
}
