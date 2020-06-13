package client;

import encrypt.MessageDecrypt;
import encrypt.MessageEncrypt;
import encrypt.des.DES;
import encrypt.rsa.RSA;
import message.Message;
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
    private DES des;

    private String account = "user";


    public void start(String host, int port) {
        try {
            server = new Socket(host, port);
            initStream();
            out.println("connected: " + server.getLocalSocketAddress());
            initConnect();
            out.println("input your username");
            account = scanner.nextLine();
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
                desKey = new String(rsa.getPrivateKey().decrypt(Utils.base64String2Bytes(msg)));
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
                MessageEncrypt messageEncrypt = new MessageEncrypt(des);
                while (true) {
                    var input = scanner.nextLine();
                    if (input != null && input.length() > 0) {
                        sendMsg(des.encrypt(messageEncrypt.aes(new Message(account, input))));
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
                MessageDecrypt messageDecrypt = new MessageDecrypt(des);
                while (true) {
                    var receive = receiveMsg();
                    if (receive != null && receive.length() > 0) {
                        Message decrypted = (Message) messageDecrypt.des(receive);
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

    private void sendMsg(String msg) throws IOException {
        printWriter.println(msg);
        printWriter.flush();
    }

    private String receiveMsg() throws IOException {
        return bufferedReader.readLine();
    }

}