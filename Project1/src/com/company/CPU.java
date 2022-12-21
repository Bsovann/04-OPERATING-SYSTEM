package com.company;

import java.lang.*;
import java.io.*;
import java.util.Scanner;

public class CPU
{
    public static void main(String[] args)
    {
        try
        {
            Process mem = Runtime.getRuntime().exec("java MainMemory");

            InputStream inStream = mem.getInputStream();
            OutputStream outStream = mem.getOutputStream();

            Scanner fromMem = new Scanner(inStream);
            PrintStream toMem = new PrintStream(outStream);

            for(int i=10; i>=0; i--)
            {
                toMem.println("write");
                toMem.println(i);

                System.out.print("Set to ");
                toMem.println("read");
                toMem.flush();
                System.out.println(fromMem.nextInt());
            }

            toMem.println("halt");
            toMem.flush();

            mem.waitFor();
        }
        catch(IOException ex)
        {
            System.out.println("Unable to run MainMemory");
        }
        catch(InterruptedException ex)
        {
            System.out.println("Unexpected Termination");
        }
    }
}
