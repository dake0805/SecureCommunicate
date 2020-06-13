package server;

import com.google.gson.Gson;
import encrypt.ObjectDecrypt;
import encrypt.ObjectEncrypt;
import encrypt.des.DES;
import encrypt.rsa.PublicKey;
import message.Message;

import java.io.*;
import java.net.Socket;
import java.util.List;

import static java.lang.System.out;


class ServerListener implements Runnable {

    private final Socket client;
    private BufferedReader bufferedReader;
    private PrintWriter currentClientWriter;
    private ObjectEncrypt objectEncrypt;
    private ObjectDecrypt objectDecrypt;


    public ServerListener(Socket socket, ObjectEncrypt objectEncrypt, ObjectDecrypt objectDecrypt) {
        this.client = socket;
        this.objectDecrypt = objectDecrypt;
        this.objectEncrypt = objectEncrypt;
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
                //TODO
                currentClientWriter.println(new String(publicKey.encrypt("1234".getBytes())));
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
            if (socket.equals(client)) {
                continue;
            }
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