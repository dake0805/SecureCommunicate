package com.zoe.client.message.impl;

import com.zoe.client.message.Message;
import com.zoe.client.message.MessageEncryptDecrypt;
import com.zoe.encrypt.des.DESInterface;
import com.zoe.utils.Utils;

/**
 * @author zy
 */
public class DesMessageEncryptDecrypt implements MessageEncryptDecrypt {
    private final String key;

    private final DESInterface des;

    public DesMessageEncryptDecrypt(String key) {
        this.key = key;
        this.des = new DESInterface(key);
    }

    @Override
    public String encrypt(Message message) {
        byte[] byteContent = Utils.bytes2Base64String(gson.toJson(message).getBytes()).getBytes();
        byte[] result = des.encrypt(byteContent);
        return Utils.bytes2Base64String(result);
    }

    @Override
    public Message decrypt(String base64ed) {
        byte[] result = Utils.base64String2Bytes(new String(des.decrypt(Utils.base64String2Bytes(base64ed))));
        String s = new String(result);
        return gson.fromJson(s, Message.class);
    }

    @Override
    public String getKey() {
        return key;
    }

}
