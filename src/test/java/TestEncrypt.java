import encrypt.des.DES;
import encrypt.rsa.RSA;
import org.junit.Test;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import static utils.Utils.binToUTF;
import static utils.Utils.bytesToString;

public class TestEncrypt {
    PrintWriter printWriter = new PrintWriter(System.out);

    @Test
    public void testRSA() {
        RSA rsa = new RSA();
        String testStr = "hello world, this is rsa test string";
        System.out.println("Encrypting String: " + testStr);
        System.out.println("String in Bytes: "
                + bytesToString(testStr.getBytes()));
        System.out.println("public key is: ");
        System.out.println(rsa.getPublicKey());
        byte[] encrypted = rsa.encrypt(testStr.getBytes());

        System.out.println("encrypted data is: ");
        System.out.println(bytesToString(encrypted));

        byte[] decrypted = rsa.decrypt(encrypted);
        System.out.println("Decrypting Bytes: " + bytesToString(decrypted));
        System.out.println("Decrypted String: " + new String(decrypted));
    }

    @Test
    public void testDES() {
        DES des = new DES();
        String key1 = "12341", message = "helloworld", result = null;

        result = des.encrypt(key1, message);
        System.out.println(result);

        result = des.decrypt(key1, result);

        System.out.println(binToUTF(result));

    }
}
