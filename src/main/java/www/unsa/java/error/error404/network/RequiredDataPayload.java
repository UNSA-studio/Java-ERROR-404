package www.unsa.java.error.error404.network;

import io.netty.handler.codec.DecoderException;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import www.unsa.java.error.error404.JavaError404;

import java.util.Random;

public record RequiredDataPayload(int sequence, byte[] data) implements CustomPacketPayload {
    private static final Random RANDOM = new Random();

    public static final Type<RequiredDataPayload> TYPE =
        new Type<>(ResourceLocation.fromNamespaceAndPath(JavaError404.MODID, "required_data"));

    public static volatile boolean dropActive = false;

    public static RequiredDataPayload create(int sequence) {
        byte[] data = new byte[64 + RANDOM.nextInt(192)];
        RANDOM.nextBytes(data);
        return new RequiredDataPayload(sequence, data);
    }

    public static final StreamCodec<RegistryFriendlyByteBuf, RequiredDataPayload> STREAM_CODEC =
        new StreamCodec<>() {
            @Override
            public RequiredDataPayload decode(RegistryFriendlyByteBuf buf) {
                int seq = buf.readVarInt();
                int len = buf.readVarInt();
                byte[] data = new byte[len];
                buf.readBytes(data);
                return new RequiredDataPayload(seq, data);
            }
            @Override
            public void encode(RegistryFriendlyByteBuf buf, RequiredDataPayload p) {
                buf.writeVarInt(p.sequence());
                buf.writeVarInt(p.data().length);
                buf.writeBytes(p.data());
            }
        };

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static void handle(RequiredDataPayload payload, IPayloadContext context) {
        if (dropActive) {
            dropActive = false;
            context.enqueueWork(() -> {
                Minecraft mc = Minecraft.getInstance();
                if (mc.getConnection() != null) {
                    mc.getConnection().getConnection().exceptionCaught(null,
                        new DecoderException("java.io.IOException: 远程主机强迫关闭了一个现有的连接。"));
                }
            });
        }
    }
}