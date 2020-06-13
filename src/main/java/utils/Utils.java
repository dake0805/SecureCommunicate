package utils;

import com.google.gson.Gson;
import encrypt.ObjectEncrypt;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.UnsupportedEncodingException;

public class Utils {
    public static String bytesToString(byte[] bytes) {
        StringBuilder str = new StringBuilder();
        for (byte b : bytes) {
            str.append(b);
        }
        return str.toString();
    }


    public static String binToUtf(String bin) {
        // Convert back to String
        byte[] ciphertextBytes = new byte[bin.length() / 8];
        String ciphertext = null;
        for (int j = 0; j < ciphertextBytes.length; j++) {
            String temp = bin.substring(0, 8);
            byte b = (byte) Integer.parseInt(temp, 2);
            ciphertextBytes[j] = b;
            bin = bin.substring(8);
        }

        try {
            ciphertext = new String(ciphertextBytes, "utf-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return ciphertext.trim();
    }

    public static String generateDesKey() {
        return RandomStringUtils.randomAscii(8);
    }

    public static String ObjectToJson(Object o) {
        return new Gson().toJson(o);
    }
}
