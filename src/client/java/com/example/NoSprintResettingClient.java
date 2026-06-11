package com.example;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public class NoSprintResettingClient implements ClientModInitializer {

    // ── Packet: client → server ("do you allow this mod?") ──────────────────
    public record OptInRequestPayload() implements CustomPacketPayload {
        public static final Type<OptInRequestPayload> TYPE =
                new Type<>(ResourceLocation.fromNamespaceAndPath("nosprintresetting", "opt_in_request"));
        public static final StreamCodec<FriendlyByteBuf, OptInRequestPayload> CODEC =
                StreamCodec.unit(new OptInRequestPayload());
        @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
    }

    // ── Packet: server → client ("yes, you're allowed") ─────────────────────
    public record OptInGrantedPayload() implements CustomPacketPayload {
        public static final Type<OptInGrantedPayload> TYPE =
                new Type<>(ResourceLocation.fromNamespaceAndPath("nosprintresetting", "opt_in_granted"));
        public static final StreamCodec<FriendlyByteBuf, OptInGrantedPayload> CODEC =
                StreamCodec.unit(new OptInGrantedPayload());
        @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
    }

    // Whether the current server has opted in. Off by default.
    private static volatile boolean serverOptedIn = false;

    // ── Damage tracking (unchanged from original) ────────────────────────────
    private float lastHealth = -1f;
    private boolean wasDamaged = false;
    private int damageCooldown = 0;
    private static final int DAMAGE_COOLDOWN_TICKS = 10;

    @Override
    public void onInitializeClient() {
        NoSprintResettingConfig.load();

        // Register packet types
        PayloadTypeRegistry.serverboundPlay().register(OptInRequestPayload.TYPE, OptInRequestPayload.CODEC);
        PayloadTypeRegistry.clientboundPlay().register(OptInGrantedPayload.TYPE, OptInGrantedPayload.CODEC);

        // When the server grants opt-in, enable the mod for this session
        ClientPlayNetworking.registerGlobalReceiver(OptInGrantedPayload.TYPE, (payload, context) -> {
            serverOptedIn = true;
        });

        // On every server join: reset state, then ask the server if it opts in
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            serverOptedIn = false;
            sender.sendPacket(new OptInRequestPayload());
        });

        // Clean up on disconnect
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            serverOptedIn = false;
        });

        // Tick logic — unchanged, just guarded by serverOptedIn
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;

            // Damage detection
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

            // ── Opt-in gate ──────────────────────────────────────────────────
            if (!serverOptedIn) return;

            if (!NoSprintResettingConfig.enabled) return;

            if (NoSprintResettingConfig.disableWhileSneaking && client.player.isShiftKeyDown()) return;
            if (NoSprintResettingConfig.disableInWater && client.player.isInWater()) return;
            if (NoSprintResettingConfig.disableWhileTakingDamage && wasDamaged) return;
            if (NoSprintResettingConfig.disableWhileEating && client.player.isUsingItem()) return;
            if (client.player.getFoodData().getFoodLevel() <= NoSprintResettingConfig.minHungerThreshold) return;

            client.player.setSprinting(true);
        });
    }
}
