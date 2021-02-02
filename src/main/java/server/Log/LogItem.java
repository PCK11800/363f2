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
        return str + desc + '\n';
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
