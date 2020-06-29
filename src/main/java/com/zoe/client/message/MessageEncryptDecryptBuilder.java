package com.zoe.client.message;

import com.zoe.client.message.impl.DesMessageEncryptDecrypt;

public class MessageEncryptDecryptBuilder {
    private final MessageEncryptDecrypt messageEncryptDecrypt;

    public MessageEncryptDecryptBuilder(String key) {
        messageEncryptDecrypt = new DesMessageEncryptDecrypt(key);
    }

    public MessageEncryptDecrypt build() {
        return messageEncryptDecrypt;
    }
}
