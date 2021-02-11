package server.password;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.*;
import java.security.spec.*;
import java.util.HashMap;
import java.util.*;
import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.imageio.stream.FileImageInputStream;
public class PasswordManager{
    private HashMap<String, SaltHash> passwords = loadPasswords();
    private String ADMIN_NAME = "ADMIN";


    public PasswordManager(){}

    /**
     * Generates a PBKE hash for the password and salt passed as parameters
     * @param password - the password to be hashed as a string
     * @param salt - the byte array that will be used to salt the password prior to hashing
     * @return the hashed password as a byte array
     */
    private byte[] generateHash(String password, byte[] salt)
    {
        try {
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 10000, 128);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            return factory.generateSecret(spec).getEncoded();
        } catch (Exception e) {
            System.out.println("Hash Failed: " + e);
            return null;
        }
    }

    /**
     * Changes the password for a given user
     * @param userName - the username of the user whoose password is being changed, as a String
     * @param newPassword - the new password, as a string
     * @param oldPassword - the old password, as a string, this is requried to ensure that only the user can update their password
     * @return a flag for wether the change has been successful
     */
    public boolean changePassword(String userName, String newPassword, String oldPassword)
    {
        if(!checkIfPasswordIsCorrect(userName, oldPassword))
        {
            System.out.println("That's not the correct password");
            return false;
        }

        try {
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[16];
            random.nextBytes(salt);

            byte[] hashedPassword = generateHash(newPassword, salt);
            int role = passwords.get(userName).getRole();
            SaltHash newSh = new SaltHash(salt, hashedPassword, role);
            passwords.replace(userName, newSh);
            savePasswords();
            return true;
        } catch (Exception e) {
            System.out.println("Password Change Failed: " + e);
            return false;
        }
    }

    /**
     * Adds a new user to the password store
     * @param userName - the username of the new user, as a string 
     * @param password - the password of the new user, as a string
     * @return a flag as to whether the addition of the new user was successful
     */
    public boolean addNewUser(String userName, String password, String adminUserName, String adminPassword, int role)
    {
        try {
            if(!checkIfPasswordIsCorrect(adminUserName, adminPassword) || !isPermitted(adminUserName, 7))
            {
                System.out.println("Un authorised user tried to create a new user");
                return false;
            }

            if(role > 3 || role < 0)
            {
                System.out.println("That's not an existing role");
                return false;
            }
            if(passwords.containsKey(userName))
            {
                System.out.println("That User Already Exists, so can't be added");
                return false;
            }
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[16];
            random.nextBytes(salt);

            byte[] hashedPassword = generateHash(password, salt);
            SaltHash sh = new SaltHash(salt, hashedPassword, role);
            passwords.put(userName, sh);
            savePasswords();
            System.out.println("Added user " + userName + " with the password " + password);
            return true;
        } catch (Exception e) {
            System.out.println("Adding new user failed: " + e);
            return false;
        }
    }

    /**
     * THIS IS FOR DEBUGGING PURPOSE ONLY!
     * USE THIS IN THE MAIN METHOD BELOW TO GENERATE A NEW PASSWORD.TXT FILE
     * IF ERRORS ARE FOUND.
     */
    public boolean addNewUser(String userName, String password, int role)
    {
        if(passwords.containsKey(userName) || role > 3 || role < 0)
        {
            return false;
        }
        else
        {
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[16];
            random.nextBytes(salt);

            byte[] hashedPassword = generateHash(password, salt);
            SaltHash sh = new SaltHash(salt, hashedPassword, role);
            passwords.put(userName, sh);

            if(role == 0) //Patient
            {
                System.out.println("Patient");
                addPermission_Admin(userName, 1);
                addPermission_Admin(userName, 2);
            }

            else if(role == 2) //Regulator
            {
                System.out.println("Regulator");
                addPermission_Admin(userName, 1);
                addPermission_Admin(userName, 3);
                addPermission_Admin(userName, 5);
            }

            else if(role == 1 || role == 3) //Staff
            {
                System.out.println("Staff");
                addPermission_Admin(userName, 1);
                addPermission_Admin(userName, 2);
                addPermission_Admin(userName, 3);
                addPermission_Admin(userName, 4);
                addPermission_Admin(userName, 5);
            }

            savePasswords();
            return true;
        }
    }

    /**
     * Checks if a password corresponds to a username i.e if a password is correct
     * @param userName - the username of the user trying to login
     * @param password - the password that will be checked if it's correct
     * @return a flag as to whether the password is correct
     */
    public boolean checkIfPasswordIsCorrect(String userName, String password)
    {
        if(!passwords.containsKey(userName))
        {
            return false;
        }
        byte[] salt = passwords.get(userName).getSalt();
        byte[] trueHash = passwords.get(userName).getHash();

        byte[] testHash = generateHash(password, salt);

        if(Arrays.equals(trueHash, testHash))
        {
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Saves the passwords hashmap to a txt file
     * @return a flag as to whether the saving was successful
     */
    private boolean savePasswords()
    {
        try {
            FileOutputStream FOS = new FileOutputStream(new File("data/passwords/Passwords.txt"));
            ObjectOutputStream OOS = new ObjectOutputStream(FOS);
            OOS.writeObject(passwords);
            FOS.close();
            OOS.close();
            return true;
        } catch (Exception e) {
            System.out.println("Password save failed: " + e);
            return false;
        }
    }
    
    public boolean addPermission(String userName, int perm, String adminUserName, String adminPass)
    {
        boolean success = false;
        if(!checkIfPasswordIsCorrect(adminUserName, adminPass) || !isAdmin(adminUserName))
        {
            System.out.println("Non-Admin user tried to add a permission");
            return false;
        }
        if(passwords.containsKey(userName))
        {
            SaltHash newSH = passwords.get(userName);
            success = newSH.addPermission(perm);
            passwords.replace(userName, newSH);
        }

        if(!success)
            System.out.println("Adding of permission was unsuccessful");

        return success;
    }

    private void addPermission_Admin(String userName, int perm)
    {
        if(passwords.containsKey(userName))
        {
            SaltHash newSH = passwords.get(userName);
            newSH.addPermission(perm);
            passwords.replace(userName, newSH);
        }
    }

    public boolean removePermission(String userName, int perm, String adminUserName, String adminPass)
    {
        boolean success = false;
        if(!checkIfPasswordIsCorrect(adminUserName, adminPass) || !isAdmin(adminUserName))
        {
            System.out.println("Non-Admin user tried to remove a permission");
            return false;
        }
        if(passwords.containsKey(userName))
        {
            SaltHash newSH = passwords.get(userName);
            if(newSH.isPermitted(perm))
            {
                newSH.removePermission(perm);
                passwords.replace(userName, newSH);
                success = true;
            }
        }

        if(!success)
            System.out.println("Removing of the permission was unsuccessful");

        return success;
    }

    public boolean isPermitted(String userName, int perm)
    {
        if(passwords.containsKey(userName))
        {
            SaltHash SH = passwords.get(userName);
            return SH.isPermitted(perm);
        }
        System.out.println("That user doesn't exist");
        return false;
    }

    public void clearPermissions(String userName, String adminUserName, String adminPass)
    {
        if(!checkIfPasswordIsCorrect(ADMIN_NAME, adminPass) || !isAdmin(adminUserName))
        {
            System.out.println("Non-Admin user tried to clear a users permissions");
            return;
        }
        if(passwords.containsKey(userName))
        {
            SaltHash SH = passwords.get(userName);
            SH.clearPermissions();
            passwords.replace(userName, SH);
        }
    }

    public int getRole(String userName)
    {
        if(passwords.containsKey(userName))
        {
            return passwords.get(userName).getRole();
        }
        return -1;
    }

    public boolean isAdmin(String userName)
    {
        if(passwords.containsKey(userName))
        {
            if(passwords.get(userName).getRole() == 3)
            {
                return true;
            }
        }
        return false;
    }

    public boolean deleteUser(String userName)
    {
        if(passwords.containsKey(userName))
        {
            passwords.remove(userName);
            savePasswords();
            System.out.println("User: " + userName + " deleted");
            return true;
        }
        else
        {
            System.out.println("User tried to delete a non-existent user");
        }
        return false;
    }

    public LinkedList<String> getAllUsers()
    {
        LinkedList<String> users = new LinkedList<>();
        passwords.forEach((k, v) -> users.add(k));
        return users;
    }
    
    /**
     * Loads the previously saved passwords hashmap
     * @return The previously saved passwords hashmap, if there hasn't been one saved then a new empty hashmap will be returned
     */
    private HashMap<String, SaltHash> loadPasswords()
    {
        try {
            String mainPath = "data/passwords/Passwords.txt";
            File passwordFile = new File(mainPath);
            if(passwordFile.exists())
            {
                FileInputStream FIS = new FileInputStream(new File(mainPath));
                ObjectInputStream OIS = new ObjectInputStream(FIS);
                HashMap<String, SaltHash> result;
                result = (HashMap<String, SaltHash>) OIS.readObject();
                return result;
            }
            else
            {
                return new HashMap<String, SaltHash>();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<String, SaltHash>();
        }
    }

    /*
     * USER ACCOUNTS FOR TESTING
     * TEMP EMAIL FROM https://www.guerrillamail.com/
     *
     * ADMIN ACCOUNT
     * Username: f2.scc363@gmail.com
     * Password: tY,?S5b&7Xn{)NR@
     *
     * PATIENT ACCOUNT
     * Username: patient@sharklasers.com
     * Password: [RPrhn\R!3h>Fb{(
     *
     * DOCTOR ACCOUNT
     * Username: doctor@sharklasers.com
     * Password: `A%~9^2acHV'{tu=
     *
     * REGULATOR ACCOUNT
     * Username: regulator@sharklasers.com
     * Password: :nTz6<C~QBE8w&y9
     */
    public static void main(String[] args)
    {
        PasswordManager pa = new PasswordManager();
        pa.addNewUser("f2.scc363@gmail.com", "tY,?S5b&7Xn{)NR@", 3);
        pa.addNewUser("patient@sharklasers.com", "[RPrhn\\R!3h>Fb{(", 0);
        pa.addNewUser("doctor@sharklasers.com", "`A%~9^2acHV'{tu=", 1);
        pa.addNewUser("regulator@sharklasers.com", ":nTz6<C~QBE8w&y9", 2);
    }
}

