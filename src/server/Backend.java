import java.util.Base64;
import java.util.HashMap;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.mail.Session;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;


public class Backend extends java.rmi.server.UnicastRemoteObject implements backendInterface{
    private PasswordManager pM = new PasswordManager();
    private SessionToken sessionKeys = new SessionToken();
    private PublicKey serverPublicKey;
    private PrivateKey serverPrivateKey;
    private SecretKey sessionKey;

    public Backend()
        throws java.rmi.RemoteException{
            super();
        }

    public void generateServerKeys() throws java.rmi.RemoteException
    {
        try (FileOutputStream fos = new FileOutputStream("keyS.ks");)
        {
            KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance("RSA");
            SecureRandom secureRandom = new SecureRandom();
            keyGenerator.initialize(1024, secureRandom);
            KeyPair keyPair = keyGenerator.generateKeyPair();
            serverPublicKey = keyPair.getPublic();
            serverPrivateKey = keyPair.getPrivate();

            //TODO fix storage of private key

            /*
            char[] pass = "password".toCharArray();
            String alias = "privateKey";
            X509Certificate certificate = generateCertificate(keyPair);
            Certificate[] chain = new Certificate[1];
            chain[0] = certificate;

            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, pass);

            keyStore.setKeyEntry(alias, new KeyStore.SecretKeyEntry(keyPair.getPrivate), pass, chain);
            keyStore.store(fos, pass);
            */
        }
        catch (IOException | GeneralSecurityException e)
        {
            e.printStackTrace();
        }
    }

    public PublicKey getServerPublic() throws java.rmi.RemoteException
    {
        return this.serverPublicKey;
    }

    
    //Sending the encrypted session key to the server
    public void sendSessionKey(byte[] encryptedKey) throws java.rmi.RemoteException
    {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, serverPrivateKey);
        String session = String.valueOf(cipher.doFinal(encryptedKey));

        byte[] decodedKey = Base64.getDecoder().decode(session);
        sessionKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES"); 
    }


    public boolean login(String userName, String password) throws java.rmi.RemoteException
    {
        boolean correctPass = pM.checkIfPasswordIsCorrect(userName, password);
        if(correctPass)
        {
            sendCodeToUser(userName);
        }

        return correctPass;
    }

    public boolean newAccount(String userName, String password, String adminUserName, String adminPassword, int role)
    {
        return pM.addNewUser(userName, password, adminUserName, adminPassword, role);
    }

    
    public boolean changePassword(String userName, String password, String oldPassword) throws java.rmi.RemoteException
    {
        return pM.changePassword(userName, password, oldPassword);
    }

    //Returns true if log out was successful, false otherwise
    public boolean logOut(String user, String sessionKey) throws java.rmi.RemoteException
    {
        sessionKeys.removeSessionToken(sessionKey);
        if (!sessionKeys.tokenExists(sessionKey))
        {
            return true;
        }
        return false;
    }

    private MultifactorAuthenticator mfa = new MultifactorAuthenticator();
    private HashMap<String, Integer> mfaMap = new HashMap<>(); // Stores Username and Code in a HashMap

    public void sendCodeToUser(String user) throws java.rmi.RemoteException
    {
        int code = mfa.generateAuthenticationCode();
        mfa.sendAuthenticationCode("user.getEmail()", code);
        mfaMap.put(user, code);
    }

    public String validateCode(String user, int inputtedCode) throws java.rmi.RemoteException
    {
        if(mfaMap.get(user) == inputtedCode)
        {
            return sessionKeys.createSessionToken(user);
        }
        return null;
    }
    
    public boolean addPermission(String userName, int perm, String adminUserName, String adminPass)
    {
        return pM.addPermission(userName, perm, adminUserName, adminPass);
    }

    public boolean removePermission(String userName, int perm, String adminUserName, String adminPass)
    {
        return pM.removePermission(userName, perm, adminUserName, adminPass);
    }

    public boolean isPermitted(String userName, int perm)
    {
        return pM.isPermitted(userName, perm);
    }

    public void clearPermissions(String userName, String adminUserName, String adminPass)
    {
        pM.clearPermissions(userName, adminUserName, adminPass);
    }

    public boolean deleteUser(String userName, String adminUserName, String adminPassword)
    {
        return pM.deleteUser(userName, adminUserName, adminPassword);
    }

}
