package com.automations;

import java.io.*;

public class Automate {

    private static String mode;
    private static Integer rate;
    private static String keyToPress;
    private static Integer timeToWait;
    private static Boolean debugMode;

    public static void main(final String[] args) {
        // Used to determine automation method
        mode = System.getenv("MODE");

        // Used only in useItem, can provide bogus values otherwise
        keyToPress = System.getenv("USE_KEY_TO_PRESS");
        String waitTime = System.getenv("USE_WAIT_TIME_SECONDS");
        timeToWait = Integer.parseInt(waitTime);

        // Used to debug
        String isDebug = System.getenv("DEBUG");
        debugMode = Boolean.parseBoolean(isDebug);

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
