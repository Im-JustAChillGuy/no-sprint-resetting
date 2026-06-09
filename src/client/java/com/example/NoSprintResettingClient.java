package com.example;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.EndTick;

public class NoSprintResettingClient implements ClientModInitializer {

    // Track last health to detect damage
    private float lastHealth = -1f;
    private boolean wasDamaged = false;
    private int damageCooldown = 0;
    private static final int DAMAGE_COOLDOWN_TICKS = 10;

    @Override
    public void onInitializeClient() {
        NoSprintResettingConfig.load();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;

            // Damage detection - compare health each tick
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

            // Check each condition
            if (NoSprintResettingConfig.disableWhileSneaking && client.player.isShiftKeyDown()) return;
            if (NoSprintResettingConfig.disableInWater && client.player.isInWater()) return;
            if (NoSprintResettingConfig.disableWhileTakingDamage && wasDamaged) return;
            if (NoSprintResettingConfig.disableWhileEating && client.player.isUsingItem()) return;
            if (client.player.getFoodData().getFoodLevel() <= NoSprintResettingConfig.minHungerThreshold) return;

            client.player.setSprinting(true);
        });
    }
}
