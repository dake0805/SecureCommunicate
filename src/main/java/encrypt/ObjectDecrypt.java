package encrypt;

import com.google.gson.Gson;
import encrypt.des.DES;
import encrypt.rsa.PrivateKey;
import encrypt.rsa.PublicKey;
import encrypt.rsa.RSA;

public class ObjectDecrypt {
    private final DES des;
    private RSA rsa;
    private PrivateKey privateKey;
    private PublicKey publicKey;
    Gson gson;

    public ObjectDecrypt(DES des) {
        this.des = des;
        gson = new Gson();
    }

    public Object des(String encrypted) {
        return gson.fromJson(des.decrypt(encrypted), Object.class);
    }

    public Object rsa(PrivateKey privateKey, String encrypted) {
        if (privateKey == null) {
            return "";
        }
        return gson.fromJson(new String(privateKey.decrypt(encrypted.getBytes())), Object.class);
    }

    public DES getDes() {
        return des;
    }

}
