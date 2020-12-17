package com.automations;

import java.awt.*;
import java.awt.event.InputEvent;
import java.util.Random;

public class Alch implements Automation {
    public void run(int rate, boolean debugMode, String keyToPress, int timeToWait) {
        try {
            final Robot robot = new Robot();
            while (true) {
                final Random r = new Random();
                Integer sleep = r.nextInt(rate) + 500;
                if (debugMode) {
                    System.out.println("running in debug mode");
                }
                try {
                    Thread.sleep(sleep);
                } catch (final InterruptedException ex) {
                    System.out.println("Script stopped");
                }
                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            }
        } catch (final AWTException e) {
            System.out.println(e.toString());
        }
    }
}
