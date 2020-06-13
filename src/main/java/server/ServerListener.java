package server;

import message.Message;

import java.io.*;
import java.net.Socket;
import java.util.List;


class ServerListener extends Thread {

    private final Socket client;

    private PrintWriter writer;
    private BufferedReader bufferedReader;
    private static List<Socket> CLIENTS;

    public ServerListener(Socket socket, List<Socket> clients) {
        this.client = socket;
        CLIENTS = clients;
    }

    @Override
    public void run() {
        try {
            initStream();
            //sendMsg("[系统消息]：欢迎" + client.getRemoteSocketAddress() + "来到聊天室，当前共有" + CLIENTS.size() + "人在聊天");
            while (true) {
                //sendMsg("[" + client.getRemoteSocketAddress() + "]：" + receiveMsg());
            }
        } catch (IOException e) {
            // log
        }
    }

    private void sendMsg(Message plainMessage) throws IOException {

    }


    private void forwardMsg(String msg) throws IOException {
        for (Socket socket : CLIENTS) {
            if (socket == client) {
                continue;
            }
            writer.println(msg);
            // 这里需要特别注意，对方用readLine获取消息，就必须用print而不能用write，否则会导致消息获取不了
            writer.flush();
        }
    }


    private String receiveMsg() throws IOException {
        return bufferedReader.readLine();
    }

    private void initStream() throws IOException {
        OutputStream outputStream = client.getOutputStream();
        writer = new PrintWriter(outputStream);
        InputStream inputStream = client.getInputStream();
        InputStreamReader isr = new InputStreamReader(inputStream);
        bufferedReader = new BufferedReader(isr);
    }

}