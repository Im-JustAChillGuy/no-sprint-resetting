package com.example.server;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public class NoSprintResettingServer implements ModInitializer {

    // Must match the IDs in NoSprintResettingClient exactly
    public record OptInRequestPayload() implements CustomPacketPayload {
        public static final Type<OptInRequestPayload> TYPE =
                new Type<>(ResourceLocation.fromNamespaceAndPath("nosprintresetting", "opt_in_request"));
        public static final StreamCodec<FriendlyByteBuf, OptInRequestPayload> CODEC =
                StreamCodec.unit(new OptInRequestPayload());
        @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
    }

    public record OptInGrantedPayload() implements CustomPacketPayload {
        public static final Type<OptInGrantedPayload> TYPE =
                new Type<>(ResourceLocation.fromNamespaceAndPath("nosprintresetting", "opt_in_granted"));
        public static final StreamCodec<FriendlyByteBuf, OptInGrantedPayload> CODEC =
                StreamCodec.unit(new OptInGrantedPayload());
        @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
    }

    @Override
    public void onInitialize() {
        PayloadTypeRegistry.serverboundPlay().register(OptInRequestPayload.TYPE, OptInRequestPayload.CODEC);
        PayloadTypeRegistry.clientboundPlay().register(OptInGrantedPayload.TYPE, OptInGrantedPayload.CODEC);

        // When a client asks, reply with a grant
        ServerPlayNetworking.registerGlobalReceiver(OptInRequestPayload.TYPE, (payload, context) -> {
            context.responseSender().sendPacket(new OptInGrantedPayload());
        });
    }
}
