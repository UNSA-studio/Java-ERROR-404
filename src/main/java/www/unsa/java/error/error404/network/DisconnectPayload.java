package www.unsa.java.error.error404.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import www.unsa.java.error.error404.JavaError404;

public record DisconnectPayload(String reason) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<DisconnectPayload> TYPE =
        new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(JavaError404.MODID, "disconnect"));

    public static final StreamCodec<ByteBuf, DisconnectPayload> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public DisconnectPayload decode(ByteBuf buf) {
            int len = buf.readInt();
            byte[] bytes = new byte[len];
            buf.readBytes(bytes);
            String reason = new String(bytes, java.nio.charset.StandardCharsets.UTF_8);
            return new DisconnectPayload(reason);
        }

        @Override
        public void encode(ByteBuf buf, DisconnectPayload payload) {
            byte[] bytes = payload.reason.getBytes(java.nio.charset.StandardCharsets.UTF_8);
            buf.writeInt(bytes.length);
            buf.writeBytes(bytes);
        }
    };

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static void handle(DisconnectPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player().level().isClientSide) {
                Minecraft.getInstance().player.connection.getConnection().disconnect(Component.literal(payload.reason));
            }
        });
    }
}
