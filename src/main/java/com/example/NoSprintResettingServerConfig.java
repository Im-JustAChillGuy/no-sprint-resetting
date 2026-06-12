package com.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class NoSprintResettingServerConfig {
    public static boolean serverOptIn = true; 

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance()
            .getConfigDir().resolve("nosprintresetting-server.json");

    public static void load() {
        if (!Files.exists(CONFIG_PATH)) {
            save();
            return;
        }
        try {
            String json = Files.readString(CONFIG_PATH);
            ConfigData data = GSON.fromJson(json, ConfigData.class);
            if (data != null) {
                serverOptIn = data.serverOptIn;
            }
        } catch (IOException e) {
            System.err.println("[NoSprintResetting] Failed to load server config: " + e.getMessage());
        }
    }

    public static void save() {
        try {
            Files.writeString(CONFIG_PATH, GSON.toJson(new ConfigData()));
        } catch (IOException e) {
            System.err.println("[NoSprintResetting] Failed to save server config: " + e.getMessage());
        }
    }

    private static class ConfigData {
        boolean serverOptIn = NoSprintResettingServerConfig.serverOptIn;
    }
}
