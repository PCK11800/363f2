package server.backend;

import client.pages.components.Person;
import server.data.DataRetriever;
import server.multifactor.MultifactorAuthenticator;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Backend extends UnicastRemoteObject implements BackendInterface {

    //Assume it works for now
    //private PasswordManager passwordManager = new PasswordManager();
    private MultifactorAuthenticator mfa = new MultifactorAuthenticator();
    private DataRetriever dataRetriever = new DataRetriever();

    public Backend() throws RemoteException {
        super();
    }

    public boolean login(String username, String password) throws RemoteException {
        //boolean loginValid = passwordManager.checkIfPasswordIsCorrect(username, password);
        //return loginValid;

        return true;
    }

    public int sendAuthenticationCode(String username) throws RemoteException {
        String email = "chinkiu.pak@gmail.com"; //Retrieve user email
        int code = mfa.generateAuthenticationCode();
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

    public void deletePerson(String name) throws RemoteException
    {
        System.out.println("Reached BACKEND");
        dataRetriever.deletePerson(name);
    }

}
