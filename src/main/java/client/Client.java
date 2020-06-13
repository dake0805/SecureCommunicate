package client;

import encrypt.ObjectDecrypt;
import encrypt.ObjectEncrypt;
import encrypt.des.DES;
import encrypt.rsa.RSA;
import utils.Utils;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

import static java.lang.System.in;
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
    private DES des;


    public void start(String host, int port) {
        try {
            server = new Socket(host, port);
            initStream();
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
                des = new DES(desKey);
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
                ObjectEncrypt objectEncrypt = new ObjectEncrypt(des);
                while (true) {
                    var input = scanner.nextLine();
                    if (input != null && input.length() > 0) {
                        sendMsg(des.encrypt(objectEncrypt.des(input)));
                    }
                }
            } catch (SocketException e) {
                out.println("server error: " + server.getRemoteSocketAddress());
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
                ObjectDecrypt objectDecrypt = new ObjectDecrypt(des);
                while (true) {
                    var receive = receiveMsg();
                    if (receive != null && receive.length() > 0) {
                        out.println(objectDecrypt.des(receive));
                    }
                }
            } catch (SocketException e) {
                out.println("server error" + server.getRemoteSocketAddress());
            } catch (IOException e) {
                // log
            }
        }
    }

    private void sendMsg(String msg) throws IOException {
        printWriter.println(msg);
        printWriter.flush();
    }

    private String receiveMsg() throws IOException {
        return bufferedReader.readLine();
    }

}