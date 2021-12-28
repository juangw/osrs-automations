package com.automations;

import java.awt.*;
import java.awt.event.InputEvent;
import java.util.Random;
import java.util.logging.Logger;

public class Alch implements Automation {
    private final static Logger LOGGER = Logger.getLogger(Alch.class.getName());

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
                    LOGGER.info("running in debug mode");
                }
                Thread.sleep(sleep);
                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            }
        } catch (final AWTException e) {
            LOGGER.info(e.toString());
        } catch (final InterruptedException ex) {
            LOGGER.info("Script stopped");
        }
    }
}
