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
        assignPermissions(role);
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
            System.out.println("Permission " + perm + " was addded");
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

    private void assignPermissions(int role)
    {
        switch (role) {
            case 0:
                permissions.add(1);
                break;
        
            case 1:
                permissions.add(1);
                permissions.add(2);
                permissions.add(3);
                permissions.add(4);
                permissions.add(5);
                permissions.add(6);
                break;
            
            case 2:
                permissions.add(1);
                permissions.add(2);
            
            case 3:
                permissions.add(1);
                permissions.add(2);
                permissions.add(3);
                permissions.add(4);
                permissions.add(5);
                permissions.add(6);
                permissions.add(7);
                permissions.add(8);

        }
    }
}
