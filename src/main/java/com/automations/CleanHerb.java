package com.automations;

import java.awt.AWTException;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.util.Random;
import java.util.logging.Logger;

public class CleanHerb implements Automation {

  private final static Logger LOGGER = Logger.getLogger(CleanHerb.class.getName());

  int rate;
  boolean debugMode;

  public CleanHerb(int rate, boolean debugMode) {
    this.rate = rate;
    this.debugMode = debugMode;
  }

  private static void cleanInventory(Robot robot) throws InterruptedException {
    final Random r = new Random();
    Point startingPoint = Automation.getCurrentPoint();
    int movementTimeTo = r.nextInt(100) + 100;
    // Move mouse to first inventory tile
    Automation.mouseGlide(robot, (int) startingPoint.getX(), (int) startingPoint.getY(), 1720, 798,
        movementTimeTo, 100);

    // clean items for all rows
    Point currentPoint = Automation.getCurrentPoint();
    int verticalPoint = (int) currentPoint.getY();
    int originalXPoint = (int) currentPoint.getX();
    int endRow = 6;
    for (int y = 0; y <= endRow; y++) {

      // Drop items in row
      for (int x = 0; x <= 3; x++) {
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        // Wait for click to register
        Thread.sleep(200);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        Point xPoint = Automation.getCurrentPoint();
        int newXPoint = (int) xPoint.getX() + 40;

        if (x != 3) {
          Automation.mouseGlide(robot, (int) xPoint.getX(), (int) xPoint.getY(), newXPoint,
              verticalPoint, movementTimeTo, 100);
        }
      }

      // Go to next row and reset horizontal movement
      verticalPoint += 38;
      Point yPoint = Automation.getCurrentPoint();
      if (y != endRow) {
        Automation.mouseGlide(robot, (int) yPoint.getX(), (int) yPoint.getY(), originalXPoint,
            verticalPoint, movementTimeTo, 100);
      }
    }
  }

  public static void click(Robot robot, String type, int xCoord, int yCoord, int maxXCoord,
      int maxYCoord,
      int movementTime) throws InterruptedException {
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

    Point startPoint = Automation.getCurrentPoint();
    int startXCoord = (int) startPoint.getX();
    int startYCoord = (int) startPoint.getY();
    Automation.mouseGlide(robot, startXCoord, startYCoord, randomXCoord, randomYCoord, movementTime,
        100);

    if (type.equals("left")) {
      robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
      robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
      Thread.sleep(100);
    } else if (type.equals("right")) {
      robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
      robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
    }
  }

  public void run() {
    try {
      final Robot robot = new Robot();
      while (true) {
        final Random r = new Random();
        int sleep = r.nextInt(200) + this.rate;
        Thread.sleep(sleep);

        Point point = Automation.getCurrentPoint();
        LOGGER.info(String.valueOf(point.getX()));
        LOGGER.info(String.valueOf(point.getY()));

        if (this.debugMode) {
          continue;
        }

        cleanInventory(robot);

        // Set random movement times
        int movementTimeTo;

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
        Point currentPoint = Automation.getCurrentPoint();
        Automation.mouseGlide(robot, (int) currentPoint.getX(), (int) currentPoint.getY(),
            (int) point.getX(),
            (int) point.getY(), movementTimeTo, 100);
      }
    } catch (final AWTException e) {
      LOGGER.info(e.toString());
    } catch (final InterruptedException ex) {
      LOGGER.info("Script stopped");
    }
  }
}
