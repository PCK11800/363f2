package server.password;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Evaluation {

    // USE THIS METHOD TO EVALUATE PASSWORD:
    public boolean checkPasswordStrength(String password)
    {
        if(lengthOK(password) // check if password is of appropriate length
                && containsDigits(password) // check if password contains at least 1 digit
                && containsLowercase(password) // check if password contains at least 1 lowercase letter
                && containsUppercase(password) // check if password contains at least 1 uppercase letter
                && containsSpecialCharacter(password) // check if password contains at least 1 special character
                && !containsDictionaryWord(password)) // check if password doesn't contain any real words (English dictionary word list .txt file is from: http://www.gwicks.net/dictionaries.htm)

        {
            System.out.println("PASSWORD IS OK");
            return true;
        }

        return false;
    }

    // check if password is of appropriate length
    public static boolean lengthOK(String password)
    {
        if (password.length() >= 12) // If password has 12 or more characters, then it is of appropriate length
        {
            return true;
        }

        return false;
    }

    // check if password contains at least 1 digit
    public static boolean containsDigits(String password)
    {
        boolean containsDigit = password.contains("0")
                || password.contains("1")
                || password.contains("2")
                || password.contains("3")
                || password.contains("4")
                || password.contains("5")
                || password.contains("6")
                || password.contains("7")
                || password.contains("8")
                || password.contains("9");

        // System.out.println(containsDigit);
        if(containsDigit) // If password contains a digit
        {
            return true;
        }

        return false;
    }

    // check if password contains at least 1 lowercase letter
    public static boolean containsLowercase(String password)
    {
        ArrayList<String> lowercaseLetters = new ArrayList<>();
        List<String> letterList = Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y" , "z");
        lowercaseLetters.addAll(letterList);

        boolean containsLowercase = false;

        for (String letter : lowercaseLetters)
        {
            if (password.contains(letter))
            {
                containsLowercase = true;
            }
        }

        //System.out.println(containsLowercase);
        return containsLowercase;
    }

    // check if password contains at least 1 uppercase letter
    public static boolean containsUppercase(String password)
    {
        ArrayList<String> uppercaseLetters = new ArrayList<>();
        List<String> letterList = Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y" , "Z");
        uppercaseLetters.addAll(letterList);

        boolean containsUppercase = false;

        for (String letter : uppercaseLetters)
        {
            if (password.contains(letter))
            {
                containsUppercase = true;
            }
        }

        //System.out.println(containsLowercase);
        return containsUppercase;
    }

    // check if password contains at least 1 special character
    public static boolean containsSpecialCharacter(String password)
    {
        ArrayList<String> specialCharacters = new ArrayList<>();
        List<String> letterList = Arrays.asList("!", "@", "&", "#", "$", "*", "%");
        specialCharacters.addAll(letterList);

        boolean containsSpecial = false;

        for (String letter : specialCharacters)
        {
            if (password.contains(letter))
            {
                containsSpecial = true;
            }
        }

        //System.out.println(containsLowercase);
        return containsSpecial;
    }

    // check if password doesn't contain any real words
    public static boolean containsDictionaryWord(String password)
    {
        // English dictionary word list .txt file is from: http://www.gwicks.net/dictionaries.htm
        File dictionaryFile = new File("uk_english_dictionary.txt");

        boolean containsDictionaryWord = false;

        try
        {
            Scanner sc = new Scanner(dictionaryFile);
            while(sc.hasNextLine())
            {
                String word = sc.nextLine();

                if(password.contains(word))
                {
                    containsDictionaryWord = true;
                }

            }
        }

        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        // System.out.println(containsDictionaryWord);
        return containsDictionaryWord;
    }
}
