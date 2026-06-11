package com.example..network;
 
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
 
/**
 * Packet definitions shared conceptually between client and server.
 *
 * Flow:
 *   1. Client joins → sends SprintOptInRequestPayload to server ("do you allow this mod?")
 *   2. Server (if companion mod is installed) → sends SprintOptInGrantedPayload back ("yes")
 *   3. Client enables sprint-reset suppression only after receiving the grant.
 *
 * If the server never responds (vanilla or no companion mod), the client stays disabled.
 */
public final class SprintOptInPackets {
 
    // Namespace this to your mod id
    public static final String MOD_ID = "no_sprint_reset";
 
    // ── C2S: Client asks the server if it opts in ────────────────────────────
 
    public record SprintOptInRequestPayload() implements CustomPayload {
 
        public static final Id<SprintOptInRequestPayload> ID =
                new Id<>(Identifier.of(MOD_ID, "opt_in_request"));
 
        public static final PacketCodec<PacketByteBuf, SprintOptInRequestPayload> CODEC =
                PacketCodec.unit(new SprintOptInRequestPayload());
 
        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }
 
    // ── S2C: Server grants opt-in to this client ─────────────────────────────
 
    public record SprintOptInGrantedPayload() implements CustomPayload {
 
        public static final Id<SprintOptInGrantedPayload> ID =
                new Id<>(Identifier.of(MOD_ID, "opt_in_granted"));
 
        public static final PacketCodec<PacketByteBuf, SprintOptInGrantedPayload> CODEC =
                PacketCodec.unit(new SprintOptInGrantedPayload());
 
        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }
 
    private SprintOptInPackets() {}
}
