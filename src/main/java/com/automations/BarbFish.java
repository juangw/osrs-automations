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
            final Point startingPoint = getCurrentPoint();
            while (true) {
                final Random r = new Random();

                // Run logic to click on fishing spot
                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                // Wait for key press to register
                try {
                    Thread.sleep(500);
                } catch (final InterruptedException ex) {
                    System.out.println("Script stopped");
                }
                robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

                // Sleep while filling up inventory
                try {
                    Thread.sleep(this.rate);
                } catch (final InterruptedException ex) {
                    System.out.println("Script stopped");
                }

                if (this.debugMode) {
                    // Get current location
                    final Point currentPoint = getCurrentPoint();
                    System.out.println(currentPoint.getX());
                    System.out.println(currentPoint.getY());
                    System.out.println("running in debug mode");
                    continue;
                }

                // Drop everything in inventory not in first row
                dropInventory(robot);

                // Reset mouse position
                final Point currentPoint = getCurrentPoint();
                Integer movementTimeTo = r.nextInt(200) + 500;
                mouseGlide(robot, (int) currentPoint.getX(), (int) currentPoint.getY(), (int) startingPoint.getX(), (int) startingPoint.getY(), movementTimeTo, 200);
            }
        } catch (final AWTException e) {
            System.out.println(e.toString());
        }
    }

    private static Point getCurrentPoint() {
        final PointerInfo pointer = MouseInfo.getPointerInfo();
        final Point point = pointer.getLocation();
        return point;
    }

    private static void dropInventory(Robot robot) {
        final Random r = new Random();
        Point startingPoint = getCurrentPoint();
        Integer movementTimeTo = r.nextInt(100) + 100;
        // Move mouse to first inventory tile
        mouseGlide(robot, (int) startingPoint.getX(), (int) startingPoint.getY(), 1720, 835, movementTimeTo, 100);
        robot.keyPress(KeyEvent.VK_SHIFT);
        // Wait for key press to register
        try {
            Thread.sleep(500);
        } catch (final InterruptedException ex) {
            System.out.println("Script stopped");
        }

        // Drop items for all rows
        Point currentPoint = getCurrentPoint();
        Integer verticalPoint = (int) currentPoint.getY();
        Integer originalXPoint = (int) currentPoint.getX();
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
                Point xPoint = getCurrentPoint();
                Integer newXPoint = (int) xPoint.getX() + 40;

                if (x != 3) {
                    mouseGlide(robot, (int) xPoint.getX(), (int) xPoint.getY(), newXPoint, verticalPoint, movementTimeTo, 100);
                }
            }

            // Go to next row and reset horizontal movement
            verticalPoint += 38;
            Point yPoint = getCurrentPoint();
            if (y != 5) {
                mouseGlide(robot, (int) yPoint.getX(), (int) yPoint.getY(), originalXPoint, verticalPoint, movementTimeTo, 100);
            }
        }

        robot.keyRelease(KeyEvent.VK_SHIFT);
    }

    private static void mouseGlide(Robot robot, int x1, int y1, int x2, int y2,
                                  final int t, final int n) {
        try {
            final double dx = (x2 - x1) / ((double) n);
            final double dy = (y2 - y1) / ((double) n);
            final double dt = t / ((double) n);
            for (int step = 1; step <= n; step++) {
                Thread.sleep((int) dt);
                robot.mouseMove((int) (x1 + dx * step), (int) (y1 + dy * step));
            }
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
    }
}
