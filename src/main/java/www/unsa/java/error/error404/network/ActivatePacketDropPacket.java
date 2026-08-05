package www.unsa.java.error.error404.network;

import io.netty.handler.codec.DecoderException;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import www.unsa.java.error.error404.JavaError404;

public record ActivatePacketDropPacket() implements CustomPacketPayload {
    public static final Type<ActivatePacketDropPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(JavaError404.MODID, "activate_drop"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ActivatePacketDropPacket> STREAM_CODEC =
        StreamCodec.<RegistryFriendlyByteBuf, ActivatePacketDropPacket>unit(new ActivatePacketDropPacket());

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static void handle(ActivatePacketDropPacket payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.getConnection() != null) {
                // Walk the vanilla Connection error path so the client shows the exact
                // genuine network-error screen: "Internal Exception: io.netty.handler.codec.DecoderException: java.io.IOException: Packet was discarded"
                mc.getConnection().getConnection().exceptionCaught(null, new DecoderException("java.io.IOException: Packet was discarded"));
            }
        });
    }
}
