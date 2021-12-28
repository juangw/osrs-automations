package com.automations;

import java.awt.*;
import java.awt.event.InputEvent;
import java.util.Random;
import java.util.logging.Logger;

public class Thieve implements Automation {
    private final static Logger LOGGER = Logger.getLogger(Thieve.class.getName());

    int rate;
    boolean debugMode;

    public Thieve(int rate, boolean debugMode) {
        this.rate = rate;
        this.debugMode = debugMode;
    }

    public void run() {
        try {
            final Robot robot = new Robot();
            final Point startingPoint = Automation.getCurrentPoint();
            while (true) {
                final Random r = new Random();

                // Run logic to click on NPC
                clickNPC(robot, r);

                if (this.debugMode) {
                    // Get current location
                    final Point checkPointThree = Automation.getCurrentPoint();
                    LOGGER.info(String.valueOf(checkPointThree.getX()));
                    LOGGER.info(String.valueOf(checkPointThree.getY()));
                    LOGGER.info("running in debug mode");
                    continue;
                }

                // Drop everything in inventory not in first row
                lootPouches(robot, r);

                // Sleep for given rate
                int sleep = r.nextInt(200) + this.rate;
                Thread.sleep(sleep);

                // Reset mouse position
                final Point checkPointFour = Automation.getCurrentPoint();
                int movementTimeTo = r.nextInt(200) + 500;
                Automation.mouseGlide(
                        robot,
                        (int) checkPointFour.getX(),
                        (int) checkPointFour.getY(),
                        (int) startingPoint.getX(),
                        (int) startingPoint.getY(),
                        movementTimeTo,
                        200
                );
            }
        } catch (final AWTException e) {
            LOGGER.info(e.toString());
        } catch (final InterruptedException ex) {
            LOGGER.info("Script stopped");
        }
    }

    private static void clickNPC(Robot robot, Random r) throws InterruptedException {
        long currentTime = System.currentTimeMillis();
        long end = currentTime + 30000 + r.nextInt(500);
        while (System.currentTimeMillis() < end) {
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            // Wait for key press to register
            Thread.sleep(500);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            Thread.sleep(r.nextInt(200) + 200);
        }
    }

    private static void lootPouches(Robot robot, Random r) throws InterruptedException {
        Point startingPoint = Automation.getCurrentPoint();
        int movementTimeTo = r.nextInt(100) + 100;
        // Move mouse to first inventory tile
        Automation.mouseGlide(robot, (int) startingPoint.getX(), (int) startingPoint.getY(), 1720, 835, movementTimeTo, 100);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        // Wait for key press to register
        Thread.sleep(500);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }
}
