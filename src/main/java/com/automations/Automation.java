package com.automations;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Robot;

public interface Automation {

  static Point getCurrentPoint() {
    final PointerInfo pointer = MouseInfo.getPointerInfo();
    return pointer.getLocation();
  }

  static void mouseGlide(Robot robot, int x1, int y1, int x2, int y2,
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

  void run();
}
