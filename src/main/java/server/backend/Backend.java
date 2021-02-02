package server.backend;

import server.data.DataRetriever;
import server.multifactor.MultifactorAuthenticator;
import server.password.PasswordManager;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.*;
import java.util.Base64;
import java.util.LinkedList;

public class Backend extends UnicastRemoteObject implements BackendInterface {

    //Assume it works for now
    private PasswordManager pM = new PasswordManager();
    private MultifactorAuthenticator mfa = new MultifactorAuthenticator();
    private DataRetriever dataRetriever = new DataRetriever();

    private PublicKey serverPublicKey;
    private PrivateKey serverPrivateKey;
    private SecretKey sessionKey;


    public Backend() throws RemoteException {
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
    public void sendSessionKey(byte[] encryptedKey) throws java.rmi.RemoteException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, serverPrivateKey);
        String session = String.valueOf(cipher.doFinal(encryptedKey));

        byte[] decodedKey = Base64.getDecoder().decode(session);
        sessionKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
    }


    public boolean login(String username, String password) throws RemoteException {
        boolean loginValid = pM.checkIfPasswordIsCorrect(username, password);
        return loginValid;

        //return true;
    }

    public String sendAuthenticationCode(String username) throws RemoteException {
        String email = "chinkiu.pak@gmail.com"; //Retrieve user email
        String code = mfa.generateAuthenticationCode();
        mfa.sendAuthenticationCode(email, code);
        return code;
    }

    public String[] getAllNames() throws RemoteException {
        return dataRetriever.getAllNames();
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
    }
    
    public boolean deleteUser(String userName, String adminUserName, String adminPassword) throws RemoteException
    {
        return pM.deleteUser(userName, adminUserName, adminPassword);
    }
    
    public boolean newAccount(String userName, String password, String adminUserName, String adminPassword, int role) throws RemoteException
    {
        return pM.addNewUser(userName, password, adminUserName, adminPassword, role);
    }
    
    public boolean changePassword(String userName, String password, String oldPassword) throws RemoteException
    {
        return pM.changePassword(userName, password, oldPassword);
    }
    
    public boolean addPermission(String userName, int perm, String adminUserName, String adminPass) throws RemoteException
    {
        return pM.addPermission(userName, perm, adminUserName, adminPass);
    }
    
    public boolean removePermission(String userName, int perm, String adminUserName, String adminPass) throws RemoteException
    {
        return pM.removePermission(userName, perm, adminUserName, adminPass);
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
    
    public LinkedList<String> getAllUsers() throws RemoteException
    {
        return pM.getAllUsers();
    }


}
