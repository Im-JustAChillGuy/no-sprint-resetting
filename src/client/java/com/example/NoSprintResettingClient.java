package com.example;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class NoSprintResettingClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        NoSprintResettingConfig.load();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (NoSprintResettingConfig.enabled && client.player != null) {
                boolean sneaking = client.player.isShiftKeyDown();
                if (!sneaking || !NoSprintResettingConfig.disableWhileSneaking) {
                    client.player.setSprinting(true);
                }
            }
        });
    }
}
