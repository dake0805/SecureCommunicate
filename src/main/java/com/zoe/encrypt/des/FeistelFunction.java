package com.zoe.encrypt.des;

class FeistelFunction
{
    /**
     * Values for the Expansion Permutation (E) step.
     */
    private static final byte[] E =
    {
        32, 1,  2,  3,  4,  5,
        4,  5,  6,  7,  8,  9,
        8,  9,  10, 11, 12, 13,
        12, 13, 14, 15, 16, 17,
        16, 17, 18, 19, 20, 21,
        20, 21, 22, 23, 24, 25,
        24, 25, 26, 27, 28, 29,
        28, 29, 30, 31, 32, 1
    };

    /**
     * Values for the 8 Substitution (S) box tables.
     */
    private final static byte[][][] S_BOX =
    {{
    	{ 14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7 },
		{ 0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8 },
		{ 4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0 },
		{ 15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13 }
    },
    {
        { 15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10 },
		{ 3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5 },
		{ 0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15 },
		{ 13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9 }
    },
    {
        { 10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8 },
		{ 13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1 },
		{ 13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7 },
		{ 1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12 }
    },
    {
        { 7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15 },
		{ 13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9 },
		{ 10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4 },
		{ 3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14 }
    },
    {
        { 2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9 },
		{ 14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6 },
		{ 4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14 },
		{ 11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3 }
    },
    {
        { 12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11 },
		{ 10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8 },
		{ 9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6 },
		{ 4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13 }
    },
    {
        { 4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1 },
		{ 13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6 },
		{ 1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2 },
		{ 6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12 }
    },
    {
        { 13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7 },
		{ 1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2 },
		{ 7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8 },
		{ 2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11}
    }};

    /**
     * Values for the Permutation (P)-Box table.
     */
    private final static byte[] P_BOX =
    {
        16, 7,  20, 21,
        29, 12, 28, 17,
        1,  15, 23, 26,
        5,  18, 31, 10,
        2,  8,  24, 14,
        32, 27, 3,  9,
        19, 13, 30, 6,
        22, 11, 4,  25
    };

	/**
     * Given the 32 bit @param input, words by getting the index offsets specified in the E table.
     * Masks out the bit at the given index and ORs it in the next place in the output 48 bits.
     *
     * @return 48 bits expanded from the 32 bit input. As Java longs are 64 bits, it will leave at
     * least 16 zero bits in the MSB positions.
     */
    private long expansionPermutation(int input)
    {
        return DES.genericPermutation(input, E, 32);
    }

    /**
     * Given the 6 bit @param input, works by masking out the 6th and 1st bits of input (which
     * specifies the row in the S-table), and shifting them to the MSB positions while shifting the
     * remaining 4 bits (which specify the column) to the LSB positions. Given the S-box table
     * formats, the 6 permuted bits will specify an index between 2^0 to (2^6)-1 in the relevant
     * S-box table (specified by @param SBoxNum).
     *
     * @return A byte containing the 4 bit value specified in the relevant S-box table.
     */
    private byte SBoxSubstitution(int SBoxNum, byte input)
    {
    	byte row, col;
    	row = (byte) (((input & 0x20) >> 4) | input & 0x01);
    	col = (byte) ((input & 0x1E) >> 1);

        return S_BOX[SBoxNum][row][col];
    }

    /**
     * Given the 32 bit @param input, works by getting the index offsets specified in the P_BOX
     * table, and masking out the bit at the calculated index. Once the specified bit has been
     * masked out, OR it into the next bit position in the output 32 bits.
     *
     * @return 32 bits of input permuted according to the P_BOX table indices.
     */
    private int PBoxPermutation(int input)
    {
        return (int) DES.genericPermutation(input, P_BOX, 32);
    }

    /**
     * The core function of DES. Does the following:
     *      - expansion permutation step on @param input
     *      - XORs the expanded 48 bits with the 48 bit @param roundKey
     *      - inputs the 48 bit product into the eight S-Boxes, getting a 32 bit output
     *      - inputs the 32 bit product into the P-Box and returns the 32 bit output
     *
     * @return The 32 bit output of the P-Box permutation step.
     */
    int F(int input, long roundKey)
    {
        long output = expansionPermutation(input);
        output ^= roundKey;

        int SBoxOutputs = 0;
        for (int i = 0; i < 8; i++)
        {
            SBoxOutputs <<= 4;
            SBoxOutputs |= SBoxSubstitution(i, (byte) ((output & DES.MASK_6_BITS) >> 42));
            output = output << 6;
        }

        return PBoxPermutation(SBoxOutputs);
    }
}