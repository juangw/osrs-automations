package com.automations;

public interface Automation {
    public void run(int rate, boolean debugMode, String keyToPress, int timeToWait);
}
