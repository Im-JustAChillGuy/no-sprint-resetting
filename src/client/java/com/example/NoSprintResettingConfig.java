package com.example;

public class NoSprintResettingConfig {
    public static boolean enabled = true;
    public static boolean disableWhileSneaking = true;

    public static boolean isEnabled() {
        return enabled;
    }

    public static void setEnabled(boolean value) {
        enabled = value;
    }
}
