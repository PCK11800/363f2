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
import java.rmi.server.RemoteObject;
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
    private SecretKey sessionKey = null;

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

    private String encryptMessage(String message)
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

    private String[] encryptArrayMessage(String[] messages)
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

    private String[] decryptArrayMessage(String[] encryptedMessages)
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

    private String decryptMessage(String encryptedMessage)
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
        String decryptedUsername = this.decryptMessage(username);
        String email = decryptedUsername; //Retrieve user email
        String code = mfa.generateAuthenticationCode();
        mfa.sendAuthenticationCode(email, code);
        log.addMsg(0,"Authentication code sent" , decryptedUsername);
        String encryptedCode = this.encryptMessage(code);
        return encryptedCode;
    }

    public String[] getAllNames(String username) throws RemoteException {
        String decryptedUsername = decryptMessage(username);
        if(!isPermitted(username, encryptMessage(Integer.toString(5))))
        {
            String[] names = {dataRetriever.getNameFromEmail(decryptedUsername)};
            String[] encryptedNames = encryptArrayMessage(names);
            return encryptedNames;
        }
        else
        {
            //return dataRetriever.getAllNames();
            return encryptArrayMessage(dataRetriever.getAllNames());
        }
    }

    public String[] getPerson(String name) throws RemoteException
    {
        String decryptedName = decryptMessage(name);
        return encryptArrayMessage(dataRetriever.getPerson(decryptedName));
    }

    public void storePerson(String[] person) throws RemoteException
    {
        String[] decryptedPerson = decryptArrayMessage(person);
        dataRetriever.storePerson(decryptedPerson);
    }
    //There are two methods for deleting users, which is best can be decided on monday
    public void deletePerson(String name) throws RemoteException
    {
        String decryptedName = decryptMessage(name);
        System.out.println("Reached BACKEND");
        dataRetriever.deletePerson(decryptedName);
        log.addMsg(1,"User deleted" , decryptedName);
    }
    
    public boolean deleteUser(String userName) throws RemoteException
    {
        String decryptedName = decryptMessage(userName);
        boolean result = pM.deleteUser(decryptedName);
        if(result){
            log.addMsg(1,"User deleted" , decryptedName);
        } else{
            log.addMsg(1,"Attempt to delete user failed" , decryptedName);
        }
        return result;
    }
    
    public boolean newAccount(String userName, String password, String role) throws RemoteException
    {
        String decryptedName = decryptMessage(userName);
        String decryptedPassword = decryptMessage(password);
        String decryptedRole = decryptMessage(role);
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

    //int i = 1;
    //String ii = Integer.toString(i);
    //int j = Integer.parseInt(ii);

    public boolean changePassword(String userName, String password, String oldPassword) throws RemoteException
    {
        String decryptedName = decryptMessage(userName);
        String decryptedOldPassword = decryptMessage(password);
        String decryptedNewPassword = decryptMessage(oldPassword);

        boolean result =  pM.changePassword(decryptedName, decryptedOldPassword, decryptedNewPassword);
        if(result) {
            log.addMsg(0,"Password changed" , decryptedName);
        } else{
            log.addMsg(1,"Failed to change password" , decryptedName);
        }
        return result;
    }
    
    public boolean addPermission(String userName, String perm, String adminUserName, String adminPass) throws RemoteException
    {
        String decryptedName = decryptMessage(userName);
        String decryptedAdminName = decryptMessage(adminUserName);
        String decryptedAdminPassword = decryptMessage(adminPass);
        String decryptedPerm = decryptMessage(perm);
        int finalPerm = Integer.parseInt(decryptedPerm);

        boolean result =  pM.addPermission(decryptedName, finalPerm, decryptedAdminName, decryptedAdminPassword);
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
        String decryptedName = decryptMessage(userName);
        String decryptedAdminName = decryptMessage(adminUserName);
        String decryptedAdminPassword = decryptMessage(adminPass);
        String decryptedPerm = decryptMessage(perm);
        int finalPerm = Integer.parseInt(decryptedPerm);

        boolean result =  pM.removePermission(decryptedName, finalPerm, decryptedAdminName, decryptedAdminPassword);
        if(result) {
            String email = "f2.scc363@gmail.com";//username;
            mfa.permissionsUpdated(email);
            log.addMsg(0,"Users permissions changed" , decryptedName);
        } else{
            log.addMsg(2,"Failed to change users permissions" , decryptedName);
        }
        return result;
    }

    public boolean isPermitted(String userName, String perm) throws RemoteException
    {
        String decryptedName = decryptMessage(userName);
        String decryptedPerm = decryptMessage(perm);
        int finalPerm = Integer.parseInt(decryptedPerm);
        return pM.isPermitted(decryptedName, finalPerm);
    }

    public void clearPermissions(String userName, String adminUserName, String adminPass) throws RemoteException
    {
        pM.clearPermissions(userName, adminUserName, adminPass);
    }

    public String getRole(String userName) throws RemoteException
    {
        String decryptedName = decryptMessage(userName);
        //return pM.getRole(decryptedName);
        return encryptMessage(Integer.toString(pM.getRole(decryptedName)));
    }

    public boolean isPasswordValid(String username, String password) throws RemoteException {
        String decryptedName = decryptMessage(username);
        String decryptedPassword = decryptMessage(password);
        return pM.checkIfPasswordIsCorrect(decryptedName, decryptedPassword);
    }

    @Override
    public boolean isPasswordStrong(String password) throws RemoteException {
        String decryptedPassword = decryptMessage(password);
        return passwordEvaluator.checkPasswordStrength(decryptedPassword);
    }

    public LinkedList<String> getAllUsers() throws RemoteException
    {
        return pM.getAllUsers();
    }

}
