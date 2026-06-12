package com.example;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record ServerOptInPayload(boolean optIn) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ServerOptInPayload> TYPE =
        new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("nosprintresetting", "server_opt_in"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ServerOptInPayload> CODEC =
        StreamCodec.of(
            (buf, value) -> buf.writeBoolean(value.optIn()),
            buf -> new ServerOptInPayload(buf.readBoolean())
        );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
