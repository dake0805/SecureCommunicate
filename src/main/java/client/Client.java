package client;

import encrypt.rsa.RSA;
import utils.Utils;

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

    private String desKey;


    public void start(String host, int port) {
        try {
            initStream();
            // 连接服务器
            server = new Socket(host, port);
            out.println("connected: " + server.getLocalSocketAddress());
            initConnect();
            new ReceiveMessageListener().start();
            new SendMessageListener().start();
        } catch (SocketException e) {
            out.println("server error: " + server.getRemoteSocketAddress());
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
                desKey = new String(rsa.getPrivateKey().decrypt(msg.getBytes()));
                out.println("encrypted to server now.");
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
            try {
                while (true) {
                    sendMsg(scanner.nextLine());
                }
            } catch (SocketException e) {
                out.println("服务器" + server.getRemoteSocketAddress() + "嗝屁了");
            } catch (IOException e) {
                // log
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
                // 循环监听，除非掉线，或者服务器宕机了
                while (true) {
                    out.println(receiveMsg());
                }
            } catch (SocketException e) {
                out.println("服务器" + server.getRemoteSocketAddress() + "嗝屁了");
            } catch (IOException e) {
                // log
            }
        }
    }

    /**
     * 发送消息
     *
     * @param msg 消息内容
     * @throws IOException
     */
    private void sendMsg(String msg) throws IOException {
        printWriter.println(msg);
        printWriter.flush();
    }

    private String receiveMsg() throws IOException {
        return bufferedReader.readLine();
    }

}