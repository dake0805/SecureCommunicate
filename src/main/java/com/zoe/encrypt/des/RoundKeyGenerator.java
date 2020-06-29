package com.zoe.encrypt.des;

class RoundKeyGenerator
{
    /**
     * Values for the Permutation Choice 1 table for the initial permutation of the 56-bit key.
     */
    private final static byte[] PC1 =
    {
        57, 49, 41, 33, 25, 17, 9,
        1,  58, 50, 42, 34, 26, 18,
        10, 2,  59, 51, 43, 35, 27,
        19, 11, 3,  60, 52, 44, 36,
        63, 55, 47, 39, 31, 23, 15,
        7,  62, 54, 46, 38, 30, 22,
        14, 6,  61, 53, 45, 37, 29,
        21, 13, 5,  28, 20, 12, 4
    };

    /**
     * Values for the Permutation Choice 2 table for the contracting permutation step of the key
     * halves.
     */
    private final static byte[] PC2=
    {
        14, 17, 11, 24, 1,  5,
        3,  28, 15, 6,  21, 10,
        23, 19, 12, 4,  26, 8,
        16, 7,  27, 20, 13, 2,
        41, 52, 31, 37, 47, 55,
        30, 40, 51, 45, 33, 48,
        44, 49, 39, 56, 34, 53,
        46, 42, 50, 36, 29, 32
    };

    /**
     * Values for the number of left circular shifts to use on the key halves.
     */
    private final static byte[] CIRCULAR_SHIFTS =
    {
        1, 1, 2, 2,
        2, 2, 2, 2,
        1, 2, 2, 2,
        2, 2, 2, 1
    };


    /**
     * Implements a circular left shift by @param shift bits on a 28 bit @param input.
     *
     * @return 28 bit string that has been circular shifted left @param shift bits.
     */
    private int circularLeftShift(int input, int shift)
    {
    	return ((input << shift) & DES.MASK_28_BITS) | (input >> (28 - shift));
    }

    /**
     * Takes @param input a 64 bit key.
     * @return A 56 bit selection of the original 64 bits.
     */
    private long permutationChoice1(long input)
    {
    	return DES.genericPermutation(input, PC1, 64);
    }

    /**
     * Takes @param input a 56 bit selection of the original key.
     * @return A 48 bit selection of the 56 bit @param input.
     */
    private long permutationChoice2(long input)
    {
    	return DES.genericPermutation(input, PC2, 56);
    }

    /**
     * Generates all the round keys from 64 bit specified key @param input.
     * @return An array of 16 longs representing all the 48 bit round keys.
     */
    long[] generateRoundKeys(long input)
    {
    	input = permutationChoice1(input);
    	int halfA = (int) (input >> 28);			// gets 28 MSBs
    	int halfB = (int) (input & DES.MASK_28_BITS);	// masks 28 LSBs

    	long[] roundKeys = new long[DES.NUM_OF_ROUNDS];

    	// generates all the 58 bit round keys for each round of DES and stores them in an array
    	for (int i = 0; i < DES.NUM_OF_ROUNDS; i++)
    	{
    		halfA = circularLeftShift(halfA, CIRCULAR_SHIFTS[i]);
    		halfB = circularLeftShift(halfB, CIRCULAR_SHIFTS[i]);

    		long joinedHalves = ((halfA & DES.MASK_32_BITS) << 28) | (halfB & DES.MASK_32_BITS);
    		roundKeys[i] = permutationChoice2(joinedHalves);
    	}
    	return roundKeys;
    }
}