import encrypt.rsa.RSA;
import org.junit.Test;

import static utils.Utils.bytesToString;

public class TestRSA {
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
}
