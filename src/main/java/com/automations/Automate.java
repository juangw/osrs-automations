package com.automations;

import java.io.*;

public class Automate {

    private static String mode = "";
    private static Integer rate = 0;
    private static String keyToPress = "";
    private static Integer timeToWait = 0;
    private static Boolean debugMode = false;

    public static void main(final String[] args) {
        // Used to determine automation method
        mode = args[0];

        // Used only in useItem, can provide bogus values otherwise
        keyToPress = args[1];
        timeToWait = Integer.parseInt(args[2]);

        // Used to debug
        if (args.length == 4) {
            debugMode = Boolean.parseBoolean(args[4]);
        }

        // Get input on rate of repetition
        while (rate == 0) {
            try {
                System.out.println("Speed of the auto-clicker (in miliseconds):");
                final BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                try {
                    rate = Integer.parseInt(in.readLine());
                    if (rate < 500) {
                        rate = 0;
                        System.out.println("Must be at least 500 miliseconds.");
                    }
                } catch (final NumberFormatException ex) {
                    System.out.println("Error - please try again.");
                }
            } catch (final IOException e) {
                System.out.println(e.toString());
            }
        }

        // Run logic based on mode
        switch(mode) {
            case "useItems":
                UseItem useItem = new UseItem();
                useItem.run(rate, debugMode, keyToPress, timeToWait);
            case "alch":
                Alch alch = new Alch();
                alch.run(rate, debugMode, keyToPress, timeToWait);
            default:
                System.out.println("Incorrect mode provided");
        }
    }
}
