package com.zoe.encrypt.des;

import java.nio.ByteBuffer;

/**
 * Class to provide a simple user interface to the DES algorithm.
 */
public class DESInterface {

    private final long key;

    private final DES des = new DES();

    public DESInterface(String keyStr) {
        this.key = getKey(keyStr);
    }

    public byte[] encrypt(byte[] text) {
        long[] blocks = splitInputIntoBlocks(text);
        long[] cipherTexts = new long[blocks.length];
        for (int i = 0; i < blocks.length; i++) {
            cipherTexts[i] = des.encrypt(blocks[i], key);
        }
        return longArrayToBytes(cipherTexts);
    }

    public byte[] decrypt(byte[] bytes) {
        long[] plainTexts = new long[bytes.length / 8];
        long[] cipherTexts = bytesToLongArray(bytes);
        for (int i = 0; i < cipherTexts.length; i++) {
            plainTexts[i] = des.decrypt(cipherTexts[i], key);
        }
        return longArrayToBytes(plainTexts);
    }

    /**
     * Splits the input bytes into blocks of 64 bits.
     *
     * @param input The input text as a byte array.
     * @return An array of longs, representing the 64 bit blocks.
     */
    private long[] splitInputIntoBlocks(byte[] input) {
        long[] blocks = new long[input.length / 8 + 1];

        for (int i = 0, j = -1; i < input.length; i++) {
            if (i % 8 == 0) {
                j++;
            }
            blocks[j] <<= 8;
            blocks[j] |= input[i];
        }

        return blocks;
    }

    /**
     * Gets a key from @param reader and formats it correctly.
     *
     * @return A 64 bit key as type long. If the input is greater than 64 bits, it will be
     * truncated. If less than 64 bits, it will be left-padded with 0s.
     */
    private long getKey(String key) {
        byte[] keyBytes;
        long key64 = 0;

        if (key.length() > 8) {
            key = key.substring(0, 8);
        }

        keyBytes = key.getBytes();

        for (byte keyByte : keyBytes) {
            key64 <<= 8;
            key64 |= keyByte;
        }

        return key64;
    }


    private byte[] longArrayToBytes(long[] x) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES * x.length);
        for (long l : x) {
            buffer.putLong(l);
        }
        return buffer.array();
    }

    private long[] bytesToLongArray(byte[] bytes) {
        long[] longs = new long[bytes.length / 8];
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        for (int i = 0; i < longs.length; i++) {
            longs[i] = buffer.getLong();
        }
        return longs;
    }
}