import com.google.gson.Gson;
import encrypt.des.DES;
import encrypt.rsa.PublicKey;
import encrypt.rsa.RSA;
import org.junit.Test;

import static utils.Utils.*;

public class TestEncrypt {
   /*   @Test
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
    }*/

    @Test
    public void rsa2() {
        RSA rsa = new RSA();
        var encrypted = bytes2Base64String(rsa.getPublicKey().encrypt("1234".getBytes()));
        var decrypted = new String( rsa.getPrivateKey().decrypt(base64String2Bytes(encrypted)));
        System.out.println(encrypted);
        System.out.println(decrypted);
    }

    @Test
    public void gson() {
        System.out.println(new Gson().toJson("1234"));
    }

    @Test
    public void testDES() {
        DES des = new DES("1234");
        String key = "123213213", message = "helloworld", result = null;

        result = des.encrypt(message);
        System.out.println(result);

        result = des.decrypt(result);

        System.out.println(binToUtf(result));

    }
}
