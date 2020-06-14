package com.zoe.client.message.impl;

import com.zoe.client.message.Message;
import com.zoe.client.message.MessageEncryptDecrypt;

public class DesMessageEncryptDecrypt implements MessageEncryptDecrypt {
    String key;

    public DesMessageEncryptDecrypt(String key) {
        this.key = key;
    }

    @Override
    public String encrypt(Message message) {
        return null;
    }

    @Override
    public Message decrypt(String encrypted) {
        return null;
    }

    @Override
    public String getKey() {
        return key;
    }
}
