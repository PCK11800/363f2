package server.backend;

import server.log.Log;
import server.data.DataRetriever;
import server.multifactor.MultifactorAuthenticator;
import server.password.Evaluation;
import server.password.PasswordManager;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedList;

public class Backend extends UnicastRemoteObject implements BackendInterface {

    //Assume it works for now
    private PasswordManager pM = new PasswordManager();
    private MultifactorAuthenticator mfa = new MultifactorAuthenticator();
    private DataRetriever dataRetriever = new DataRetriever();
    private Evaluation passwordEvaluator = new Evaluation();

    private PublicKey serverPublicKey;
    private PrivateKey serverPrivateKey;
    private HashMap<String, SecretKey> sessionTokens = new HashMap<>();


    private Log log = new Log();


    public Backend() throws RemoteException {
        super();
    }

    public void exchangeMessages(String encryptedMessage, String username) throws RemoteException
    {
        try {
            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.DECRYPT_MODE, getSessionKey(username));
            byte[] decryptedBytes = c.doFinal(Base64.getDecoder().decode(encryptedMessage));
            String decrypted = new String(decryptedBytes);
            System.out.println(decrypted);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private String encryptMessage(String message, SecretKey sessionKey)
    {
        String encrypted = "";
        if (sessionKey != null) {
            try {
                Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
                cipher.init(Cipher.ENCRYPT_MODE, sessionKey);
                final byte[] encValue = cipher.doFinal(message.getBytes("UTF-8"));
                encrypted = Base64.getEncoder().encodeToString(encValue);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return encrypted;
    }

    private String[] encryptArrayMessage(String[] messages, SecretKey sessionKey)
    {
        String[] encryptedMessages = new String[messages.length];
        String encrypted = "";

        for (int i = 0; i < messages.length; i++)
        {
            if (sessionKey != null) {
                try {
                    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
                    cipher.init(Cipher.ENCRYPT_MODE, sessionKey);
                    final byte[] encValue = cipher.doFinal(messages[i].getBytes("UTF-8"));
                    encrypted = Base64.getEncoder().encodeToString(encValue);
                    encryptedMessages[i] = encrypted;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else
            {
                encryptedMessages[i] = "";
            }
        }
        return encryptedMessages;
    }

    private String[] decryptArrayMessage(String[] encryptedMessages, SecretKey sessionKey)
    {
        String[] decryptedMessages = new String[encryptedMessages.length];
        String decrypted = "";

        for (int i = 0; i < encryptedMessages.length; i++)
        {
            if (sessionKey != null) {
                try {
                    Cipher c = Cipher.getInstance("AES/ECB/PKCS5PADDING");
                    c.init(Cipher.DECRYPT_MODE, sessionKey);
                    byte[] decryptedBytes = c.doFinal(Base64.getDecoder().decode(encryptedMessages[i]));
                    decrypted = new String(decryptedBytes);
                    decryptedMessages[i] = decrypted;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else
            {
                decryptedMessages[i] = "";
            }
        }
        return decryptedMessages;
    }

    private String decryptMessage(String encryptedMessage, SecretKey sessionKey)
    {
        String decrypted = "";
        if (sessionKey != null) {
            try {
                Cipher c = Cipher.getInstance("AES/ECB/PKCS5PADDING");
                c.init(Cipher.DECRYPT_MODE, sessionKey);
                byte[] decryptedBytes = c.doFinal(Base64.getDecoder().decode(encryptedMessage));
                decrypted = new String(decryptedBytes);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return decrypted;
    }

    public void generateServerKeys() throws java.rmi.RemoteException
    {
        try
        {
            KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance("RSA");
            SecureRandom secureRandom = new SecureRandom();
            keyGenerator.initialize(1024, secureRandom);
            KeyPair keyPair = keyGenerator.generateKeyPair();
            serverPublicKey = keyPair.getPublic();
            serverPrivateKey = keyPair.getPrivate();
                    }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public PublicKey getServerPublic() throws java.rmi.RemoteException
    {
        return this.serverPublicKey;
    }

    private SecretKey getSessionKey(String username)
    {
        if (sessionTokens.containsKey(username))
        {
            return sessionTokens.get(username);
        }
        else return null;
    }

    //Sending the encrypted session key to the server
    public void sendSessionKey(byte[] encryptedKey, String username) throws java.rmi.RemoteException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, serverPrivateKey);
        byte[] decrypted = cipher.doFinal(encryptedKey);
        String session = new String(decrypted);
        byte[] decodedKey = Base64.getDecoder().decode(session);
        sessionTokens.put(username, new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES"));
    }

    @Override
    public void logout(String username) throws RemoteException {
        sessionTokens.remove(username);
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
        System.out.println("-----------------------------------------------------");
        System.out.println("Authentication Code: " + code);
        System.out.println("-----------------------------------------------------");
        String encryptedCode = this.encryptMessage(code, getSessionKey(username));
        return encryptedCode;
    }

    public String[] getAllNames(String username) throws RemoteException {
        if(!isPermitted(encryptMessage(username, getSessionKey(username)), encryptMessage(Integer.toString(5), getSessionKey(username)), username))
        {
            String[] names = {dataRetriever.getNameFromEmail(username)};
            String[] encryptedNames = encryptArrayMessage(names, getSessionKey(username));
            return encryptedNames;
        }
        else
        {
            return encryptArrayMessage(dataRetriever.getAllNames(), getSessionKey(username));
        }
    }

    public String[] getPerson(String username, String name) throws RemoteException
    {
        String decryptedName = decryptMessage(name, getSessionKey(username));
        return encryptArrayMessage(dataRetriever.getPerson(decryptedName), getSessionKey(username));
    }

    public void storePerson(String[] person, String username) throws RemoteException
    {
        String[] decryptedPerson = decryptArrayMessage(person, getSessionKey(username));
        dataRetriever.storePerson(decryptedPerson);
    }
    //There are two methods for deleting users, which is best can be decided on monday
    public void deletePerson(String name, String username) throws RemoteException
    {
        String decryptedName = decryptMessage(name, getSessionKey(username));
        System.out.println("Reached BACKEND");
        dataRetriever.deletePerson(decryptedName);
        log.addMsg(1,"User deleted" , decryptedName);
    }
    
    public boolean deleteUser(String userName, String username) throws RemoteException
    {
        String decryptedName = decryptMessage(userName, getSessionKey(username));
        boolean result = pM.deleteUser(decryptedName);
        if(result){
            log.addMsg(1,"User deleted" , decryptedName);
        } else{
            log.addMsg(1,"Attempt to delete user failed" , decryptedName);
        }
        return result;
    }
    
    public boolean newAccount(String userName, String password, String role, String currentUsername) throws RemoteException
    {
        String decryptedName = decryptMessage(userName, getSessionKey(currentUsername));
        String decryptedPassword = decryptMessage(password, getSessionKey(currentUsername));
        String decryptedRole = decryptMessage(role, getSessionKey(currentUsername));
        int finalRole = Integer.parseInt(decryptedRole);

        boolean result =  pM.addNewUser(decryptedName, decryptedPassword, finalRole);
        if(result) {
            log.addMsg(0,"New account added" , decryptedName);
            mfa.newAccount(decryptedName);
        } else{
            log.addMsg(1,"Failed to add user" , decryptedName);
        }
        return result;
    }

    public boolean changePassword(String userName, String password, String oldPassword) throws RemoteException
    {
        String decryptedOldPassword = decryptMessage(password, getSessionKey(userName));
        String decryptedNewPassword = decryptMessage(oldPassword, getSessionKey(userName));

        boolean result =  pM.changePassword(userName, decryptedOldPassword, decryptedNewPassword);
        if(result) {
            log.addMsg(0,"Password changed" , userName);
        } else{
            log.addMsg(1,"Failed to change password" , userName);
        }
        return result;
    }
    
    public boolean addPermission(String userName, String perm, String adminUserName, String adminPass) throws RemoteException
    {
        String decryptedName = decryptMessage(userName, getSessionKey(adminUserName));
        String decryptedAdminPassword = decryptMessage(adminPass, getSessionKey(adminUserName));
        String decryptedPerm = decryptMessage(perm, getSessionKey(adminUserName));
        int finalPerm = Integer.parseInt(decryptedPerm);

        boolean result =  pM.addPermission(decryptedName, finalPerm, adminUserName, decryptedAdminPassword);
        if(result) {
            String email = "f2.scc363@gmail.com";//username;
            mfa.permissionsUpdated(email);
            log.addMsg(0,"Users permissions changed" , decryptedName);
        } else{
            log.addMsg(2,"Failed to change users permissions" , decryptedName);
        }
        return result;
    }
    
    public boolean removePermission(String userName, String perm, String adminUserName, String adminPass) throws RemoteException
    {
        String decryptedName = decryptMessage(userName, getSessionKey(adminUserName));
        String decryptedAdminPassword = decryptMessage(adminPass, getSessionKey(adminUserName));
        String decryptedPerm = decryptMessage(perm, getSessionKey(adminUserName));
        int finalPerm = Integer.parseInt(decryptedPerm);

        boolean result =  pM.removePermission(decryptedName, finalPerm, adminUserName, decryptedAdminPassword);
        if(result) {
            String email = "f2.scc363@gmail.com";//username;
            mfa.permissionsUpdated(email);
            log.addMsg(0,"Users permissions changed" , decryptedName);
        } else{
            log.addMsg(2,"Failed to change users permissions" , decryptedName);
        }
        return result;
    }

    public boolean isPermitted(String userName, String perm, String currentUsername) throws RemoteException
    {
        String decryptedName = decryptMessage(userName, getSessionKey(currentUsername));
        String decryptedPerm = decryptMessage(perm, getSessionKey(currentUsername));
        int finalPerm = 0;
        if (!decryptedPerm.isEmpty())
        {
            finalPerm = Integer.parseInt(decryptedPerm);
        }
        return pM.isPermitted(decryptedName, finalPerm);
    }

    public void clearPermissions(String userName, String adminUserName, String adminPass) throws RemoteException
    {
        pM.clearPermissions(userName, adminUserName, adminPass);
    }

    public String getRole(String userName, String currentUserName) throws RemoteException
    {
        String decryptedName = decryptMessage(userName, getSessionKey(currentUserName));
        return encryptMessage(Integer.toString(pM.getRole(decryptedName)), getSessionKey(currentUserName));
    }

    public boolean isPasswordValid(String username, String password) throws RemoteException {
        String decryptedPassword = decryptMessage(password, getSessionKey(username));
        return pM.checkIfPasswordIsCorrect(username, decryptedPassword);
    }

    @Override
    public boolean isPasswordStrong(String username, String password) throws RemoteException {
        String decryptedPassword = decryptMessage(password, getSessionKey(username));
        return passwordEvaluator.checkPasswordStrength(decryptedPassword);
    }

    public LinkedList<String> getAllUsers() throws RemoteException
    {
        return pM.getAllUsers();
    }

}
