package font;

import java.awt.*;
import java.io.BufferedInputStream;
import java.io.InputStream;

public class VeraMono {

    public Font getFont(int size)
    {
        Font myfont = null;
        Font myfontReal = null;

        try{
            InputStream is = new BufferedInputStream(this.getClass().getClassLoader().getResourceAsStream("fonts/VeraMono.ttf"));
            myfont = Font.createFont(Font.TRUETYPE_FONT, is);
            myfontReal = myfont.deriveFont(Font.PLAIN, size);
        }catch (Exception e){
            e.printStackTrace();
        }
        return myfontReal;
    }
}
