package com.example;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class NoSprintResettingClient implements ClientModInitializer {

    private float lastHealth = -1f;
    private boolean wasDamaged = false;
    private int damageCooldown = 0;
    private static final int DAMAGE_COOLDOWN_TICKS = 10;

    @Override
    public void onInitializeClient() {
        NoSprintResettingConfig.load();

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
            if (NoSprintResettingConfig.disableWhileSneaking && client.player.isSneaking()) return;
            if (NoSprintResettingConfig.disableInWater && client.player.isTouchingWater()) return;
            if (NoSprintResettingConfig.disableWhileTakingDamage && wasDamaged) return;
            if (NoSprintResettingConfig.disableWhileEating && client.player.isUsingItem()) return;
            if (client.player.getHungerManager().getFoodLevel() <= NoSprintResettingConfig.minHungerThreshold) return;

            client.player.setSprinting(true);
        });
    }
}
