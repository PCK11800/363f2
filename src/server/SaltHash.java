import java.io.Serializable;

public class SaltHash implements Serializable{
    private byte[] salt;
    private byte[] hash;

    public SaltHash(byte[] salt, byte[] hash)
    {
        this.salt = salt;
        this.hash = hash;
    }

    public byte[] getSalt()
    {
        return this.salt;
    }

    public byte[] getHash()
    {
        return this.hash;
    }
}
