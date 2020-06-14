package com.zoe.client.message.impl;

import com.zoe.client.message.Message;
import com.zoe.client.message.MessageEncryptDecrypt;
import com.zoe.utils.Utils;

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
public class AesMessageEncryptDecrypt implements MessageEncryptDecrypt {
    private final String key;

    public AesMessageEncryptDecrypt(String key) {
        this.key = key;
    }

    @Override
    public String encrypt(Message message) {
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

    @Override
    public Message decrypt(String encrypted) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            byte[] result = cipher.doFinal(Utils.base64String2Bytes(encrypted));
            return gson.fromJson(new String(result), Message.class);
        } catch (Exception e) {
            //
        }
        return null;
    }

    @Override
    public String getKey() {
        return key;
    }
}
