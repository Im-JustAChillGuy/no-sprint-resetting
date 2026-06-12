package com.example;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class NoSprintResettingClient implements ClientModInitializer {

    public static boolean serverOptIn = true; // Default true for singleplayer

    private float lastHealth = -1f;
    private boolean wasDamaged = false;
    private int damageCooldown = 0;
    private static final int DAMAGE_COOLDOWN_TICKS = 10;

    @Override
    public void onInitializeClient() {
        NoSprintResettingConfig.load();

        // Listen for server opt-in packet
        ClientPlayNetworking.registerGlobalReceiver(ServerOptInPayload.ID, (payload, context) -> {
            serverOptIn = payload.optIn();
        });

        // Reset to true when disconnecting (for singleplayer default)
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            serverOptIn = true;
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;

            float currentHealth = client.player.getHealth();
            if (lastHealth > 0 && currentHealth < lastHealth) {
                wasDamaged = true;
                damageCooldown = DAMAGE_COOLDOWN_TICKS;
            }
            lastHealth = currentHealth;

            if (damageCooldown > 0) {
                damageCooldown--;
                if (damageCooldown == 0) wasDamaged = false;
            }

            if (!NoSprintResettingConfig.enabled) return;
            if (!serverOptIn) return; // Respect server opt-in
            if (NoSprintResettingConfig.disableWhileSneaking && client.player.isShiftKeyDown()) return;
            if (NoSprintResettingConfig.disableInWater && client.player.isInWater()) return;
            if (NoSprintResettingConfig.disableWhileTakingDamage && wasDamaged) return;
            if (NoSprintResettingConfig.disableWhileEating && client.player.isUsingItem()) return;
            if (client.player.getFoodData().getFoodLevel() <= NoSprintResettingConfig.minHungerThreshold) return;

            client.player.setSprinting(true);
        });
    }
}
