package com.automations;

import java.awt.*;
import java.awt.event.InputEvent;
import java.util.Random;

public class CleanHerb implements Automation {
    int rate;
    boolean debugMode;

    public CleanHerb(int rate, boolean debugMode) {
        this.rate = rate;
        this.debugMode = debugMode;
    }

    public void run() {
        try {
            final Robot robot = new Robot();
            while (true) {
                final Random r = new Random();
                Integer sleep = r.nextInt(200) + this.rate;
                try {
                    Thread.sleep(sleep);
                } catch (final InterruptedException ex) {
                    System.out.println("Script stopped");
                }

                Point point = getCurrentPoint();
                System.out.println(point.getX());
                System.out.println(point.getY());

                if (this.debugMode) {
                    continue;
                }

                cleanInventory(robot);

                // Set random movement times
                Integer movementTimeTo;

                // left-click on banker
                movementTimeTo = r.nextInt(200) + 1000;
                click(robot, "left", 903, 370, 0, 1, movementTimeTo);

                // Deposit all items
                movementTimeTo = r.nextInt(200) + 1000;
                click(robot, "left", 1025, 845, 10, 10, movementTimeTo);

                // Get first item
                movementTimeTo = r.nextInt(200) + 1000;
                click(robot, "left", 807, 520, 10, 10, movementTimeTo);

                // Exit bank menu
                movementTimeTo = r.nextInt(200) + 500;
                click(robot, "left", 1060, 87, 5, 5, movementTimeTo);

                movementTimeTo = r.nextInt(200) + 1000;
                Point currentPoint = getCurrentPoint();
                mouseGlide(robot, (int) currentPoint.getX(), (int) currentPoint.getY(), (int) point.getX(),
                        (int) point.getY(), movementTimeTo, 100);
            }
        } catch (final AWTException e) {
            System.out.println(e.toString());
        }
    }

    private static void cleanInventory(Robot robot) {
        final Random r = new Random();
        Point startingPoint = getCurrentPoint();
        Integer movementTimeTo = r.nextInt(100) + 100;
        // Move mouse to first inventory tile
        mouseGlide(robot, (int) startingPoint.getX(), (int) startingPoint.getY(), 1720, 798, movementTimeTo, 100);

        // clean items for all rows
        Point currentPoint = getCurrentPoint();
        Integer verticalPoint = (int) currentPoint.getY();
        Integer originalXPoint = (int) currentPoint.getX();
        Integer endRow = 6;
        for (int y = 0; y <= endRow; y++) {

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
            if (y != endRow) {
                mouseGlide(robot, (int) yPoint.getX(), (int) yPoint.getY(), originalXPoint, verticalPoint, movementTimeTo, 100);
            }
        }
    }

    private static Point getCurrentPoint() {
        final PointerInfo pointer = MouseInfo.getPointerInfo();
        final Point point = pointer.getLocation();
        return point;
    }

    public static void click(Robot robot, String type, int xCoord, int yCoord, int maxXCoord, int maxYCoord,
                             int movementTime) {
        final Random r = new Random();
        int xNewOffset = 0;
        int yNewOffset = 0;
        if (maxXCoord != 0) {
            xNewOffset = r.nextInt(maxXCoord);
        }
        if (maxYCoord != 0) {
            yNewOffset = r.nextInt(maxYCoord);
        }

        // Move pixels to the right with random maxXCoord
        final int randomXCoord = xCoord + xNewOffset;
        // Move pixels up or down with random maxYCoord
        final int randomYCoord = yCoord + yNewOffset;

        Point startPoint = getCurrentPoint();
        int startXCoord = (int) startPoint.getX();
        int startYCoord = (int) startPoint.getY();
        mouseGlide(robot, startXCoord, startYCoord, randomXCoord, randomYCoord, movementTime, 100);

        if (type.equals("left")) {
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            try {
                Thread.sleep(100);
            } catch (final InterruptedException e) {
                System.out.println(e.toString());
            }
        } else if (type.equals("right")) {
            robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
            robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
        }
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
