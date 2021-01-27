package server;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Base64;

public class CredentialNegotiation {
    private Signature signature;
    private byte[] sign;
    private int serverChallenge;

    public CredentialNegotiation() {

    }

    public String negotiationSign(String text, PrivateKey privateKey)
            throws InvalidKeyException, NoSuchAlgorithmException, SignatureException {
        signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(text.getBytes(StandardCharsets.UTF_8));
        sign = signature.sign();
        return Base64.getEncoder().encodeToString(sign);
    }

    public boolean verifySignature(String text, String signedText, PublicKey publicKey)
            throws NoSuchAlgorithmException
    {
        signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(publicKey);
        signature.update(text.getBytes(StandardCharsets.UTF_8));
        sign = Base64.getDecoder().decode(signedText);
        return signature.verify(sign);
    }

    public String authenticationChallenge(String X) 
    {
        return this.negotiationSign(X, serverPrivateKey);
    }

    public int getServerauthenticationChallenge()
    {
        serverChallenge = secureRandom.nextInt();
        return serverChallenge;
    }

    public Boolean serverAuthentication(String signedByClient, PublicKey clientPublicKey)
    {
        if (this.verifySignature(Integer.toString(serverChallenge), signedByClient, clientPublicKey)) {
            return true;
        } else
            return false;
    }
   
}
