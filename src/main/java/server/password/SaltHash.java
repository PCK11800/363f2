package server.password;

import java.io.Serializable;
import java.util.LinkedList;

public class SaltHash implements Serializable{
    private byte[] salt;
    private byte[] hash;
    private LinkedList<Integer> permissions = new LinkedList<>();
    private int role;

    public SaltHash(byte[] salt, byte[] hash, int role)
    {
        this.salt = salt;
        this.hash = hash;
        this.role = role;
    }

    public byte[] getSalt()
    {
        return this.salt;
    }

    public byte[] getHash()
    {
        return this.hash;
    }

    public boolean addPermission(int perm)
    {
        if(!permissions.contains(perm) && perm >= 1 && perm <=8)
        {
            System.out.println("Permission " + perm + " was added");
            return permissions.add(perm);
        }
        return false;
    }

    public boolean removePermission(int perm)
    {
        return permissions.remove(new Integer(perm));
    }

    public boolean isPermitted(int perm)
    {
        return permissions.contains(perm);
    }

    public void clearPermissions()
    {
        permissions.clear();
    }

    public int getRole()
    {
        return this.role;
    }

}
