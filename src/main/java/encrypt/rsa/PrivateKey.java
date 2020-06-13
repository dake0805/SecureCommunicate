package encrypt.rsa;

import java.math.BigInteger;

/**
 * @author zy
 */
public class PrivateKey {
    private final BigInteger n;
    private final BigInteger d;

    public PrivateKey(BigInteger n, BigInteger d) {
        this.n = n;
        this.d = d;
    }

    protected byte[] decrypt(byte[] message) {
        return (new BigInteger(message)).modPow(d, n).toByteArray();
    }
}
