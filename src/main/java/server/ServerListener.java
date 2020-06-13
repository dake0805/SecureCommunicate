package server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.out;


class ServerListener extends Thread {

    private final Socket client;

    private OutputStream outputStream;
    private PrintWriter writer;
    private InputStream inputStream;
    private InputStreamReader isr;
    private BufferedReader bufferedReader;
    private static List<Socket> CLIENTS;

    public ServerListener(Socket socket, List<Socket> clients) {
        this.client = socket;
        CLIENTS = clients;
    }

    @Override
    public void run() {
        try {
            // 每个用户连接上了，就发送一条系统消息（类似于广播）
            sendMsg("[系统消息]：欢迎" + client.getRemoteSocketAddress() + "来到聊天室，当前共有" + CLIENTS.size() + "人在聊天");
            // 循环监听消息
            while (true) {
                sendMsg("[" + client.getRemoteSocketAddress() + "]：" + receiveMsg());
            }
        } catch (IOException e) {
            // log
        }
    }

    private void sendMsg(String msg) throws IOException {
        for (Socket socket : CLIENTS) {
            if (socket == client) {
                continue;
            }
            outputStream = socket.getOutputStream();
            writer = new PrintWriter(outputStream);
            writer.println(msg);
            // 这里需要特别注意，对方用readLine获取消息，就必须用print而不能用write，否则会导致消息获取不了
            writer.flush();
        }
    }

    private String receiveMsg() throws IOException {
        inputStream = client.getInputStream();
        isr = new InputStreamReader(inputStream);
        bufferedReader = new BufferedReader(isr);
        return bufferedReader.readLine();
    }
}