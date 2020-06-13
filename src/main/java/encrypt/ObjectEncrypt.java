package encrypt;

import com.google.gson.Gson;
import encrypt.des.DES;
import encrypt.rsa.PrivateKey;
import encrypt.rsa.PublicKey;
import encrypt.rsa.RSA;

public class ObjectEncrypt {
    private final DES des;
    private RSA rsa;
    private PrivateKey privateKey;
    private PublicKey publicKey;
    Gson gson;


    ObjectEncrypt(DES des) {
        this.des = des;
        gson = new Gson();
    }

    public String des(Object message) {
        return des.encrypt(gson.toJson(message));
    }

    public String rsa(PublicKey publicKey, Object message) {
        if (publicKey == null) {
            return "";
        }
        return new String(publicKey.encrypt(gson.toJson(message).getBytes()));
    }

}
