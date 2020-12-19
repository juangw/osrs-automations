package com.automations;

public interface Automation {
    void run(int rate, boolean debugMode, String keyToPress, int timeToWait);
}
