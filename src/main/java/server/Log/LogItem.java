package server.log;


import java.io.Serializable;

public class LogItem implements Serializable{
    private int type;
    private String desc;
    private byte[] hash;    
    private String user;

    public LogItem(int type, String desc, byte[] hash, String user)
    {
        this.type = type;
        this.desc = desc;
        this.hash = hash;
        this.user = user;
    }

    public String toString()
    {
        String str = "";
        switch (type) {
            case 0:
                str = str + "[INFO] ";
                break;
            case 1:
                str = str + "[WARNING] ";
                break;
            case 2:
                str = str + "[ERROR] ";
                break;    
        }
        str = str + "[" + user + "] ";
        try {
            return str + desc + " Hash: " + hash.toString() +'\n';
        } catch (Exception e) {
            System.out.println("Hash has not yet been assigned");
            return str + desc + '\n';
        }
    }

    public byte[] getHash()
    {
        return this.hash;
    }

    public int getType()
    {
        return this.type;
    }
}
