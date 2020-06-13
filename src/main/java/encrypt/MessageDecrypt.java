package encrypt;

import com.google.gson.Gson;
import encrypt.des.DES;
import message.Message;
import utils.Utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author zy
 */
public class MessageDecrypt {
    private DES des;
    Gson gson;

    private byte[] aesKey;

    public MessageDecrypt(DES des) {
        this.des = des;
        gson = new Gson();
    }

    public MessageDecrypt(byte[] aesKey) {
        this.aesKey = aesKey;
    }

    public Message des(String encrypted) {
        String decrypedJson = des.decrypt(encrypted);
        return gson.fromJson(decrypedJson, Message.class);
    }

    public DES getDes() {
        return des;
    }

    public Message aes(String encrypted) {
        try {
            SecretKeySpec key = new SecretKeySpec(aesKey, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] result = cipher.doFinal(Utils.base64String2Bytes(encrypted));
            return gson.fromJson(new String(result), Message.class);
        } catch (Exception e) {
            //
        }
        return null;
    }


}
