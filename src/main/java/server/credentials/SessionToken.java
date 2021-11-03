package server.credentials;

import java.beans.Encoder;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class SessionToken {

    private String invalidSession = "Invalid or expired session token";

    private SecretKey sessionToken;
    private LocalDateTime tokenTime;

    public String keyToString(SecretKey token)
    {
        return Base64.getEncoder().encodeToString(token.getEncoded());
    }

    public SecretKey stringToKey(String token)
    {
        byte[] decodedKey = Base64.getDecoder().decode(token);
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
    }

    public SessionToken()
    {
    }

    /**
     * A token is valid if it exists and is less than 12 hours old.
     * @return Returns true os a session token is valid.
     */
    public boolean isTokenValid()
    {
        LocalDateTime currentTime = LocalDateTime.now();

        //Find the time between when the token was generated an the current time
        Duration duration = Duration.between(tokenTime, currentTime);

        if (duration.toHours() <= 1) //The token will expire after 1 hour
        {
            return true;
        }
        else{
            sessionToken = null;
            tokenTime = null;
            //Remove the expired sessionToken
            return false;
        }
    }


    /**
     * This method creates a new session token
     * @return The newly created session token
     */
    public String createSessionTokenString() throws NoSuchAlgorithmException {
        //The sessionToken is a randomly generated string and a username
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        SecureRandom secureRandom = new SecureRandom();
        keyGenerator.init(128, secureRandom);
        SecretKey sessionToken = keyGenerator.generateKey();

        //sessionTokens.put(sessionToken, LocalDateTime.now());
        tokenTime = LocalDateTime.now();
        this.sessionToken = sessionToken;

        return this.keyToString(sessionToken);
    }

    public SecretKey returnSessionTokenKey()
    {
        return sessionToken;
    }

    public String returnSessionTokenString()
    {
        return this.keyToString(sessionToken);
    }


    /**
     * This method is for when a user needs to log out.
     */
    public void removeSessionToken()
    {
        if (this.isTokenValid())
        {
            sessionToken = null;
            tokenTime = null;
        }
    }

}
