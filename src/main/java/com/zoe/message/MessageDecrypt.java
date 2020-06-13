package com.zoe.message;

import com.google.gson.Gson;
import com.zoe.utils.Utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author zy
 */
public class MessageDecrypt {
    mode mode = com.zoe.message.mode.des;
    private String key;
    Gson gson;

    public MessageDecrypt() {
        gson = new Gson();
        key = "";
    }

    public MessageDecrypt mode(String mode, String key) {
        if ("aes".equals(mode)) {
            this.mode = com.zoe.message.mode.aes;
        }
        if ("des".equals(mode)) {
            this.mode = com.zoe.message.mode.des;

        }
        this.key = key;
        return this;
    }

    public Message decrypt(String decrypted) {
        switch (mode) {
            case aes:
                return aes(decrypted);
            case des:
                break;
            default:
                break;
        }
        return null;
    }

//    public Message des(String encrypted) {
//        String decrypedJson = des.decrypt(encrypted);
//        return gson.fromJson(decrypedJson, Message.class);
//    }

    public Message aes(String encrypted) {
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

}