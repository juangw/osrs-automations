package com.automations;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Random;
import java.util.logging.Logger;

public class Mine implements Automation {
    private final static Logger LOGGER = Logger.getLogger(Mine.class.getName());

    int rate;
    boolean debugMode;

    public Mine(int rate, boolean debugMode) {
        this.rate = rate;
        this.debugMode = debugMode;
    }

    public void run() {
        try {
            final Robot robot = new Robot();
            final Random r = new Random();
            final Point startingPoint = Automation.getCurrentPoint();
            while (true) {
                if (this.debugMode) {
                    // Get current location
                    final Point debugCheckpoint = Automation.getCurrentPoint();
                    LOGGER.info(String.valueOf(debugCheckpoint.getX()));
                    LOGGER.info(String.valueOf(debugCheckpoint.getY()));
                    LOGGER.info("running in debug mode");

                    int sleep = r.nextInt(200) + 3000;
                    Thread.sleep(sleep);
                    continue;
                }

                // Run logic
                for (int i = 0; i < 6; i++) {
                    mineThreeRocks(robot, r);
                    Thread.sleep(1500);
                }

                // Drop everything in inventory not in first row
                dropInventory(robot, r);

                // Sleep for given rate
                int sleep = r.nextInt(200) + this.rate;
                Thread.sleep(sleep);
            }
        } catch (final AWTException e) {
            LOGGER.info(e.toString());
        } catch (final InterruptedException ex) {
            LOGGER.info("Script stopped");
        }
    }

    private static void mineThreeRocks(Robot robot, Random r) throws InterruptedException {
        int movementTimeTo = r.nextInt(100) + 300;
        final Point currentPoint = Automation.getCurrentPoint();
        Automation.mouseGlide(robot, (int) currentPoint.getX(), (int) currentPoint.getY(), 980, 430, movementTimeTo, 100);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        // Wait for click to register
        Thread.sleep(200);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

        Thread.sleep(1500);

        Automation.mouseGlide(robot, 980, 430, 1230, 630, movementTimeTo, 100);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        // Wait for click to register
        Thread.sleep(200);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

        Thread.sleep(1500);

        Automation.mouseGlide(robot, 1230, 630, 975, 895, movementTimeTo, 100);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        // Wait for click to register
        Thread.sleep(200);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

        Thread.sleep(1500);
    }

    private static void dropInventory(Robot robot, Random r) throws InterruptedException {
        Point startingPoint = Automation.getCurrentPoint();
        int movementTimeTo = r.nextInt(100) + 100;
        // Move mouse to first inventory tile
        Automation.mouseGlide(robot, (int) startingPoint.getX(), (int) startingPoint.getY(), 1720, 835, movementTimeTo, 100);
        robot.keyPress(KeyEvent.VK_SHIFT);
        // Wait for key press to register
        Thread.sleep(500);

        // Drop items for all rows
        Point currentPoint = Automation.getCurrentPoint();
        int verticalPoint = (int) currentPoint.getY();
        int originalXPoint = (int) currentPoint.getX();
        for (int y = 0; y <= 5; y++) {

            // Drop items in row
            for (int x = 0; x <= 3; x++) {
                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                // Wait for click to register
                Thread.sleep(200);
                robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                Point xPoint = Automation.getCurrentPoint();
                int newXPoint = (int) xPoint.getX() + 40;

                if (x != 3) {
                    Automation.mouseGlide(robot, (int) xPoint.getX(), (int) xPoint.getY(), newXPoint, verticalPoint, movementTimeTo, 100);
                }
            }

            // Go to next row and reset horizontal movement
            verticalPoint += 38;
            Point yPoint = Automation.getCurrentPoint();
            if (y != 5) {
                Automation.mouseGlide(robot, (int) yPoint.getX(), (int) yPoint.getY(), originalXPoint, verticalPoint, movementTimeTo, 100);
            }
        }

        robot.keyRelease(KeyEvent.VK_SHIFT);
    }
}
