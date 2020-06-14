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
        sendMsg(Utils.ObjectToJson(rsa.getPublicKey()));
        while (true) {
            var msg = receiveMsg();
            if (msg != null && msg.length() >= 1) {
                String key = new String(rsa.getPrivateKey().decrypt(Utils.base64String2Bytes(msg)));
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
                    sendMsg(messageEncryptDecrypt.encrypt(new Message(account, input)));
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
                    var receive = receiveMsg();
                    if (receive != null && receive.length() > 0) {
                        Message decrypted = messageEncryptDecrypt.decrypt(receive);
                        out.println(decrypted.toString());
                    }
                }
            } catch (SocketException e) {
                out.println("server error" + server.getRemoteSocketAddress());
            } catch (IOException e) {
                // log
            }
        }
    }

    private void sendMsg(String msg) {
        printWriter.println(msg);
        printWriter.flush();
    }

    private String receiveMsg() throws IOException {
        return bufferedReader.readLine();
    }

}