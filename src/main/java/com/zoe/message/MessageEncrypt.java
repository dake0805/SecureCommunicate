package com.zoe.message;

import com.google.gson.Gson;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static com.zoe.utils.Utils.bytes2Base64String;

/**
 * @author zy
 */
public class MessageEncrypt {
    mode mode = com.zoe.message.mode.des;
    private String key;
    Gson gson;


    public MessageEncrypt() {
        gson = new Gson();
        key = "";
    }

    public MessageEncrypt mode(String mode, String key) {
        if ("aes".equals(mode)) {
            this.mode = com.zoe.message.mode.aes;
        }
        if ("des".equals(mode)) {
            this.mode = com.zoe.message.mode.des;
        }
        this.key = key;
        return this;
    }

    public String encrypt(Message message) {
        switch (mode) {
            case aes:
                return aes(message);
            case des:
                break;
            default:
                break;
        }
        return "";
    }


    private String aes(Message message) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            byte[] byteContent = gson.toJson(message).getBytes();
            byte[] result = cipher.doFinal(byteContent);
            return bytes2Base64String(result);
        } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getKey() {
        return this.key;
    }
}

