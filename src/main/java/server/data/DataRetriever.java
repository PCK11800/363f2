package server.data;

import client.pages.components.Person;
import com.google.gson.JsonParser;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class DataRetriever {

    public String[] getAllNames()
    {
        File file = null;
        String mainPath = "data";
        file = new File(mainPath);
        File[] listOfPersons = file.listFiles();

        ArrayList<String> names = new ArrayList<>();
        for (File name : listOfPersons) {
            names.add(name.getName());
        }

        return names.toArray(String[]::new);
    }

    public void storePerson(String[] person)
    {
        String fileName = person[0];
        JSONObject fileContent = new JSONObject();

        fileContent.put("name", person[0]);
        fileContent.put("age", person[1]);
        fileContent.put("gender", person[2]);
        fileContent.put("birthday", person[3]);
        fileContent.put("address", person[4]);
        fileContent.put("email", person[5]);
        fileContent.put("phoneNumber", person[6]);
        fileContent.put("doctorsNote", person[7]);

        try{
            String mainPath = "data/" + fileName;
            Files.write(Paths.get(mainPath), fileContent.toJSONString().getBytes());
            System.out.println(mainPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String[] getPerson(String name)
    {
        String fileName = name;
        JSONObject fileContent = (JSONObject) readJSON(fileName);
        System.out.println(fileContent.get("name"));

        String[] person = new String[8];
        person[0] = (String) fileContent.get("name");
        person[1] = (String) fileContent.get("age");
        person[2] = (String) fileContent.get("gender");
        person[3] = (String) fileContent.get("birthday");
        person[4] = (String) fileContent.get("address");
        person[5] = (String) fileContent.get("email");
        person[6] = (String) fileContent.get("phoneNumber");
        person[7] = (String) fileContent.get("doctorsNote");

        return person;
    }

    private Object readJSON(String fileName)
    {
        JSONParser jsonParser = new JSONParser();
        Object object = null;
        try {
            String mainPath = "data/" + fileName;
            FileReader reader = new FileReader(mainPath);
            object = jsonParser.parse(reader);
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return object;
    }

    public void deletePerson(String name)
    {
        String mainPath = "data/" + name;
        System.out.println(mainPath);
        File toBeDeleted = new File(mainPath);
        toBeDeleted.delete();
    }
}
