package client.components.font;

import java.awt.*;
import java.io.BufferedInputStream;
import java.io.InputStream;

public class Inconsolata {

    public Font getFont(int size)
    {
        Font myfont = null;
        Font myfontReal = null;

        try{
            InputStream is = new BufferedInputStream(this.getClass().getClassLoader().getResourceAsStream("fonts/Inconsolata.ttf"));
            myfont = Font.createFont(Font.TRUETYPE_FONT, is);
            myfontReal = myfont.deriveFont(Font.PLAIN, size);
        }catch (Exception e){
            e.printStackTrace();
        }
        return myfontReal;
    }
}
