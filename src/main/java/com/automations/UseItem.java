package com.automations;

import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class UseItem implements Automation {

    public void run(int rate, boolean debugMode, String keyToPress, int timeToWait) {
        try {
            final Robot robot = new Robot();
            // Get starting location
            final Point startingPoint = getCurrentPoint();

            Integer[] offsets = {0, 0};
            while (true) {
                final Random r = new Random();
                Integer sleep = r.nextInt(rate) + 200;
                try {
                    Thread.sleep(sleep);
                } catch (final InterruptedException ex) {
                    System.out.println("Script stopped");
                }

                Point point = getCurrentPoint();
                System.out.println(point.getX());
                System.out.println(point.getY());

                // Set random movement times
                Integer movementTimeTo = r.nextInt(200) + 1000;

                if (debugMode) {
                    continue;
                }
                useRightAndReturnOffsets(robot, startingPoint, offsets);

                // Press desired key from panel
                Integer waitTime = r.nextInt(200) + 1000;
                try {
                    Thread.sleep(waitTime);
                } catch (final InterruptedException ex) {
                    System.out.println("Script stopped");
                }
                switch (keyToPress) {
                    case "space":
                        robot.keyPress(KeyEvent.VK_SPACE);
                        robot.keyRelease(KeyEvent.VK_SPACE);
                        break;
                    case "6":
                        robot.keyPress(KeyEvent.VK_6);
                        robot.keyRelease(KeyEvent.VK_6);
                        break;
                    default:
                        System.out.println("Invalid key to press passed in");
                }

                // Wait for completion
                Integer sleepTime = r.nextInt(1000) + timeToWait;
                try {
                    Thread.sleep(sleepTime);
                } catch (final InterruptedException ex) {
                    System.out.println("Script stopped");
                }

                // Right-click on banker
                movementTimeTo = r.nextInt(200) + 1000;
                click(robot, "left", 880, 550, 0, 1, movementTimeTo);

                // Deposit all items
                movementTimeTo = r.nextInt(200) + 1000;
                click(robot, "left", 1025, 845, 10, 10, movementTimeTo);

                // Get first item
                movementTimeTo = r.nextInt(200) + 1000;
                click(robot, "left", 835, 520, 10, 10, movementTimeTo);

                // Get second item
                movementTimeTo = r.nextInt(200) + 500;
                click(robot, "left", 870, 520, 5, 5, movementTimeTo);

                // Exit bank menu
                movementTimeTo = r.nextInt(200) + 500;
                click(robot, "left", 1078, 99, 5, 5, movementTimeTo);

                movementTimeTo = r.nextInt(200) + 1000;
                Point currentPoint = getCurrentPoint();
                mouseGlide(robot, (int) currentPoint.getX(), (int) currentPoint.getY(), (int) point.getX(),
                        (int) point.getY(), movementTimeTo, 100);
            }
        } catch (final AWTException e) {
            System.out.println(e.toString());
        }
    }

    public static Point getCurrentPoint() {
        final PointerInfo pointer = MouseInfo.getPointerInfo();
        final Point point = pointer.getLocation();
        return point;
    }

    public static void mouseGlide(Robot robot, int x1, int y1, int x2, int y2,
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
        } else if (type.equals("right")) {
            robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
            robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
        }
    }

    public static Integer[] useRightAndReturnOffsets(Robot robot, Point startingPoint, Integer[] offsets) {
        final Random r = new Random();
        // Use initial item by right clicking
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        int startingXCoord = 0;
        int startingYCoord = 0;
        if (offsets[0] == 0 && offsets[1] == 0) {
            startingXCoord = (int) startingPoint.getX();
            startingYCoord = (int) startingPoint.getY();
        } else {
            Point currentPoint = getCurrentPoint();
            startingXCoord = (int) currentPoint.getX() - offsets[0];
            startingYCoord = (int) currentPoint.getY() - offsets[1];
        }

        final int xNewOffset = r.nextInt(10);
        final int yNewOffset = r.nextInt(10) - 5;

        // Move 40 pixels to the right with random offset
        final int randomXCoord = startingXCoord + xNewOffset + 40;
        // Move 0 pixels up or down with random offset
        final int randomYCoord = startingYCoord + yNewOffset;

        // Move the cursor to new location
        final int movementTimeTo = r.nextInt(200) + 100;
        mouseGlide(robot, startingXCoord, startingYCoord, randomXCoord, randomYCoord, movementTimeTo, 50);

        // Use on second item by right clicking
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

        // Move the cursor to original location with new offsets
        final int xReturnOffset = r.nextInt(10);
        final int yReturnOffset = r.nextInt(10) - 5;

        // Move back to the left with random offset
        final int ReturnXCoord = startingXCoord + xReturnOffset;
        final int ReturnYCoord = startingYCoord + yReturnOffset;
        final int movementTimeBack = r.nextInt(200) + 100;
        mouseGlide(robot, randomXCoord, randomYCoord, ReturnXCoord, ReturnYCoord, movementTimeBack, 50);

        Integer[] returnOffsets = { xReturnOffset, yReturnOffset };
        return returnOffsets;
    }
}