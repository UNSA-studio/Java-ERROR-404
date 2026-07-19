package www.unsa.java.error.error404.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import www.unsa.java.error.error404.JavaError404;
import www.unsa.java.error.error404.mixin.MixinClientPacketListener;

public record ActivatePacketDropPacket() implements CustomPacketPayload {
    public static final Type<ActivatePacketDropPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(JavaError404.MODID, "activate_drop"));
    public static final StreamCodec<ByteBuf, ActivatePacketDropPacket> STREAM_CODEC = StreamCodec.unit(new ActivatePacketDropPacket());

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static void handle(ActivatePacketDropPacket payload, IPayloadContext context) {
        context.enqueueWork(() -> MixinClientPacketListener.activateDrop());
    }
}
