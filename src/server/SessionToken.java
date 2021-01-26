import java.beans.Encoder;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class SessionToken {

    private String ErrInvalidSession = "Invalid or expired session token";
    private Random random;
    private byte[] stringBytes;
    private StringBuffer stringBuffer;
    private String hexString;

    //TODO: Find better storage place for tokens
    public HashMap<String, LocalDateTime> sessionTokens = new HashMap<>();


    public SessionToken()
    {
    }

    public boolean tokenExists(String sessionToken)
    {
        if (sessionTokens.containsKey(sessionToken))
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
        if(sessionTokens.containsKey(sessionToken))
        {
            LocalDateTime tokenTime = sessionTokens.get(sessionToken);
            LocalDateTime currentTime = LocalDateTime.now();

            //Find the time between when the token was generated an the current time 
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

    
    /** 
     * This method creates a new session token 
     * @param username The current users' username
     * @return The newly creatred session token
     */
    public String createSessionToken(String username)
    {
        //The sessionToken is a randomly generated string and a username
        String sessionToken = generateToken() + username; 
        sessionTokens.put(sessionToken, LocalDateTime.now());

        return sessionToken;
    }

    
    /** 
     * This method is for when a user needs to log out. 
     * @param token The current session token 
     */
    public void removeSessionToken(String token)
    {
        if (this.isTokenValid(token))
        {
            sessionTokens.remove(token);
        }
    }

    
    /** 
     * @return Returns a token for the session token 
     */
    private byte[] generateToken()
    {
        random = new SecureRandom();
        stringBytes = new byte[8];
        random.nextBytes(stringBytes);

        return stringBytes;
        //return this.bytesToString(stringBytes);
    }

    
    /** 
     * @param bytes
     * @return A string
     */
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

    private String btoString(byte[] bytes)
    {
        java.util.Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();
        return encoder.encodeToString(bytes);
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
