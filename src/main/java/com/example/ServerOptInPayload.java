package com.example;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record ServerOptInPayload(boolean optIn) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ServerOptInPayload> ID =
        new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("nosprintresetting", "server_opt_in"));
    public static final StreamCodec<FriendlyByteBuf, ServerOptInPayload> CODEC =
        StreamCodec.of(
            (buf, value) -> buf.writeBoolean(value.optIn()),
            buf -> new ServerOptInPayload(buf.readBoolean())
        );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
