package com.zoe.client.message;

import com.google.gson.Gson;

/**
 * 实现对称加密的接口
 */
public interface MessageEncryptDecrypt {
    Gson gson = new Gson();

    String encrypt(Message message);

    Message decrypt(String encrypted);

    String getKey();
}
