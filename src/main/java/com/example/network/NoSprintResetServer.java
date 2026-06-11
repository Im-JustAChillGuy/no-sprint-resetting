package com.yourname.noSprintResetServer;

import com.yourname.noSprintReset.network.SprintOptInPackets;
import com.yourname.noSprintReset.network.SprintOptInPackets.SprintOptInGrantedPayload;
import com.yourname.noSprintReset.network.SprintOptInPackets.SprintOptInRequestPayload;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

/**
 * Companion server-side mod for No Sprint Reset.
 *
 * Server admins install this mod to explicitly opt their server in to
 * allowing the client mod's sprint-reset suppression.
 *
 * When a client sends a SprintOptInRequestPayload, this mod replies
 * with SprintOptInGrantedPayload, unlocking the feature on that client
 * for the duration of their session.
 */
public class NoSprintResetServer implements ModInitializer {

    @Override
    public void onInitialize() {
        // Register both payload types on the server side.
        // C2S: we receive client requests; S2C: we send grants.
        PayloadTypeRegistry.playC2S().register(
                SprintOptInRequestPayload.ID,
                SprintOptInRequestPayload.CODEC
        );
        PayloadTypeRegistry.playS2C().register(
                SprintOptInGrantedPayload.ID,
                SprintOptInGrantedPayload.CODEC
        );

        // When a client asks, reply immediately with the grant.
        ServerPlayNetworking.registerGlobalReceiver(
                SprintOptInRequestPayload.ID,
                (payload, context) -> {
                    context.responseSender().sendPacket(new SprintOptInGrantedPayload());
                    System.out.println("[NoSprintResetServer] Granted sprint opt-in to: "
                            + context.player().getName().getString());
                }
        );
    }
}
