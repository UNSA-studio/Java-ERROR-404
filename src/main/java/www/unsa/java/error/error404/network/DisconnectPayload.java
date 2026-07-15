package www.unsa.java.error.error404.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import www.unsa.java.error.error404.JavaError404;

public record DisconnectPayload(String reason) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<DisconnectPayload> TYPE =
        new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(JavaError404.MODID, "disconnect"));
    public static final StreamCodec<ByteBuf, DisconnectPayload> STREAM_CODEC = StreamCodec.composite(
        ByteBufHelper.readUTF, DisconnectPayload::reason,
        DisconnectPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static void handle(DisconnectPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            // 客户端主动断开连接
            if (context.player().level().isClientSide) {
                context.player().connection.getConnection().disconnect(Component.literal(payload.reason));
            }
        });
    }

    // ByteBuf 辅助读 UTF 字符串
    private static class ByteBufHelper {
        static String readUTF(ByteBuf buf) {
            int len = buf.readInt();
            byte[] bytes = new byte[len];
            buf.readBytes(bytes);
            return new String(bytes, java.nio.charset.StandardCharsets.UTF_8);
        }

        static void writeUTF(ByteBuf buf, String str) {
            byte[] bytes = str.getBytes(java.nio.charset.StandardCharsets.UTF_8);
            buf.writeInt(bytes.length);
            buf.writeBytes(bytes);
        }
    }
}
