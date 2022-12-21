package com.company;
import java.io.*;
import java.util.*;

public class DriverProgram {

    protected static Set<String> historyOfPassKey = new LinkedHashSet<>();
    protected static Set<String> historyOfEncryption = new LinkedHashSet<>();
    protected static Process encryptionProg = null;
    protected static BufferedReader fromEncryptProg = null;
    protected static PrintStream toEncryptProg = null;
    protected static Process loggingProg = null;
    protected static BufferedReader fromLogProg = null;
    protected static PrintStream toLogProg = null;
    protected static String passkey = null;
    protected static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) throws IOException {

        // Should add "args[2]"
        String logFile = "log.txt";

        // Logging programming setup
        LoggingProgramSetup(logFile);

        //Encryption programming setup
        EncryptionProgramSetup();

        //Start with User Interaction
        userInteraction();
    }

    // Helper Function: to create process and set up pipe between loggingProgram & DriverProgram.
    private static void LoggingProgramSetup(String logFile) throws IOException {
        // Running Encryption Program
        String[] command1 = {"javac", "-cp", "src", "src\\com\\company\\LoggingProgram.java"};
        String[] command2 = {"java", "-cp", "src", "com.company.LoggingProgram", logFile};
        ProcessBuilder builder = new ProcessBuilder(command1);
        loggingProg = builder.start();

        builder = new ProcessBuilder(command2);

        loggingProg = builder.start();
        fromLogProg = new BufferedReader(new InputStreamReader(loggingProg.getInputStream()));
        toLogProg = new PrintStream(loggingProg.getOutputStream());
    }

    // Helper Function: to create process and set up pipe between EncryptionProgram & DriverProgram.
    private static void EncryptionProgramSetup() throws IOException {
        // Running Encryption Program
        String[] command1 = {"javac", "-cp", "src", "src\\com\\company\\EncryptionProgram.java"};
        String[] command2 = {"java", "-cp", "src", "com.company.EncryptionProgram"};
        ProcessBuilder builder = new ProcessBuilder(command1);
            builder.start();
            builder = new ProcessBuilder(command2);

        encryptionProg = builder.start();
        fromEncryptProg = new BufferedReader(new InputStreamReader(encryptionProg.getInputStream()));
        toEncryptProg = new PrintStream(encryptionProg.getOutputStream());
    }

    // Helper Function: to send information to loggingProgram
    private static void sendToLogProg(String input){
        toLogProg.println(input);
        toLogProg.flush();
    }

    // Helper Function: to send information to EncryptionProgram
    private static void sendToEncryptProg(String input) throws IOException {
        toEncryptProg.println(input);
        toEncryptProg.flush();
    }

    // Helper Function: to read information from EncryptionProgram
    private static String readFromEncryptProg () throws IOException {
        return fromEncryptProg.readLine();
    }

    // Helper Function: Interact with User (showing Menu etc)
    private static void userInteraction() throws IOException {
        while(true){
            userMenu();
            String command = getUserCommand().toLowerCase().trim();
            executeCommand(command);
        }
    }

    // Helper Function: execute command each command in Menu
    private static void executeCommand(String command) throws IOException {
        if (command.equals("password")) {
            sendToEncryptProg(command);
            historyOfPassFunc(command);
        }
        else if(command.equals("encrypt")) {
            if(checkPassKey()) {
                sendToLogProg(command);
                encryptionInteraction(command);
            }
        }
        else if(command.equals("decrypt")){
            if(checkPassKey()) {
                sendToLogProg(command);
                encryptionInteraction(command);
            }
        }
        else if(command.equals("history")) {
            sendToLogProg(command);
            showEncryptionHistory();
        }
        else if(command.equals("quit")) {
            sendToLogProg(command);
            sendToEncryptProg(command);
            System.exit(0);
        }
        else {
            sendToLogProg("invalid-command");
            System.out.println("Invalid Command!");
        }
    }

    // Helper Function: show to user if he/she wants to use old password or not.
    private static void historyOfPassFunc(String command) throws IOException {
        if(!historyOfPassKey.isEmpty()) {
            System.out.print("Do you want to use History Password? \n(Y/N) or Any key back to Menu : " );
            char ans =  sc.nextLine().toLowerCase().toCharArray()[0];
            if (ans == 'y') {
                if (checkPassKey()) {
                    showPassHistory();
                    updatePassKeyWithExistingKey();
                }
            } else if (ans == 'n') {
                System.out.print("Passkey Input: ");
                passkey = sc.nextLine();
                sendToLogProg(command);
                sendToEncryptProg(passkey);
                addToPassKeyHistory(passkey);
            } else
                return;
        }
        else {
            System.out.print("Passkey Input: ");
            passkey = sc.nextLine();
            sendToLogProg(command);
            sendToEncryptProg(passkey);
            addToPassKeyHistory(passkey);
        }
    }

    // Helper Function: encryption program interaction
    private static void encryptionInteraction(String command) throws IOException {
        if(!historyOfEncryption.isEmpty()) {
            System.out.print("Do you want to use Encryption History Password? \n(Y/N) or Any key back to Menu : " );
            char ans =  sc.nextLine().toLowerCase().toCharArray()[0];
            if (ans == 'y') {
                if (checkPassKey()) {
                    showEncryptionHistory(true);
                    encryptionFunction(command, true);
                }
            } else if (ans == 'n') {
                encryptionFunction(command, false);
            } else
                return;
        }
        else {
            encryptionFunction(command, false);
        }
    }

    // Helper Function: Encryption Function for both Encrypt/Decrypt function
    private static void encryptionFunction(String command, Boolean existPassword) throws IOException {
        sendToEncryptProg(command);
        String password;

        if(existPassword) {
            password = getHistoryToDoEncryption();
            sc.nextLine();
        }
        else {
            System.out.print("Enter password: ");
            password = sc.nextLine();
        }

        sendToLogProg(command.equals(command) ? password : password);
        sendToEncryptProg(password);

        String result = readFromEncryptProg();
        addToHistoryEncryption(result);

        sendToLogProg("result");
        sendToLogProg((result.equals("error") ? "Error" : "Successful"));

        System.out.println("Result : " + result);
        System.out.print("Press Enter to go back to Menu");
        sc.nextLine();
    }

    // Helper Function: print the list of every encrypt/decrypt result
    private static void showEncryptionHistory(){
        if(historyOfEncryption.isEmpty())
            return;

        int i = 1;
        System.out.println("********************************************************");
        System.out.printf("%35s\n","Encryption History");
        System.out.println("********************************************************");
        for(var out : historyOfEncryption.toArray())
            System.out.printf("%7d. %-7s\n", i++, out);
        System.out.println("********************************************************");
        System.out.print("Press Enter to continue: ");
        sc.nextLine();
        System.out.println();
    }

    // Helper Function: print the list of every encrypt/decrypt result
    private static void showEncryptionHistory(Boolean justList){
        if(historyOfEncryption.isEmpty())
            return;

        int i = 1;
        System.out.println("********************************************************");
        System.out.printf("%35s\n","Encryption History");
        System.out.println("********************************************************");
        for(var out : historyOfEncryption.toArray())
            System.out.printf("%7d. %-7s\n", i++, out);
        System.out.println("********************************************************");

    }

    // Helper Function: Add encrypt/Decrypt result to the history list
    private static void addToHistoryEncryption(String input){
        historyOfEncryption.add(input);
    }

    // Helper Function: Check passkey if it empty or not
    private static boolean checkPassKey(){
        if(passkey == null) {
            System.out.println("Password is Required!! Try Again!!");
            System.out.print("Press enter to continue!");
            sc.nextLine();
            return false;
        }
            return true;
    }

    // Helper Function: Update passkey
    private static void updatePassKeyWithExistingKey() throws IOException {
        System.out.print("Enter the number: ");
        int i = sc.nextInt();
        int index = ( i < 0) ? 1 : i;
        passkey = (String) historyOfPassKey.toArray()[index - 1];
        addToPassKeyHistory(passkey);
        sendToEncryptProg(passkey);
        sendToLogProg("password");
    }

    // Helper Function: Using history to perform encryption
    private static String getHistoryToDoEncryption() throws IOException {
        System.out.print("Enter the number: ");
        int i = sc.nextInt();
        int index = ( i < 0) ? 1 : i;
        String password = (String) historyOfEncryption.toArray()[index - 1];

       return password;
    }

    // Helper Function: Show all entered password
    private static void showPassHistory(){
        if(historyOfPassKey.isEmpty())
            return;

        int i = 1;
        System.out.println("********************************************************");
        System.out.printf("%35s\n","Password History");
        System.out.println("********************************************************");
        for(var out : historyOfPassKey.toArray())
            System.out.printf("%7d. %-7s\n", i++, out);
        System.out.println("********************************************************");
    }

    // Helper Function: Add passkey to the list of history
    private static void addToPassKeyHistory(String command){
        historyOfPassKey.add(command);
    }

    // Helper Function: Get user command
    private static String getUserCommand(){
        String command = "";

        System.out.print("Enter Command: ");
        command = sc.nextLine();

        return command;
    }

    // Helper Function: Show menu to User
    private static void userMenu(){
        System.out.println("********************************************************");
        System.out.printf("%28s\n","Menu");
        System.out.println("********************************************************");
        System.out.println();
        System.out.println("password - set the password for encryption/decryption");
        System.out.println("encrypt - encrypt the string");
        System.out.println("decrypt - decrypt a string");
        System.out.println("history - show history");
        System.out.println("quit - quit program");
        System.out.println();
        System.out.println("********************************************************");
    }
}