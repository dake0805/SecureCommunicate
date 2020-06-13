package com.zoe.encrypt.rsa;

import java.io.Serializable;
import java.math.BigInteger;

public class PublicKey implements Serializable {
    private final BigInteger n;
    private final BigInteger e;

    public PublicKey(BigInteger n, BigInteger e) {
        this.n = n;
        this.e = e;
    }

    public byte[] encrypt(byte[] message) {
        return (new BigInteger(message)).modPow(e, n).toByteArray();
    }

    @Override
    public String toString() {
        return "PublicKey{" +
                "n=" + n +
                ", e=" + e +
                '}';
    }
}
