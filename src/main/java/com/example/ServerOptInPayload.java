package com.example;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record ServerOptInPayload(boolean optIn) implements CustomPayload {
    public static final CustomPayload.Id<ServerOptInPayload> ID =
        new CustomPayload.Id<>(Identifier.of("nosprintresetting", "server_opt_in"));
    public static final PacketCodec<PacketByteBuf, ServerOptInPayload> CODEC =
        PacketCodec.of(
            (buf, value) -> buf.writeBoolean(value.optIn()),
            buf -> new ServerOptInPayload(buf.readBoolean())
        );

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}
