package server.data;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

public class DataRetriever {

    public String[] getAllNames()
    {
        URL url = this.getClass().getClassLoader().getResource("data/personal");
        File file = null;
        try {
            file = new File(url.toURI());
        } catch (URISyntaxException e) {
            file = new File(url.getPath());
        }
        File[] listOfPersons = file.listFiles();

        ArrayList<String> names = new ArrayList<>();
        for (File name : listOfPersons) {
            names.add(name.getName());
        }

        return names.toArray(String[]::new);
    }
}
