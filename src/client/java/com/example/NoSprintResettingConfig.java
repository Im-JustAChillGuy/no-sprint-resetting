package com.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class NoSprintResettingConfig {
    public static boolean enabled = true;
    public static boolean disableWhileSneaking = true;
    public static boolean disableInWater = true;
    public static boolean disableWhileTakingDamage = true;
    public static boolean disableWhileEating = true;
    public static int minHungerThreshold = 6;

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance()
            .getConfigDir().resolve("nosprintresetting.json");

    public static boolean isEnabled() { return enabled; }
    public static void setEnabled(boolean value) { enabled = value; save(); }

    public static void load() {
        if (!Files.exists(CONFIG_PATH)) {
            save();
            return;
        }
        try {
            String json = Files.readString(CONFIG_PATH);
            ConfigData data = GSON.fromJson(json, ConfigData.class);
            if (data != null) {
                enabled = data.enabled;
                disableWhileSneaking = data.disableWhileSneaking;
                disableInWater = data.disableInWater;
                disableWhileTakingDamage = data.disableWhileTakingDamage;
                disableWhileEating = data.disableWhileEating;
                minHungerThreshold = data.minHungerThreshold;
            }
        } catch (IOException e) {
            System.err.println("[NoSprintResetting] Failed to load config: " + e.getMessage());
        }
    }

    public static void save() {
        try {
            Files.writeString(CONFIG_PATH, GSON.toJson(new ConfigData()));
        } catch (IOException e) {
            System.err.println("[NoSprintResetting] Failed to save config: " + e.getMessage());
        }
    }

    private static class ConfigData {
        boolean enabled = NoSprintResettingConfig.enabled;
        boolean disableWhileSneaking = NoSprintResettingConfig.disableWhileSneaking;
        boolean disableInWater = NoSprintResettingConfig.disableInWater;
        boolean disableWhileTakingDamage = NoSprintResettingConfig.disableWhileTakingDamage;
        boolean disableWhileEating = NoSprintResettingConfig.disableWhileEating;
        int minHungerThreshold = NoSprintResettingConfig.minHungerThreshold;
    }
}
