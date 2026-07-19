package www.unsa.java.error.error404.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import www.unsa.java.error.error404.JavaError404;

public record ClientboundCrashPacket(CrashType crashType) implements CustomPacketPayload {
    public static final Type<ClientboundCrashPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(JavaError404.MODID, "crash"));

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundCrashPacket> STREAM_CODEC = StreamCodec.of(
        (buf, packet) -> buf.writeUtf(packet.crashType().name()),
        buf -> new ClientboundCrashPacket(CrashType.valueOf(buf.readUtf()))
    );

    public static void handle(ClientboundCrashPacket payload, IPayloadContext context) {
        context.enqueueWork(() -> payload.crashType().execute());
    }
}
