package com.company;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class LoggingProgram {

    protected static Scanner fromDriver = new Scanner(System.in);

    public static void main(String[] args) throws IOException {

        String logFile = args[0];
        // Create an object to be able to write to file
        BufferedWriter file = new BufferedWriter(new FileWriter(logFile, true));

        // First when program is start up
        updateLog("start", file);

        // Log Function:
        logFunction(file);
    }

    // Helper Function: Interact with DriverProgram
    private static void logFunction(BufferedWriter file) throws IOException {
        String action = fromDriver.nextLine();

        while(true){
            if(action.equals("password"))
                updateLog(action, file);
            else if (action.equals("encrypt"))
                updateLog(action, file);
            else if (action.equals("decrypt"))
                updateLog(action, file);
            else if (action.equals("result")) // can be error and successful
                updateLog(action, file);
            else if (action.equals("history"))
                updateLog(action, file);
            else if (action.equals("invalid-command"))
                updateLog(action, file);
            else if (action.equals("quit")) {
                updateLog("log-stop", file);
                updateLog(action, file);
                System.exit(0);
            }

            action = fromDriver.nextLine();
        }


    }

    // Helper Function: Update log file.
    private static void updateLog(String action, BufferedWriter file) throws IOException {
        String message = getDateAndTime() + " [" + action.toUpperCase() + "] : " + getMessage(action) + ".\n";
        file.write(message);
        file.flush();
    }

    // Helper Function: Get message base on action
    private static String getMessage(String action){
        String keyWord = action.toLowerCase().trim();

        if (keyWord.equals("start"))
            return "logging is Started";
        else if(keyWord.equals("password"))
            return "Password is been set";
        else if (keyWord.equals("encrypt"))
            return fromDriver.nextLine();
        else if (keyWord.equals("decrypt"))
            return fromDriver.nextLine();
        else if (keyWord.equals("history"))
            return "Print History";
        else if (keyWord.equals("result"))
            return fromDriver.nextLine();
        else if (keyWord.equals("error"))
            return fromDriver.nextLine();
        else if (keyWord.equals("invalid-command"))
            return "Invalid input";
        else if (keyWord.equals("log-stop"))
            return "logging stopped";
        else if (keyWord.equals("quit"))
            return "Quit all programs";

        return "Error";
    }

    // Helper Function: Get Date and Time
    private static String getDateAndTime(){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTimeNow = LocalDateTime.now();

        return dateTimeFormatter.format(dateTimeNow);
    }
}
