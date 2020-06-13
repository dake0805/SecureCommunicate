package encrypt.des;

import utils.Utils;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class DES {

    public static int KEY_LENGTH = 64;

    private final String key;

    private static int[] PC1 =
            {
                    57, 49, 41, 33, 25, 17, 9,
                    1, 58, 50, 42, 34, 26, 18,
                    10, 2, 59, 51, 43, 35, 27,
                    19, 11, 3, 60, 52, 44, 36,
                    63, 55, 47, 39, 31, 23, 15,
                    7, 62, 54, 46, 38, 30, 22,
                    14, 6, 61, 53, 45, 37, 29,
                    21, 13, 5, 28, 20, 12, 4
            };

    // First index is garbage value, loops operating on this should start with index = 1
    private static int[] KEY_SHIFTS =
            {
                    0, 1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1
            };

    private static int[] PC2 =
            {
                    14, 17, 11, 24, 1, 5,
                    3, 28, 15, 6, 21, 10,
                    23, 19, 12, 4, 26, 8,
                    16, 7, 27, 20, 13, 2,
                    41, 52, 31, 37, 47, 55,
                    30, 40, 51, 45, 33, 48,
                    44, 49, 39, 56, 34, 53,
                    46, 42, 50, 36, 29, 32
            };


    private static int[][] s1 = {
            {14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7},
            {0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8},
            {4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0},
            {15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13}
    };

    private static int[][] s2 = {
            {15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10},
            {3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5},
            {0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15},
            {13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9}
    };

    private static int[][] s3 = {
            {10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8},
            {13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1},
            {13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7},
            {1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12}
    };

    private static int[][] s4 = {
            {7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15},
            {13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9},
            {10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4},
            {3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14}
    };

    private static int[][] s5 = {
            {2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9},
            {14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6},
            {4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14},
            {11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3}
    };

    private static int[][] s6 = {
            {12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11},
            {10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8},
            {9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6},
            {4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13}
    };

    private static int[][] s7 = {
            {4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1},
            {13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6},
            {1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2},
            {6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12}
    };

    private static int[][] s8 = {
            {13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7},
            {1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2},
            {7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8},
            {2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11}
    };

    private static int[][][] s = {s1, s2, s3, s4, s5, s6, s7, s8};

    private static int[] g =
            {
                    32, 1, 2, 3, 4, 5,
                    4, 5, 6, 7, 8, 9,
                    8, 9, 10, 11, 12, 13,
                    12, 13, 14, 15, 16, 17,
                    16, 17, 18, 19, 20, 21,
                    20, 21, 22, 23, 24, 25,
                    24, 25, 26, 27, 28, 29,
                    28, 29, 30, 31, 32, 1
            };


    static int[] p =
            {
                    16, 7, 20, 21,
                    29, 12, 28, 17,
                    1, 15, 23, 26,
                    5, 18, 31, 10,
                    2, 8, 24, 14,
                    32, 27, 3, 9,
                    19, 13, 30, 6,
                    22, 11, 4, 25
            };

    static int[] IP =
            {
                    58, 50, 42, 34, 26, 18, 10, 2,
                    60, 52, 44, 36, 28, 20, 12, 4,
                    62, 54, 46, 38, 30, 22, 14, 6,
                    64, 56, 48, 40, 32, 24, 16, 8,
                    57, 49, 41, 33, 25, 17, 9, 1,
                    59, 51, 43, 35, 27, 19, 11, 3,
                    61, 53, 45, 37, 29, 21, 13, 5,
                    63, 55, 47, 39, 31, 23, 15, 7
            };

    static int[] IPi =
            {
                    40, 8, 48, 16, 56, 24, 64, 32,
                    39, 7, 47, 15, 55, 23, 63, 31,
                    38, 6, 46, 14, 54, 22, 62, 30,
                    37, 5, 45, 13, 53, 21, 61, 29,
                    36, 4, 44, 12, 52, 20, 60, 28,
                    35, 3, 43, 11, 51, 19, 59, 27,
                    34, 2, 42, 10, 50, 18, 58, 26,
                    33, 1, 41, 9, 49, 17, 57, 25
            };

    private long[] K;

    public DES() {
        // First index is garbage value, loops operating on this should start with index = 1
        K = new long[17];
        //todo bugs
        key = Utils.generateDesKey();
        System.err.println(key);
    }

    public DES(String key) {
        // First index is garbage value, loops operating on this should start with index = 1
        K = new long[17];
        this.key = key;
        System.err.println(key);
    }

    private static String binToHex(String bin) {

        BigInteger b = new BigInteger(bin, 2);
        String ciphertext = b.toString(16);

        return ciphertext;
    }

    private static String hexToBin(String hex) {

        BigInteger b = new BigInteger(hex, 16);
        String bin = b.toString(2);

        return bin;
    }

    private static String binToUTF(String bin) {
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

    private static String utfToBin(String utf) {
        // Convert to binary
        byte[] bytes;
        bytes = utf.getBytes(StandardCharsets.UTF_8);
        StringBuilder bin = new StringBuilder();
        for (int aByte : bytes) {
            int value = aByte;
            for (int j = 0; j < 8; j++) {
                bin.append((value & 128) == 0 ? 0 : 1);
                value <<= 1;
            }

        }
        return bin.toString();
    }

    /**
     * Encrypt a string message with the DES block cipher
     *
     * @param key
     * @param plaintext
     * @return
     */
    public String encrypt(String plaintext) {

        plaintext = utfToBin(plaintext);

        // Build the key schedule
        buildKeySchedule(key.hashCode());

        StringBuilder binPlaintext = new StringBuilder(plaintext);

        // Add padding if necessary
        int remainder = binPlaintext.length() % 64;
        if (remainder != 0) {
            for (int i = 0; i < (64 - remainder); i++) {
                binPlaintext.append("0");
            }
        }

        // Separate binary plaintext into blocks
        String[] binPlaintextBlocks = new String[binPlaintext.length() / 64];
        int offset = 0;
        for (int i = 0; i < binPlaintextBlocks.length; i++) {
            binPlaintextBlocks[i] = binPlaintext.substring(offset, offset + 64);
            offset += 64;
        }

        String[] binCipherTextBlocks = new String[binPlaintext.length() / 64];

        // Encrypt the blocks
        for (int i = 0; i < binCipherTextBlocks.length; i++)
            try {
                binCipherTextBlocks[i] = encryptBlock(binPlaintextBlocks[i]);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        // Build the ciphertext binary string from the blocks
        StringBuilder binCipherText = new StringBuilder();
        for (String binCipherTextBlock : binCipherTextBlocks) {
            binCipherText.append(binCipherTextBlock);
        }

        // Destroy key schedule
        Arrays.fill(K, 0);


        return binToHex(binCipherText.toString());
    }

    /**
     * Decrypt a string message with the DES block cipher
     *
     * @param key           : String - the key to decrypt with
     * @param hexCipherText : String - Hex string to decrypt
     * @return Plaintext message string
     */
    public String decrypt(String hexCipherText) {

        // Build the key schedule
        buildKeySchedule(key.hashCode());

        String binPlaintext = null;

        binPlaintext = hexToBin(hexCipherText);

        // Add padding if necessary
        int remainder = binPlaintext.length() % 64;
        if (remainder != 0) {
            for (int i = 0; i < (64 - remainder); i++)
                binPlaintext = binPlaintext + "0";
        }

        // Separate binary plaintext into blocks
        String[] binPlaintextBlocks = new String[binPlaintext.length() / 64];
        int offset = 0;
        for (int i = 0; i < binPlaintextBlocks.length; i++) {
            binPlaintextBlocks[i] = binPlaintext.substring(offset, offset + 64);
            offset += 64;
        }

        String[] binCiphertextBlocks = new String[binPlaintext.length() / 64];

        // Encrypt the blocks
        for (int i = 0; i < binCiphertextBlocks.length; i++) {
            try {
                binCiphertextBlocks[i] = decryptBlock(binPlaintextBlocks[i]);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        // Build the ciphertext binary string from the blocks
        StringBuilder binCiphertext = new StringBuilder();
        for (String binCiphertextBlock : binCiphertextBlocks) {
            binCiphertext.append(binCiphertextBlock);
        }

        // Destroy key schedule
        Arrays.fill(K, 0);

        return binCiphertext.toString();
    }

    public String encryptBlock(String plaintextBlock) throws Exception {
        int length = plaintextBlock.length();
        if (length != 64)
            throw new RuntimeException("Input block length is not 64 bits!");

        //Initial permutation
        String out = "";
        for (int i = 0; i < IP.length; i++) {
            out = out + plaintextBlock.charAt(IP[i] - 1);
        }

        String mL = out.substring(0, 32);
        String mR = out.substring(32);

        for (int i = 0; i < 16; i++) {

            // 48-bit current key
            String curKey = Long.toBinaryString(K[i + 1]);
            while (curKey.length() < 48)
                curKey = "0" + curKey;

            // Get 32-bit result from f with m1 and ki
            String fResult = f(mR, curKey);

            // XOR m0 and f
            long f = Long.parseLong(fResult, 2);
            long cmL = Long.parseLong(mL, 2);

            long m2 = cmL ^ f;
            String m2String = Long.toBinaryString(m2);

            while (m2String.length() < 32)
                m2String = "0" + m2String;

            mL = mR;
            mR = m2String;
        }

        String in = mR + mL;
        String output = "";
        for (int i = 0; i < IPi.length; i++) {
            output = output + in.charAt(IPi[i] - 1);
        }

        return output;
    }

    public String decryptBlock(String plaintextBlock) throws RuntimeException {
        int length = plaintextBlock.length();
        if (length != 64)
            throw new RuntimeException("Input block length is not 64 bits!");

        //Initial permutation
        String out = "";
        for (int i = 0; i < IP.length; i++) {
            out = out + plaintextBlock.charAt(IP[i] - 1);
        }

        String mL = out.substring(0, 32);
        String mR = out.substring(32);

        for (int i = 16; i > 0; i--) {

            // 48-bit current key
            String curKey = Long.toBinaryString(K[i]);
            while (curKey.length() < 48)
                curKey = "0" + curKey;

            // Get 32-bit result from f with m1 and ki
            String fResult = f(mR, curKey);

            // XOR m0 and f
            long f = Long.parseLong(fResult, 2);
            long cmL = Long.parseLong(mL, 2);

            long m2 = cmL ^ f;
            String m2String = Long.toBinaryString(m2);

            while (m2String.length() < 32)
                m2String = "0" + m2String;

            mL = mR;
            mR = m2String;
        }

        String in = mR + mL;
        String output = "";
        for (int i = 0; i < IPi.length; i++) {
            output = output + in.charAt(IPi[i] - 1);
        }

        return output;
    }

    /**
     * Hash Function from user <b>sfussenegger</b> on stackoverflow
     *
     * @param string : String to hash
     * @return 64-bit long hash value
     * @source http://stackoverflow.com/questions/1660501/what-is-a-good-64bit-hash-function-in-java-for-textual-strings
     */

    // adapted from String.hashCode()
    public static long hash(String string) {
        long h = 1125899906842597L; // prime
        int len = string.length();

        for (int i = 0; i < len; i++) {
            h = 31 * h + string.charAt(i);
        }
        return h;
    }

    public void buildKeySchedule(long key) {

        // Convert long value to 64bit binary string
        String binKey = Long.toBinaryString(key);

        // Add leading zeros if not at key length for ease of computations
        while (binKey.length() < 64)
            binKey = "0" + binKey;

        // For the 56-bit permuted key
        String binKey_PC1 = "";

        // Apply Permuted Choice 1 (64 -> 56 bit)
        for (int i = 0; i < PC1.length; i++)
            binKey_PC1 = binKey_PC1 + binKey.charAt(PC1[i] - 1);

        String sL, sR;
        int iL, iR;

        // Split permuted string in half | 56/2 = 28
        sL = binKey_PC1.substring(0, 28);
        sR = binKey_PC1.substring(28);

        // Parse binary strings into integers for shifting
        iL = Integer.parseInt(sL, 2);
        iR = Integer.parseInt(sR, 2);

        // Build the keys (Start at index 1)
        for (int i = 1; i < K.length; i++) {

            // Perform left shifts according to key shift array
            iL = Integer.rotateLeft(iL, KEY_SHIFTS[i]);
            iR = Integer.rotateLeft(iR, KEY_SHIFTS[i]);

            // Merge the two halves
            long merged = ((long) iL << 28) + iR;

            // 56-bit merged
            String sMerged = Long.toBinaryString(merged);

            // Fix length if leading zeros absent
            while (sMerged.length() < 56)
                sMerged = "0" + sMerged;

            // For the 56-bit permuted key
            String binKey_PC2 = "";

            // Apply Permuted Choice 2 (56 -> 48 bit)
            for (int j = 0; j < PC2.length; j++)
                binKey_PC2 = binKey_PC2 + sMerged.charAt(PC2[j] - 1);

            // Set the 48-bit key
            K[i] = Long.parseLong(binKey_PC2, 2);
        }
    }


    /**
     * Feistel function in DES algorithm specified in FIPS Pub 46
     *
     * @param mi  : String - 32-bit message binary string
     * @param key : String - 48-bit key binary string
     * @return 32-bit output string
     */
    public static String f(String mi, String key) {

        // Expansion function g (named E in fips pub 46)
        String gMi = "";
        for (int i = 0; i < g.length; i++) {
            gMi = gMi + mi.charAt(g[i] - 1);
        }

        long m = Long.parseLong(gMi, 2);
        long k = Long.parseLong(key, 2);

        // XOR expanded message block and key block (48 bits)
        Long result = m ^ k;

        String bin = Long.toBinaryString(result);
        // Making sure the string is 48 bits
        while (bin.length() < 48) {
            bin = "0" + bin;
        }

        // Split into eight 6-bit strings
        String[] sin = new String[8];
        for (int i = 0; i < 8; i++) {
            sin[i] = bin.substring(0, 6);
            bin = bin.substring(6);
        }


        // Do S-Box calculations
        String[] sout = new String[8];
        for (int i = 0; i < 8; i++) {
            int[][] curS = s[i];
            String cur = sin[i];

            // Get binary values
            int row = Integer.parseInt(cur.charAt(0) + "" + cur.charAt(5), 2);
            int col = Integer.parseInt(cur.substring(1, 5), 2);

            // Do S-Box table lookup
            sout[i] = Integer.toBinaryString(curS[row][col]);

            // Make sure the string is 4 bits
            while (sout[i].length() < 4)
                sout[i] = "0" + sout[i];

        }

        // Merge S-Box outputs into one 32-bit string
        String merged = "";
        for (int i = 0; i < 8; i++) {
            merged = merged + sout[i];
        }

        // Apply Permutation P
        String mergedP = "";
        for (int i = 0; i < p.length; i++) {
            mergedP = mergedP + merged.charAt(p[i] - 1);
        }

        return mergedP;
    }

    public static void main(String[] args) {
    }

}

