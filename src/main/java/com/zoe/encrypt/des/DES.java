package com.zoe.encrypt.des;

/**
 *  Data Encryption Standard (DES) Java Implementation
 *  	with Cipher Block Chaining mode
 *
 *  2013-04-02
 *
 *  Jonathan Poltak Samosir     <jpsam3@student.monash.edu>     2271 3603
 *  Goodwin Lam                 <hklam9@student.monash.edu>     2317 6105
 *
 *  All supplementary data gotten from Wikipedia:
 *  http://en.wikipedia.org/wiki/DES_supplementary_material
 */

public class DES
{
    protected final static long MASK_6_BITS     = 0xFC0000000000L;
    protected final static long MASK_32_BITS    = 0xFFFFFFFFL;
    protected final static int  MASK_28_BITS    = 0x0FFFFFFF;
	protected final static int  NUM_OF_ROUNDS   = 16;

    /**
     * Values for the Initial Permutation (IP) step.
     */
    private final static byte[] IP =
    {
        58, 50, 42, 34, 26, 18, 10, 2,
        60, 52, 44, 36, 28, 20, 12, 4,
        62, 54, 46, 38, 30, 22, 14, 6,
        64, 56, 48, 40, 32, 24, 16, 8,
        57, 49, 41, 33, 25, 17, 9,  1,
        59, 51, 43, 35, 27, 19, 11, 3,
        61, 53, 45, 37, 29, 21, 13, 5,
        63, 55, 47, 39, 31, 23, 15, 7
    };

    /**
     * Values for the Final Permutation (FP) step.
     */
    private final static byte[] FP =
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

    private final RoundKeyGenerator keygen;
    private final FeistelFunction feistel;

    public DES()
    {
    	keygen = new RoundKeyGenerator();
    	feistel = new FeistelFunction();
    }

    /**
     * Wrapper for CBCMode(), specifying encryption.
     */
    public long[] CBCEncrypt(long plainTexts[], long key, long IV)
    {
    	return CBCMode(plainTexts, key, IV, true);
    }

    /**
     * Wrapper for CBCMode(), specifying encryption.
     */
    public long[] CBCDecrypt(long cipherTexts[], long key, long IV)
    {
    	return CBCMode(cipherTexts, key, IV, false);
    }

    /**
     * Wrapper for cipher(), specifying encryption.
     */
    public long encrypt(long block, long key)
    {
    	return cipher(block, key, true);
    }

    /**
     * Wrapper for cipher(), specifying decryption.
     */
    public long decrypt(long block, long key)
    {
    	return cipher(block, key, false);
    }


    /**
     * Implements the DES cipher on 64 bit blocks in @param input using @param key in CBC mode.
     * CBC mode encryption or decryption can be specified with @param encrypt.
     *
     * @return An array of 64 bit blocks that have been ciphered through DES CBC mode.
     */
    private long[] CBCMode(long[] inputs, long key, long IV, boolean encrypt)
    {
    	long[] outputs = new long[inputs.length];

    	long xor_val = IV;
    	for (int i = 0; i < inputs.length; i++)
    		if (encrypt)
    		{
    			outputs[i] = encrypt(inputs[i] ^ xor_val, key);
    			xor_val = outputs[i];
    		} else
    		{
    			outputs[i] = decrypt(inputs[i], key) ^ xor_val;
    			xor_val = inputs[i];
    		}

    	return outputs;
    }

    /**
     * Does the main part of the DES algorithm (encrypting or decrypting).
     * Operates on a 64 bit @param block and generates round keys from 64 bit @param key.
     * Encryption or decryption can be specified by @param encrypt.
     *
     * @return A 64 bit block of primitive type long.
     */
    private long cipher(long block, long key, boolean encrypt)
    {
    	long[] roundKeys = keygen.generateRoundKeys(key);
    	block = initialPermutation(block);

    	int leftHalf = (int) (block >> 32);		// get 32 MSBs
    	int rightHalf = (int) block;			// get 32 LSBs
    	int FOutput;

    	// does all 16 rounds of DES
    	for (int i = 0; i < DES.NUM_OF_ROUNDS; i++)
    	{
    		if (encrypt)
    			FOutput = feistel.F(rightHalf, roundKeys[i]);
    		else
    			FOutput = feistel.F(rightHalf, roundKeys[(DES.NUM_OF_ROUNDS-1)-i]);

    		// XOR the F function output and the left half
    		leftHalf ^= FOutput;

    		// swaps left and right halves using the XOR swapping algorithm
    		leftHalf ^= rightHalf;
    		rightHalf ^= leftHalf;
    		leftHalf ^= rightHalf;
    	}

    	// reconstruct a 64 bit block from the two halves (which get swapped)
    	long joinedHalves = ((rightHalf & MASK_32_BITS) << 32 | (leftHalf & MASK_32_BITS));

    	return finalPermutation(joinedHalves);
    }

    /**
     * @param input The 64 bit block to be ciphered by DES.
     * @return A 64 bit permutation of @param input according to table IP.
     */
    private long initialPermutation(long input)
    {
    	return DES.genericPermutation(input, IP, 64);
    }

    /**
     * @param input The 64 bit block ciphered by DES block cipher.
     * @return A 64 bit permutation of @param input according to table FP.
     */
    private long finalPermutation(long input)
    {
    	return DES.genericPermutation(input, FP, 64);
    }

    /**
     * Originally had all the permutation functions (S-box, P-box, PermuteChoice, etc.) separate
     * but soon realised they are essentially doing the same function (a permutation) just returning
     * different length bit strings. Hence a generic permutation function made more sense, and is
     * able to differentiate between the permutations through arguments @param indexTable and @param inputLength
     *
     * Given the 64 bit @param input, it works with the @param inputLength LSBs and the specified
     * table in @param indexTable by looping through each bit then swapping the value with the one
     * calculated in the index variable. As it swaps the value, it shifts the bits to the left each
     * iteration
     *
     * @return The 64 bit output containing the result of the permutation from the given table.
     */
    protected static long genericPermutation(long input, byte[] indexTable, int inputLength)
    {
        long output = 0;
        int index;

        for (byte anIndexTable : indexTable) {
            index = inputLength - anIndexTable;
            output = (output << 1) | ((input >> index) & 1);
        }

        return output;
    }
}