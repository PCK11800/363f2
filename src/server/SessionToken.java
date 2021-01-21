package server;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class SessionToken {

    private String ErrInvalidSession = "Invalid or expired session token";
    private Random random;
    private byte[] stringBytes;
    private StringBuffer stringBuffer;
    private String hexString;

    //Find better storage place for tokens
    public HashMap<String, LocalDateTime> sessionTokens = new HashMap<>();


    public SessionToken()
    {
    }
    
    public boolean isTokenValid(String sessionToken)
    {
        if(sessionTokens.containsKey(sessionToken))
        {
            LocalDateTime tokenTime = sessionTokens.get(sessionToken);
            LocalDateTime currentTime = LocalDateTime.now();

            //Find the time between when the token was generate an the current time 
            Duration duration = Duration.between(tokenTime, currentTime); 

            if (duration.toHours() <= 12) //The token will expire after 12 hours 
            {
                return true;
            }
            else{
                sessionTokens.remove(sessionToken); //Remove the expired sessionToken
                return false;
            }
        }
        else 
        {
            return false; 
        }
    }

    public String createSessionToken(String username)
    {
        //The sessionToken is a randomly generated string and a username
        String sessionToken = generateToken() + username; 
        sessionTokens.put(sessionToken, LocalDateTime.now());

        return sessionToken;
    }

    public void removeSessionToken(String token)
    {
        if (this.isTokenValid(token))
        {
            sessionTokens.remove(token);
        }
    }

    private String generateToken()
    {
        random = new SecureRandom();
        stringBytes = new byte[8];
        random.nextBytes(stringBytes);

        return this.bytesToString(stringBytes);
    }

    private String bytesToString(byte[] bytes)
    {
        stringBuffer = new StringBuffer();
        for (byte b : bytes) 
        {
            hexString = Integer.toHexString(0xff & b);
            if (hexString.length() == 1)
            {
                stringBuffer.append('0');
            }
            stringBuffer.append(hexString);
        }      
        return stringBuffer.toString();
    }

    
    /*public static void main(String[] args) 
    {
        SessionToken s = new SessionToken();
        String token = s.createSessionToken("Stanci");

        System.out.println(token);

        if (s.isTokenValid(token)) 
        {
            System.out.println("The session is valid");            
        } else {
            System.out.println("The session is not valid");                  
        }
        
        try 
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
