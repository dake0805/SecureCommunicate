package message;

import java.io.*;

public class MessageSender {

    public static void main(String[] args) throws IOException {
        Message message = new Message("abc", "123");
        ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("person.txt"));
        outputStream.writeObject(message);

        ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("person.txt"));

    }
}
