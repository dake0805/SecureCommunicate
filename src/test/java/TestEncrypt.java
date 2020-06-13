//import com.google.gson.Gson;
//import com.zoe.message.MessageDecrypt;
//import com.zoe.message.MessageEncrypt;
//import com.zoe.encrypt.des.DES;
//import com.zoe.encrypt.rsa.RSA;
//import com.zoe.message.Message;
//import org.junit.Test;
//
//import static com.zoe.utils.Utils.*;
//
//public class TestEncrypt {
//   /*   @Test
//  public void testRSA() {
//        RSA rsa = new RSA();
//        String testStr = "hello world, this is rsa test string";
//        System.out.println("Encrypting String: " + testStr);
//        System.out.println("String in Bytes: "
//                + bytesToString(testStr.getBytes()));
//        System.out.println("public key is: ");
//        PublicKey publicKey = rsa.getPublicKey();
//        System.out.println(publicKey);
//        byte[] encrypted = publicKey.com.zoe.encrypt(testStr.getBytes());
//
//        System.out.println("encrypted data is: ");
//        System.out.println(bytesToString(encrypted));
//
//        byte[] decrypted = rsa.getPrivateKey().decrypt(encrypted);
//        System.out.println("Decrypting Bytes: " + bytesToString(decrypted));
//        System.out.println("Decrypted String: " + new String(decrypted));
//    }*/
//
//    @Test
//    public void rsa2() {
//        RSA rsa = new RSA();
//        var encrypted = bytes2Base64String(rsa.getPublicKey().com.zoe.encrypt("1234".getBytes()));
//        var decrypted = new String(rsa.getPrivateKey().decrypt(base64String2Bytes(encrypted)));
//        System.out.println(encrypted);
//        System.out.println(decrypted);
//    }
//
//    @Test
//    public void gson() {
//        MessageEncrypt messageEncrypt = new MessageEncrypt(new DES());
//        var e = messageEncrypt.aes(new Message("111", "111111"));
//        var d = new MessageDecrypt(messageEncrypt.getDes()).aes(e);
//        System.out.println(d);
//    }
//
//    @Test
//    public void testDES() {
//        DES des = new DES("1234");
//        String key = "123213213", com.zoe.message = "helloworld", result = null;
//
//        result = des.com.zoe.encrypt(com.zoe.message);
//        System.out.println(result);
//
//        result = des.decrypt(result);
//
//        System.out.println(result);
//
//    }
//}
