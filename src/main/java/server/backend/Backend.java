package server.backend;

import server.Log.Log;
import server.data.DataRetriever;
import server.multifactor.MultifactorAuthenticator;
import server.password.Evaluation;
import server.password.PasswordManager;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.*;
import java.util.Arrays;
import java.util.Base64;
import java.util.LinkedList;

public class Backend extends UnicastRemoteObject implements BackendInterface {

    //Assume it works for now
    private PasswordManager pM = new PasswordManager();
    private MultifactorAuthenticator mfa = new MultifactorAuthenticator();
    private DataRetriever dataRetriever = new DataRetriever();
    private Evaluation passwordEvaluator = new Evaluation();

    private PublicKey serverPublicKey;
    private PrivateKey serverPrivateKey;
    private SecretKey sessionKey;

    private Log log = new Log();


    public Backend() throws RemoteException {
        super();
    }

    public void exchangeMessages(String encryptedMessage) throws RemoteException
    {
        try {
            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.DECRYPT_MODE, sessionKey);
            byte[] decryptedBytes = c.doFinal(Base64.getDecoder().decode(encryptedMessage));
            String decrypted = new String(decryptedBytes);
            System.out.println(decrypted);
        } catch(Exception e) {
            e.printStackTrace();
        }

    }

    public void generateServerKeys() throws java.rmi.RemoteException
    {
        try (FileOutputStream fos = new FileOutputStream("data/keys/keyS.ks");)
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
    public void sendSessionKey(byte[] encryptedKey) throws java.rmi.RemoteException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, serverPrivateKey);
        byte[] decrypted = cipher.doFinal(encryptedKey);
        String session = new String(decrypted);
        byte[] decodedKey = Base64.getDecoder().decode(session);
        sessionKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
    }


    public boolean login(String username, String password) throws RemoteException {
        boolean loginValid = pM.checkIfPasswordIsCorrect(username, password);
        if(!loginValid){
            //String email = "f2.scc363@gmail.com";//username;
            mfa.failedLogInAttempt(username);
            log.addMsg(1,"Failed Log In Attempt - Wrong password" , username);
        } else{
            log.addMsg(0,"Successful Log In" , username);
        }
        return loginValid;

        //return true;
    }

    public String sendAuthenticationCode(String username) throws RemoteException {
        String email = username; //Retrieve user email
        String code = mfa.generateAuthenticationCode();
        mfa.sendAuthenticationCode(email, code);
        log.addMsg(0,"Authentication code sent" , username);
        return code;
    }

    public String[] getAllNames(String username) throws RemoteException {
        if(!isPermitted(username, 5))
        {
            String[] names = {dataRetriever.getNameFromEmail(username)};
            return names;
        }
        else
        {
            return dataRetriever.getAllNames();
        }
    }

    public String[] getPerson(String name) throws RemoteException
    {
        return dataRetriever.getPerson(name);
    }

    public void storePerson(String[] person) throws RemoteException
    {
        dataRetriever.storePerson(person);
    }
    //There are two methods for deleting users, which is best can be decided on monday
    public void deletePerson(String name) throws RemoteException
    {
        System.out.println("Reached BACKEND");
        dataRetriever.deletePerson(name);
        log.addMsg(1,"User deleted" , name);
    }
    
    public boolean deleteUser(String userName) throws RemoteException
    {
        boolean result = pM.deleteUser(userName);
        if(result){
            log.addMsg(1,"User deleted" , userName);
        } else{
            log.addMsg(1,"Attempt to delete user failed" , userName);
        }
        return result;
    }
    
    public boolean newAccount(String userName, String password, int role) throws RemoteException
    {
        boolean result =  pM.addNewUser(userName, password, role);
        if(result) {
            log.addMsg(0,"New account added" , userName);
            mfa.newAccount(userName);
        } else{
            log.addMsg(1,"Failed to add user" , userName);
        }
        return result;
    }
    
    public boolean changePassword(String userName, String password, String oldPassword) throws RemoteException
    {
        boolean result =  pM.changePassword(userName, password, oldPassword);
        if(result) {
            log.addMsg(0,"Password changed" , userName);
        } else{
            log.addMsg(1,"Failed to change password" , userName);
        }
        return result;
    }
    
    public boolean addPermission(String userName, int perm, String adminUserName, String adminPass) throws RemoteException
    {
        boolean result =  pM.addPermission(userName, perm, adminUserName, adminPass);
        if(result) {
            String email = "f2.scc363@gmail.com";//username;
            mfa.permissionsUpdated(email);
            log.addMsg(0,"Users permissions changed" , userName);
        } else{
            log.addMsg(2,"Failed to change users permissions" , userName);
        }
        return result;
    }
    
    public boolean removePermission(String userName, int perm, String adminUserName, String adminPass) throws RemoteException
    {
        boolean result =  pM.removePermission(userName, perm, adminUserName, adminPass);
        if(result) {
            String email = "f2.scc363@gmail.com";//username;
            mfa.permissionsUpdated(email);
            log.addMsg(0,"Users permissions changed" , userName);
        } else{
            log.addMsg(2,"Failed to change users permissions" , userName);
        }
        return result;
    }

    public boolean isPermitted(String userName, int perm) throws RemoteException
    {
        return pM.isPermitted(userName, perm);
    }

    public void clearPermissions(String userName, String adminUserName, String adminPass) throws RemoteException
    {
        pM.clearPermissions(userName, adminUserName, adminPass);
    }

    public int getRole(String userName) throws RemoteException
    {
        return pM.getRole(userName);
    }

    public boolean isPasswordValid(String username, String password) throws RemoteException {
        return pM.checkIfPasswordIsCorrect(username, password);
    }

    @Override
    public boolean isPasswordStrong(String password) throws RemoteException {
        return passwordEvaluator.checkPasswordStrength(password);
    }

    public LinkedList<String> getAllUsers() throws RemoteException
    {
        return pM.getAllUsers();
    }

}
