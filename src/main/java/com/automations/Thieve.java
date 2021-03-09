package com.automations;

import java.awt.*;
import java.awt.event.InputEvent;
import java.util.Random;

public class Thieve implements Automation {
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
                    System.out.println(checkPointThree.getX());
                    System.out.println(checkPointThree.getY());
                    System.out.println("running in debug mode");
                    continue;
                }

                // Drop everything in inventory not in first row
                lootPouches(robot, r);

                // Sleep for given rate
                try {
                    int sleep = r.nextInt(200) + this.rate;
                    Thread.sleep(sleep);
                } catch (final InterruptedException ex) {
                    System.out.println("Script stopped");
                }

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
            System.out.println(e.toString());
        }
    }

    private static void clickNPC(Robot robot, Random r) {
        long currentTime = System.currentTimeMillis();
        long end = currentTime + 30000 + r.nextInt(500);
        while (System.currentTimeMillis() < end) {
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            // Wait for key press to register
            try {
                Thread.sleep(500);
            } catch (final InterruptedException ex) {
                System.out.println("Script stopped");
            }
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            try {
                Thread.sleep(r.nextInt(200) + 200);
            } catch (final InterruptedException ex) {
                System.out.println("Script stopped");
            }
        }
    }

    private static void lootPouches(Robot robot, Random r) {
        Point startingPoint = Automation.getCurrentPoint();
        int movementTimeTo = r.nextInt(100) + 100;
        // Move mouse to first inventory tile
        Automation.mouseGlide(robot, (int) startingPoint.getX(), (int) startingPoint.getY(), 1720, 835, movementTimeTo, 100);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        // Wait for key press to register
        try {
            Thread.sleep(500);
        } catch (final InterruptedException ex) {
            System.out.println("Script stopped");
        }
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }
}
