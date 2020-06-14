package com.zoe.encrypt.rsa;


import java.math.BigInteger;
import java.util.Random;

public class RSA {
    private int bitLength = 1024;
    private PublicKey publicKey;
    private PrivateKey privateKey;

    public RSA() {
        generateKey();
    }

    public RSA(int bitLength) {
        this.bitLength = bitLength;
        this.generateKey();
    }

//    public RSA(PublicKey publicKey) {
//        this.publicKey = publicKey;
//    }

    private void generateKey() {
        Random r = new Random();
        BigInteger p = BigInteger.probablePrime(bitLength, r);
        BigInteger q = BigInteger.probablePrime(bitLength, r);
        BigInteger n = p.multiply(q);
        BigInteger phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        BigInteger e = BigInteger.probablePrime(bitLength / 2, r);
        //下面这个循环应该进不去
        //todo
        while (phi.gcd(e).compareTo(BigInteger.ONE) > 0 && e.compareTo(phi) < 0) {
            e.add(BigInteger.ONE);
        }
        BigInteger d = e.modInverse(phi);
        this.publicKey = new PublicKey(n, e);
        this.privateKey = new PrivateKey(n, d);
    }

//    public static void main(String[] args) throws IOException {
//        while (true) {
//            new RSA();
//        }
//    }

//    public byte[] com.zoe.encrypt(byte[] com.zoe.client.message) {
//        return publicKey.com.zoe.encrypt(com.zoe.client.message);
//    }

//    public byte[] decrypt(byte[] com.zoe.client.message) {
//        return privateKey.decrypt(com.zoe.client.message);
//    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }
}