package AIO;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class AIOEchoServer {
    public final static int PORT = 8000;
    private AsynchronousServerSocketChannel server;
    //绑定服务器端口，并使用异步信道为服务器
    public AIOEchoServer() throws IOException {
        server = AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(PORT));
    }

    public void start() {
        System.out.println("Server listen on " + PORT);
        server.accept(null, new CompletionHandler<AsynchronousSocketChannel, Object>() {
            final ByteBuffer buffer = ByteBuffer.allocate(1024);
            @Override
            public void completed(AsynchronousSocketChannel result, Object attachment) {
                System.out.println(Thread.currentThread().getName());
                Future<Integer> writeResult = null;
                try {
                    buffer.clear();
                    result.read(buffer).get(100, TimeUnit.SECONDS);
                    buffer.flip();
                    writeResult = result.write(buffer);
                } catch (InterruptedException | ExecutionException | TimeoutException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        server.accept(null, this);
                        writeResult.get();
                        result.close();
                    } catch (InterruptedException | ExecutionException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void failed(Throwable exc, Object attachment) {
                System.out.println("failed : " + exc);
            }

        });
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        new AIOEchoServer().start();
        while (true) {
            Thread.sleep(1000);
        }
    }
}
