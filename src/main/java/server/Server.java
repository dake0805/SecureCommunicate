package server;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import encrypt.ObjectDecrypt;
import encrypt.ObjectEncrypt;
import encrypt.des.DES;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static java.lang.System.out;

/**
 * @author zy
 */
public class Server {

    //port
    private static int port = 3456;
    public static final int THREAD_POOL_SIZE = 32;
    private static int userCount = 0;
    private ThreadPoolExecutor executor;

    private ObjectDecrypt objectDecrypt;
    private ObjectEncrypt objectEncrypt;

    //clients collection
    private static final List<Socket> CLIENTS = new ArrayList<>();

    public void start(int setPort) {
        port = setPort;
        initThreadPool();
        initEncrypt();
        //start server
        try {
            ServerSocket server = new ServerSocket(port);
            out.println("server on");
            //loop
            while (true) {
                Socket client = null;
                client = server.accept();
                out.println("客户端[" + (client != null ? client.getRemoteSocketAddress() : "null address")
                        + "]连接成功，当前在线用户" + userCount + "个");
                // 将客户端添加到集合
                ClientList.getClients().add(client);
                // 每一个客户端开启一个线程处理消息

                //接受publicKey

                executor.execute(new ServerListener(client, CLIENTS));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initEncrypt() {
        DES des = new DES();
        objectEncrypt = new ObjectEncrypt(des);
        objectDecrypt = new ObjectDecrypt(des);
    }

    private void initThreadPool() {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().build();
        executor = new ThreadPoolExecutor(THREAD_POOL_SIZE,
                THREAD_POOL_SIZE,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(1024),
                namedThreadFactory,
                new ThreadPoolExecutor.AbortPolicy());
    }

    public static void main(String[] args) {
        new Server().start(3456);
    }

}