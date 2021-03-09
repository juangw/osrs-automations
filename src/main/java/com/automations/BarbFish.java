package com.automations;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Random;

public class BarbFish implements Automation {
    int rate;
    boolean debugMode;

    public BarbFish(int rate, boolean debugMode) {
        this.rate = rate;
        this.debugMode = debugMode;
    }

    public void run() {
        try {
            final Robot robot = new Robot();
            final Point startingPoint = Automation.getCurrentPoint();
            while (true) {
                final Random r = new Random();

                // Run logic to click on fishing spot
                robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
                robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
                final Point checkPointOne = Automation.getCurrentPoint();
                int movementTimeDown = r.nextInt(200) + 1000;
                Automation.mouseGlide(robot, (int) checkPointOne.getX(), (int) checkPointOne.getY(), (int) startingPoint.getX(), (int) startingPoint.getY() + 30, movementTimeDown, 200);
                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                // Wait for key press to register
                try {
                    Thread.sleep(500);
                } catch (final InterruptedException ex) {
                    System.out.println("Script stopped");
                }
                robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

                final Point checkPointTwo = Automation.getCurrentPoint();
                Automation.mouseGlide(robot, (int) checkPointTwo.getX(), (int) checkPointTwo.getY(), (int) checkPointOne.getX(), (int) checkPointOne.getY(), movementTimeDown, 200);
                robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
                robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
                Automation.mouseGlide(robot, (int) checkPointOne.getX(), (int) checkPointOne.getY(), (int) startingPoint.getX(), (int) startingPoint.getY() + 30, movementTimeDown, 200);
                // Sleep while filling up inventory
                try {
                    Thread.sleep(this.rate);
                } catch (final InterruptedException ex) {
                    System.out.println("Script stopped");
                }
                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

                if (this.debugMode) {
                    // Get current location
                    final Point checkPointThree = Automation.getCurrentPoint();
                    System.out.println(checkPointThree.getX());
                    System.out.println(checkPointThree.getY());
                    System.out.println("running in debug mode");
                    continue;
                }

                // Drop everything in inventory not in first row
                dropInventory(robot);

                // Reset mouse position
                final Point checkPointFour = Automation.getCurrentPoint();
                int movementTimeTo = r.nextInt(200) + 500;
                Automation.mouseGlide(robot, (int) checkPointFour.getX(), (int) checkPointFour.getY(), (int) startingPoint.getX(), (int) startingPoint.getY(), movementTimeTo, 200);
            }
        } catch (final AWTException e) {
            System.out.println(e.toString());
        }
    }

    private static void dropInventory(Robot robot) {
        final Random r = new Random();
        Point startingPoint = Automation.getCurrentPoint();
        int movementTimeTo = r.nextInt(100) + 100;
        // Move mouse to first inventory tile
        Automation.mouseGlide(robot, (int) startingPoint.getX(), (int) startingPoint.getY(), 1720, 835, movementTimeTo, 100);
        robot.keyPress(KeyEvent.VK_SHIFT);
        // Wait for key press to register
        try {
            Thread.sleep(500);
        } catch (final InterruptedException ex) {
            System.out.println("Script stopped");
        }

        // Drop items for all rows
        Point currentPoint = Automation.getCurrentPoint();
        int verticalPoint = (int) currentPoint.getY();
        int originalXPoint = (int) currentPoint.getX();
        for (int y = 0; y <= 5; y++) {

            // Drop items in row
            for (int x = 0; x <= 3; x++) {
                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                // Wait for click to register
                try {
                    Thread.sleep(200);
                } catch (final InterruptedException ex) {
                    System.out.println("Script stopped");
                }
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
