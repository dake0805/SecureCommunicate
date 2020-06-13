import encrypt.des.DES;
import encrypt.rsa.PublicKey;
import encrypt.rsa.RSA;
import org.junit.Test;

import static utils.Utils.binToUtf;
import static utils.Utils.bytesToString;

public class TestEncrypt {
    @Test
    public void testRSA() {
        RSA rsa = new RSA();
        String testStr = "hello world, this is rsa test string";
        System.out.println("Encrypting String: " + testStr);
        System.out.println("String in Bytes: "
                + bytesToString(testStr.getBytes()));
        System.out.println("public key is: ");
        PublicKey publicKey = rsa.getPublicKey();
        System.out.println(publicKey);
        byte[] encrypted = publicKey.encrypt(testStr.getBytes());

        System.out.println("encrypted data is: ");
        System.out.println(bytesToString(encrypted));

        byte[] decrypted = rsa.getPrivateKey().decrypt(encrypted);
        System.out.println("Decrypting Bytes: " + bytesToString(decrypted));
        System.out.println("Decrypted String: " + new String(decrypted));
    }

    @Test
    public void testDES() {
        DES des = new DES();
        String key = "123213213", message = "helloworld", result = null;

        result = des.encrypt(message);
        System.out.println(result);

        result = des.decrypt(result);

        System.out.println(binToUtf(result));

    }
}
