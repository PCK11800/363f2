package server.backend;

import server.data.DataRetriever;
import server.multifactor.MultifactorAuthenticator;
import server.password.PasswordManager;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;

public class Backend extends UnicastRemoteObject implements BackendInterface {

    //Assume it works for now
    private PasswordManager pM = new PasswordManager();
    private MultifactorAuthenticator mfa = new MultifactorAuthenticator();
    private DataRetriever dataRetriever = new DataRetriever();

    public Backend() throws RemoteException {
        super();
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
    
    public boolean deleteUser(String userName, String adminUserName, String adminPassword)
    {
        return pM.deleteUser(userName, adminUserName, adminPassword);
    }
    
    public boolean newAccount(String userName, String password, String adminUserName, String adminPassword, int role)
    {
        return pM.addNewUser(userName, password, adminUserName, adminPassword, role);
    }
    
    public boolean changePassword(String userName, String password, String oldPassword)
    {
        return pM.changePassword(userName, password, oldPassword);
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
    
    public int getRole(String userName)
    {
        return pM.getRole(userName);
    }
    
    public LinkedList<String> getAllUsers()
    {
        return pM.getAllUsers();
    }


}
