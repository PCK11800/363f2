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

    private String ErrInvalidSession = "Invalid or expired session token";
    private Random random;
    private byte[] stringBytes;
    private StringBuffer stringBuffer;
    private String hexString;

    //TODO: Find better storage place for tokens
    public HashMap<SecretKey, LocalDateTime> sessionTokens = new HashMap<>();

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

    public boolean tokenExists(String sessionToken)
    {
        SecretKey key = this.stringToKey(sessionToken);

        if (sessionTokens.containsKey(key))
        {
            return true;
        }
        else return false;
    }


    /**
     * A token is valid if it exists and is less than 12 hours old.
     * @param sessionToken
     * @return Returns true os a session token is valid.
     */
    public boolean isTokenValid(String sessionToken)
    {
        SecretKey key = this.stringToKey(sessionToken);

        if(sessionTokens.containsKey(key))
        {
            LocalDateTime tokenTime = sessionTokens.get(key);
            LocalDateTime currentTime = LocalDateTime.now();

            //Find the time between when the token was generated an the current time
            Duration duration = Duration.between(tokenTime, currentTime);

            if (duration.toHours() <= 12) //The token will expire after 12 hours
            {
                return true;
            }
            else{
                sessionTokens.remove(key); //Remove the expired sessionToken
                return false;
            }
        }
        else
        {
            return false;
        }
    }


    /**
     * This method creates a new session token
     * @param username The current users' username
     * @return The newly creatred session token
     */
    public String createSessionToken(String username) throws NoSuchAlgorithmException {
        //The sessionToken is a randomly generated string and a username
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        SecureRandom secureRandom = new SecureRandom();
        keyGenerator.init(128, secureRandom);
        SecretKey sessionToken = keyGenerator.generateKey();

        sessionTokens.put(sessionToken, LocalDateTime.now());

        return this.keyToString(sessionToken);
    }


    /**
     * This method is for when a user needs to log out.
     * @param token The current session token
     */
    public void removeSessionToken(String token)
    {
        if (this.isTokenValid(token))
        {
            sessionTokens.remove(this.stringToKey(token));
        }
    }


    /*public static void main(String[] args)
    {
        SessionToken s = new SessionToken();
        String token = s.createSessionToken("Stanci");

        byte[] test = s.generateToken();
        System.out.println(test);
        System.out.println(s.btoString(test));
        System.out.println(s.bytesToString(test));

        //System.out.println(token);

        if (s.isTokenValid(token))
        {
            //System.out.println("The session is valid");
        } else {
            //System.out.println("The session is not valid");
        }

        /*try
        {
            TimeUnit.MINUTES.sleep(1);
        }
        catch (InterruptedException e)
        {
        }

        if (s.isTokenValid(token))
        {
            System.out.println("The session is valid");
        } else {
            System.out.println("The session is has expired");
        }
    }*/
}
