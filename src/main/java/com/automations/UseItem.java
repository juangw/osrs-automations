package com.automations;

import java.awt.AWTException;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Random;
import java.util.logging.Logger;

public class UseItem implements Automation {

  private final static Logger LOGGER = Logger.getLogger(UseItem.class.getName());

  int rate;
  boolean debugMode;
  String keyToPress;
  int timeToWait;
  int itemCount;

  public UseItem(int rate, boolean debugMode, String keyToPress, int timeToWait, int itemCount) {
    this.rate = rate;
    this.debugMode = debugMode;
    this.keyToPress = keyToPress;
    this.timeToWait = timeToWait;
    this.itemCount = itemCount;
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

  public static void useRight(Robot robot, Point startingPoint, int[] offsets) {
    final Random r = new Random();
    // Use initial item by right clicking
    robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
    robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    int startingXCoord;
    int startingYCoord;
    if (offsets[0] == 0 && offsets[1] == 0) {
      startingXCoord = (int) startingPoint.getX();
      startingYCoord = (int) startingPoint.getY();
    } else {
      Point currentPoint = Automation.getCurrentPoint();
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
    Automation.mouseGlide(robot, startingXCoord, startingYCoord, randomXCoord, randomYCoord,
        movementTimeTo, 50);

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
    Automation.mouseGlide(robot, randomXCoord, randomYCoord, ReturnXCoord, ReturnYCoord,
        movementTimeBack, 50);
  }

  public void run() {
    try {
      final Robot robot = new Robot();
      // Get starting location
      final Point startingPoint = Automation.getCurrentPoint();

      int[] offsets = {0, 0};
      while (true) {
        final Random r = new Random();
        int sleep = r.nextInt(this.rate) + 200;
        Thread.sleep(sleep);

        Point point = Automation.getCurrentPoint();
        LOGGER.info(String.valueOf(point.getX()));
        LOGGER.info(String.valueOf(point.getY()));

        if (this.debugMode) {
          continue;
        }
        useRight(robot, startingPoint, offsets);

        // Press desired key from panel
        int waitTime = r.nextInt(200) + 1000;
        Thread.sleep(waitTime);

        switch (this.keyToPress) {
          case "space":
            robot.keyPress(KeyEvent.VK_SPACE);
            robot.keyRelease(KeyEvent.VK_SPACE);
            break;
          case "6":
            robot.keyPress(KeyEvent.VK_6);
            robot.keyRelease(KeyEvent.VK_6);
            break;
          default:
            LOGGER.info("Invalid key to press passed in");
        }

        // Wait for completion
        int sleepTime = r.nextInt(1000) + this.timeToWait;
        Thread.sleep(sleepTime);

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

        // Get second item
        movementTimeTo = r.nextInt(200) + 750;
        click(robot, "left", 854, 520, 5, 5, movementTimeTo);

        if (this.itemCount >= 3) {
          // Get third item
          movementTimeTo = r.nextInt(200) + 750;
          click(robot, "left", 900, 520, 5, 5, movementTimeTo);
        }
        if (this.itemCount >= 4) {
          // Get third item
          movementTimeTo = r.nextInt(200) + 750;
          click(robot, "left", 950, 520, 5, 5, movementTimeTo);
        }

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