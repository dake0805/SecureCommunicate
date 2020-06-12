package utils;

import org.apache.commons.lang3.RandomStringUtils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class Utils {
    public static String bytesToString(byte[] bytes) {
        StringBuilder str = new StringBuilder();
        for (byte b : bytes) {
            str.append(b);
        }
        return str.toString();
    }


    public static String binToUTF(String bin) {
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
}
