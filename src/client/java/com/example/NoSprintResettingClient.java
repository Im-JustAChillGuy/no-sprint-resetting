package com.example;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public class NoSprintResettingClient implements ClientModInitializer {

    // ── Packet: client → server ("do you allow this mod?") ──────────────────
    public record OptInRequestPayload() implements CustomPayload {
        public static final Id<OptInRequestPayload> ID =
                new Id<>(Identifier.of("nosprintresetting", "opt_in_request"));
        public static final PacketCodec<PacketByteBuf, OptInRequestPayload> CODEC =
                PacketCodec.unit(new OptInRequestPayload());
        @Override public Id<? extends CustomPayload> getId() { return ID; }
    }

    // ── Packet: server → client ("yes, you're allowed") ─────────────────────
    public record OptInGrantedPayload() implements CustomPayload {
        public static final Id<OptInGrantedPayload> ID =
                new Id<>(Identifier.of("nosprintresetting", "opt_in_granted"));
        public static final PacketCodec<PacketByteBuf, OptInGrantedPayload> CODEC =
                PacketCodec.unit(new OptInGrantedPayload());
        @Override public Id<? extends CustomPayload> getId() { return ID; }
    }

    // Whether the current server has opted in. Off by default.
    private static volatile boolean serverOptedIn = false;

    // ── Damage tracking (unchanged from your original) ───────────────────────
    private float lastHealth = -1f;
    private boolean wasDamaged = false;
    private int damageCooldown = 0;
    private static final int DAMAGE_COOLDOWN_TICKS = 10;

    @Override
    public void onInitializeClient() {
        NoSprintResettingConfig.load();

        // Register packet types
        PayloadTypeRegistry.playC2S().register(OptInRequestPayload.ID, OptInRequestPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(OptInGrantedPayload.ID, OptInGrantedPayload.CODEC);

        // When the server grants opt-in, enable the mod for this session
        ClientPlayNetworking.registerGlobalReceiver(OptInGrantedPayload.ID, (payload, context) -> {
            serverOptedIn = true;
        });

        // On every server join: reset state, then ask the server if it opts in
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            serverOptedIn = false; // always start disabled on a new server
            sender.sendPacket(new OptInRequestPayload());
        });

        // Clean up on disconnect
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            serverOptedIn = false;
        });

        // Tick logic (unchanged from your original, just guarded by serverOptedIn)
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

            // ── Opt-in gate: do nothing if server hasn't opted in ────────────
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
