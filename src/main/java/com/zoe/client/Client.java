package com.zoe.client;

import com.zoe.client.message.Message;
import com.zoe.client.message.MessageEncryptDecrypt;
import com.zoe.client.message.impl.AesMessageEncryptDecrypt;
import com.zoe.encrypt.rsa.RSA;
import com.zoe.utils.Utils;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

import static java.lang.System.out;

/**
 * @author zy
 */
public class Client {

    private Socket server = null;

    private PrintWriter printWriter;
    private BufferedReader bufferedReader;
    private Scanner scanner;

    private String account = "user";

    MessageEncryptDecrypt messageEncryptDecrypt;

    public boolean startWithGui(String host, int port, String account) {
        try {
            this.account = account;
            server = new Socket(host, port);
            initStream();
            initConnect();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void sendMessageWithGui(String string) {
        if (string != null && string.length() > 0) {
            sendMessage(new Message(account, string));
        }
    }


    public Message receiveMessageWithGui() throws IOException {
        return receiveMsg();
    }


    public void start(String host, int port) {
        try {
            server = new Socket(host, port);
            initStream();
            out.println("connected: " + server.getLocalSocketAddress());
            initConnect();
            out.println("input your username");
            account = scanner.nextLine();
            out.println("welcome, " + account);
            new ReceiveMessageListener().start();
            new SendMessageListener().start();
        } catch (SocketException e) {
            out.println("com.zoe.server error: " + server.getRemoteSocketAddress());
        } catch (IOException e) {
            // log
        }
    }

    private void initConnect() throws IOException {
        RSA rsa = new RSA();
        printWriter.println(Utils.ObjectToJson(rsa.getPublicKey()));
        printWriter.flush();
        while (true) {
            var encryptedKey = bufferedReader.readLine();
            if (encryptedKey != null && encryptedKey.length() >= 1) {
                String key = new String(rsa.getPrivateKey().decrypt(Utils.base64String2Bytes(encryptedKey)));
                messageEncryptDecrypt = new AesMessageEncryptDecrypt(key);
                out.println("encrypted to com.zoe.server now.");
                break;
            }
        }
    }

    private void initStream() throws IOException {
        OutputStream outputStream = server.getOutputStream();
        printWriter = new PrintWriter(outputStream);
        InputStream inputStream = server.getInputStream();
        InputStreamReader isr = new InputStreamReader(inputStream);
        bufferedReader = new BufferedReader(isr);
        scanner = new Scanner(System.in);
    }

    /**
     * 发送消息的线程
     */
    class SendMessageListener extends Thread {
        @Override
        public void run() {
            while (true) {
                var input = scanner.nextLine();
                if (input != null && input.length() > 0) {
                    sendMessage(new Message(account, input));
                }
            }
        }

    }

    /**
     * 接受消息的线程
     */
    class ReceiveMessageListener extends Thread {
        @Override
        public void run() {
            try {
                while (true) {
                    var receiveMessage = receiveMsg();
                    out.println(receiveMessage.toString());
                }
            } catch (SocketException e) {
                out.println("server error" + server.getRemoteSocketAddress());
            } catch (IOException e) {
                // log
            }
        }
    }

    private void sendMessage(Message msg) {
        printWriter.println(messageEncryptDecrypt.encrypt(msg));
        printWriter.flush();
    }

    private Message receiveMsg() throws IOException {
        String receivedInfo = bufferedReader.readLine();
        if (receivedInfo != null && receivedInfo.length() > 0) {
            return messageEncryptDecrypt.decrypt(receivedInfo);
        }
        return new Message("null", "something wrong.");
    }

}