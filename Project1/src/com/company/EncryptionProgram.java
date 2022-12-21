package com.company;

import java.util.Scanner;

import static com.company.DriverProgram.passkey;

public class EncryptionProgram {
    private static Scanner fromDriverProg = new Scanner(System.in);
    private static String action = fromDriverProg.nextLine();
    private static String passkey = null;

    public static void main(String[] args){

        // Interact with Driver programming.
        DriverInteraction();

    }
    // Helper Function: Interact with User
    private static void DriverInteraction (){
        while(!action.equals("quit"))
        {
            if(action.equals("password"))
                passkey = fromDriverProg.nextLine();
            else if(action.equals("encrypt"))
                System.out.println(encrypt(fromDriverProg.nextLine(), passkey));
            else if(action.equals("decrypt"))
                System.out.println(decrypt(fromDriverProg.nextLine(), passkey));
            action = fromDriverProg.nextLine();
        }
    }

    // Helper Function: to encrypt the text
    private static String encrypt(String text, final String key) {
        if (key == null || text == "")
            return "error";

        String result = "";
        text = text.toUpperCase();
        for (int i = 0, j = 0; i < text.length(); i++)
        {
            char ch = text.charAt(i);
            if (ch < 'A' || ch > 'Z')
                continue;
            result += (char) ((ch + key.charAt(j) - 2 * 'A') % 26 + 'A');
            j = ++j % key.length();
        }
        // Simply result the error message in case having any problem with encrption.
        return result;
    }

    // Helper Function: to decrypt to text
    private static String decrypt(String text, final String key) {
        if (key == null || text == "")
            return "error";

        String result = "";
        text = text.toUpperCase();
        for (int i = 0, j = 0; i < text.length(); i++)
        {
            char ch = text.charAt(i);
            if (ch < 'A' || ch > 'Z')
                continue;
            result += (char) ((ch - key.charAt(j) + 26) % 26 + 'A');
            j = ++j % key.length();
        }
        return result;
    }

}
