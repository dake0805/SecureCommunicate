package com.zoe.server;

import com.google.gson.Gson;
import com.zoe.message.MessageDecrypt;
import com.zoe.message.MessageEncrypt;
import com.zoe.encrypt.rsa.PublicKey;
import com.zoe.utils.Utils;

import java.io.*;
import java.net.Socket;


class ServerListener implements Runnable {

    private final Socket client;
    private BufferedReader bufferedReader;
    private PrintWriter currentClientWriter;
    private MessageEncrypt messageEncrypt;
    private MessageDecrypt messageDecrypt;


    public ServerListener(Socket socket, MessageEncrypt messageEncrypt, MessageDecrypt messageDecrypt) {
        this.client = socket;
        this.messageDecrypt = messageDecrypt;
        this.messageEncrypt = messageEncrypt;
    }

    @Override
    public void run() {
        try {
            initStream();
            initConnect();
            while (true) {
                forwardData(receiveData());
            }
        } catch (IOException e) {
            // log
        }
    }

    private void initConnect() throws IOException {
        while (true) {
            //get public key
            var msg = receiveData();
            if (msg != null && msg.length() >= 1) {
                PublicKey publicKey = new Gson().fromJson(msg, PublicKey.class);
                var deskey = Utils.bytes2Base64String(publicKey.encrypt(messageEncrypt.getKey().getBytes()));
                //TODO
                currentClientWriter.println(deskey);
                currentClientWriter.flush();
                break;
            }
        }
    }

//    //发送明文消息
//    private void sendMsg(Message plainMessage) throws IOException {
//
//    }


    private void forwardData(String msg) throws IOException {
        for (Socket socket : ClientList.getClients()) {
//            if (socket.equals(com.zoe.client)) {
//                continue;
//            }
            var writer = new PrintWriter(socket.getOutputStream());
            writer.println(msg);
            writer.flush();
            // 这里需要特别注意，对方用readLine获取消息，就必须用print而不能用write，否则会导致消息获取不了
        }
    }


    private String receiveData() throws IOException {
        return bufferedReader.readLine();
    }

    private void initStream() throws IOException {
        InputStream inputStream = client.getInputStream();
        InputStreamReader isr = new InputStreamReader(inputStream);
        bufferedReader = new BufferedReader(isr);
        currentClientWriter = new PrintWriter(client.getOutputStream());
    }

}