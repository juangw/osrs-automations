package com.automations;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Random;
import java.util.logging.Logger;

public class Fish implements Automation {
    private final static Logger LOGGER = Logger.getLogger(Fish.class.getName());

    int rate;
    boolean debugMode;
    String action;

    public Fish(int rate, boolean debugMode, String action) {
        this.rate = rate;
        this.debugMode = debugMode;
        this.action = action;
    }

    public void run() {
        try {
            final Robot robot = new Robot();
            final Point startingPoint = Automation.getCurrentPoint();
            while (true) {
                final Random r = new Random();
                int movementTimeDown = r.nextInt(200) + 1000;

                // Run logic to right click on fishing spot
                robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
                robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
                Automation.mouseGlide(robot, (int) startingPoint.getX(), (int) startingPoint.getY(), (int) startingPoint.getX(), (int) startingPoint.getY() + 30, movementTimeDown, 200);
                Point firstMenuOption = Automation.getCurrentPoint();
                // Sleep while doing action
                Thread.sleep(3000);

                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                // Wait for key press to register
                Thread.sleep(500);
                robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

                Automation.mouseGlide(robot, (int) firstMenuOption.getX(), (int) firstMenuOption.getY(), (int) startingPoint.getX(), (int) startingPoint.getY(), movementTimeDown, 200);
                robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
                robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
                Automation.mouseGlide(robot, (int) startingPoint.getX(), (int) startingPoint.getY(), (int) firstMenuOption.getX(), (int) firstMenuOption.getY(), movementTimeDown, 200);
                // Sleep while filling up inventory
                Thread.sleep(this.rate);
                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

                if (this.debugMode) {
                    // Get current location
                    final Point debugPoint = Automation.getCurrentPoint();
                    LOGGER.info(String.valueOf(debugPoint.getX()));
                    LOGGER.info(String.valueOf(debugPoint.getY()));
                    LOGGER.info("running in debug mode");
                    continue;
                }

                // Sleep while running to next spot
                Thread.sleep(3000);

                // Drop everything in inventory not in first row
                switch(action) {
                    case "drop":
                        dropInventory(robot);
                        break;
                    case "use":
                        useItem(robot);
                        break;
                    default:
                        throw new Exception(String.format("Invalid action provided: %s", action));
                }

                final Point afterAction = Automation.getCurrentPoint();
                Automation.mouseGlide(robot, (int) afterAction.getX(), (int) afterAction.getY(), (int) startingPoint.getX(), (int) startingPoint.getY(), movementTimeDown, 200);
            }
        } catch (final AWTException e) {
            LOGGER.info(e.toString());
        } catch (final InterruptedException ex) {
            LOGGER.info("Script stopped");
        } catch (final Exception e) {
            LOGGER.info(e.toString());
        }
    }

    private void useItem(Robot robot) throws InterruptedException {
        final Random r = new Random();
        Point startingPoint = Automation.getCurrentPoint();
        int movementTimeTo = r.nextInt(100) + 500;
        // Move mouse to first inventory tile
        Automation.mouseGlide(robot, (int) startingPoint.getX(), (int) startingPoint.getY(), 1720, 835, movementTimeTo, 100);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        // Wait for click to register
        Thread.sleep(200);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

        // Move mouse to second inventory tile
        Point firstInventoryPoint = Automation.getCurrentPoint();
        Point secondInventoryPoint = firstInventoryPoint;
        secondInventoryPoint.x = secondInventoryPoint.x + 40;
        Automation.mouseGlide(robot, (int) firstInventoryPoint.getX(), (int) firstInventoryPoint.getY(), (int) secondInventoryPoint.getX(), (int) secondInventoryPoint.getY(), movementTimeTo, 100);

        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        // Wait for click to register
        Thread.sleep(200);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }

    private static void dropInventory(Robot robot) throws InterruptedException {
        final Random r = new Random();
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
