package com.example;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class NoSprintResettingServer implements ModInitializer {
    @Override
    public void onInitialize() {
        NoSprintResettingServerConfig.load();
        PayloadTypeRegistry.playS2C().register(ServerOptInPayload.TYPE, ServerOptInPayload.CODEC);

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayNetworking.send(handler.player,
                new ServerOptInPayload(NoSprintResettingServerConfig.serverOptIn));
        });
    }
}
