package Client;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class EncryptSessionKey {
    PublicKey serverKey;
    byte[] session;

    public EncryptSessionKey(PublicKey serverKey) {
        this.serverKey = serverKey;
    }

    public byte[] encryptSessionKey(String sessionToken) throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException
    {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, serverKey);
        cipher.update(sessionToken.getBytes());
        return cipher.doFinal();        
    }
    
}
