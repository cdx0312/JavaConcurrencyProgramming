package NIO;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NIOEchoServer {
    //selector 用来处理所有的网络连接
    private Selector selector;
    //tp用于处理客户端
    private ExecutorService tp = Executors.newCachedThreadPool();
    private static Map<Socket, Long> time_start = new HashMap<>(10240);

    private void startSesrver() throws Exception{
        //获取Selector对象的实例
        selector = SelectorProvider.provider().openSelector();
        ServerSocketChannel ssc = ServerSocketChannel.open();
        //SocketChannel设置为非阻塞模式
        ssc.configureBlocking(false);
        //端口绑定
        InetSocketAddress isa = new InetSocketAddress(InetAddress.getLocalHost(), 8000);
//        InetSocketAddress isa = new InetSocketAddress(8000);
        ssc.socket().bind(isa);
        //将ServerSocketChannel绑定到Selector上，注册感兴趣时间为ACCEPT
        SelectionKey acceptKey = ssc.register(selector, SelectionKey.OP_ACCEPT);
        //等待分发网络消息
        for (;;) {
            //阻塞方法
            selector.select();
            //获取就绪的SelectorChannel集合
            Set readyKeys = selector.selectedKeys();
            //获取结合的迭代器
            Iterator i = readyKeys.iterator();
            long e = 0;
            //开始迭代
            while (i.hasNext()) {
                SelectionKey sk = (SelectionKey) i.next();
                //处理完SelectionKey之后，务必移除
                i.remove();
                if (sk.isAcceptable())
                    doAccept(sk);
                else if (sk.isValid() && sk.isReadable()) {
                    if (!time_start.containsKey(((SocketChannel)sk.channel()).socket())) {
                        time_start.put(((SocketChannel)sk.channel()).socket(), System.currentTimeMillis());
                    }
                    doRead(sk);
                } else if (sk.isValid() && sk.isWritable()) {
                    doWrite(sk);
                    e = System.currentTimeMillis();
                    long b = time_start.remove(((SocketChannel)sk.channel()).socket());
                    System.out.println("Spend ； "  + (e-b) + "ms");
                }
            }
        }
    }

    private void doWrite(SelectionKey sk) {
        SocketChannel channel = (SocketChannel) sk.channel();
        EchoClient echoClient = (EchoClient) sk.attachment();
        LinkedList<ByteBuffer> outq = echoClient.getOutq();

        ByteBuffer bb = outq.getLast();
        try {
            int len = channel.write(bb);
            if (len == -1) {
                disconnect(sk);
                return;
            }
            if (bb.remaining() == 0) {
                outq.removeLast();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (outq.size() == 0) {
            sk.interestOps(SelectionKey.OP_READ);
        }
    }

    private void doRead(SelectionKey sk) {
        SocketChannel channel = (SocketChannel) sk.channel();
        ByteBuffer bb = ByteBuffer.allocate(8192);
        int len;

        try {
            len = channel.read(bb);
            if (len < 0) {
                disconnect(sk);
                return;
            }
        } catch (IOException e) {
            System.out.println("Fail to read from client.");
            e.printStackTrace();
            try {
                disconnect(sk);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return;
        }

        bb.flip();
        tp.execute(new HandleMsg(sk, bb));
    }

    class HandleMsg implements Runnable {
        SelectionKey sk;
        ByteBuffer bb;

        public HandleMsg(SelectionKey sk, ByteBuffer bb) {
            this.sk = sk;
            this.bb = bb;
        }


        @Override
        public void run() {
            EchoClient echoClient = (EchoClient) sk.attachment();
            echoClient.enqueue(bb);
            sk.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
            selector.wakeup();
        }
    }

    private void disconnect(SelectionKey sk) throws IOException {
        sk.channel().close();
    }

    private void doAccept(SelectionKey sk) {
        ServerSocketChannel server = (ServerSocketChannel) sk.channel();
        SocketChannel clientChannel;
        try {
            clientChannel = server.accept();
            clientChannel.configureBlocking(false);

            SelectionKey clientKey = clientChannel.register(selector, SelectionKey.OP_READ);

            EchoClient echoClient = new EchoClient();
            clientKey.attach(echoClient);

            InetAddress clientAddress = clientChannel.socket().getInetAddress();
            System.out.println("Accepted connection from " + clientAddress.getHostAddress() + "." );

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    class EchoClient {
        private LinkedList<ByteBuffer> outq;

        public EchoClient(LinkedList<ByteBuffer> outq) {
            this.outq = outq;
        }

        public EchoClient() {
        }

        public LinkedList<ByteBuffer> getOutq() {
            return outq;
        }

        public void enqueue(ByteBuffer bb) {
            outq.addFirst(bb);
        }
    }
}
