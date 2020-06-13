package encrypt;

import com.google.gson.Gson;
import encrypt.des.DES;
import message.Message;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static utils.Utils.bytes2Base64String;

public class MessageEncrypt {
    private byte[] aesKey;
    private DES des;
    Gson gson;


    public MessageEncrypt(DES des) {
        this.des = des;
        gson = new Gson();
    }

    public MessageEncrypt(byte[] aesKey) {
        this.aesKey = aesKey;
    }

    public String des(Message message) {
        return des.encrypt(gson.toJson(message));
    }

    public DES getDes() {
        return des;
    }


    public String aes(Message message) {
        try {
            //初始化密钥 SecretKeySpec(byte[] key, String algorithm)
            SecretKeySpec key = new SecretKeySpec(aesKey, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            //初始化 key要求是16位 16个字节=16*8=128bit 128位
            cipher.init(Cipher.ENCRYPT_MODE, key);

            byte[] byteContent = gson.toJson(message).getBytes();
            byte[] result = cipher.doFinal(byteContent);
            return bytes2Base64String(result);

        } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return "";
    }

}
