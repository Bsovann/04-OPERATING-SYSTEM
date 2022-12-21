package com.company;

import java.lang.*;
import java.util.Scanner;

public class MainMemory
{

    public static void main(String[] args)
    {
        String mode;
        int memLoc = 0;

        Scanner s = new Scanner(System.in);

        mode = s.next();

        while(!mode.equals("halt"))
        {
            if(mode.equals("read")) System.out.println(memLoc);
            else if(mode.equals("write")) memLoc = s.nextInt();

            mode = s.next();
        }
    }
}
