package com.automations;

import java.awt.*;
import java.awt.event.InputEvent;
import java.util.Random;

public class Alch implements Automation {
    int rate;
    boolean debugMode;

    public Alch(int rate, boolean debugMode) {
        this.rate = rate;
        this.debugMode = debugMode;
    }

    public void run() {
        try {
            final Robot robot = new Robot();
            while (true) {
                final Random r = new Random();
                Integer sleep = r.nextInt(this.rate) + 500;
                if (this.debugMode) {
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
