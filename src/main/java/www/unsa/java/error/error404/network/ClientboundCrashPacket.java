package www.unsa.java.error.error404.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import www.unsa.java.error.error404.JavaError404;

public record ClientboundCrashPacket(CrashType type) implements CustomPacketPayload {
    public static final Type<ClientboundCrashPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(JavaError404.MODID, "crash"));
    public static final StreamCodec<ByteBuf, ClientboundCrashPacket> STREAM_CODEC = StreamCodec.composite(
        net.minecraft.network.codec.ByteBufCodecs.STRING_UTF8.map(CrashType::valueOf, Enum::name),
        ClientboundCrashPacket::type,
        ClientboundCrashPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static void handle(ClientboundCrashPacket payload, IPayloadContext context) {
        context.enqueueWork(() -> payload.type().execute());
    }
}
