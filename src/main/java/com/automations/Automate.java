package com.automations;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Automate {

    private static String mode;
    private static Integer rate;
    private static String keyToPress;
    private static Integer timeToWait;
    private static Boolean debugMode;
    private static Integer itemCount;

    public static void main(final String[] args) {
        // Used to determine automation method
        mode = System.getenv("MODE");

        // Used only in useItem, can provide bogus values otherwise
        keyToPress = System.getenv("USE_KEY_TO_PRESS");
        itemCount = Integer.parseInt(System.getenv("ITEM_COUNT"));
        timeToWait = Integer.parseInt(System.getenv("USE_WAIT_TIME_SECONDS"));

        // Used to debug
        String isDebug = System.getenv("DEBUG");
        debugMode = Boolean.parseBoolean(isDebug);

        // Get input on rate of repetition
        while (rate == null) {
            try {
                System.out.println("Iteration speed of the automation (in milliseconds):");
                final BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                try {
                    rate = Integer.parseInt(in.readLine());
                    if (rate < 500) {
                        rate = 0;
                        System.out.println("Must be at least 500 milliseconds.");
                    }
                } catch (final NumberFormatException ex) {
                    System.out.println("Error - please try again.");
                }
            } catch (final IOException e) {
                System.out.println(e.toString());
            }
        }

        // Run logic based on mode
        Automate automate = new Automate();
        Automation automation = automate.getAutomation(mode);
        if (automation == null) {
            System.out.println(String.format("Unsupported mode provided: %s", mode));
        } else {
            automation.run();
        }
    }

    public Automation getAutomation(String mode){
        if (mode == null){
            return null;
        }
        switch(mode) {
            case "useItems":
                return new UseItem(rate, debugMode, keyToPress, timeToWait, itemCount);
            case "alch":
                return new Alch(rate, debugMode);
            case "barbFish":
                return new BarbFish(rate, debugMode);
            case "cleanHerb":
                return new CleanHerb(rate, debugMode);
            case "thieve":
                return new Thieve(rate, debugMode);
            default:
                return null;
        }
    }
}
