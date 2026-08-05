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

/**
 * 必须数据通道 —— 服务端持续向客户端发送无用的"必须数据"。
 * 正常情况下数据被静默接收；当剪刀激活丢包后，该通道的包被视为"丢失"，
 * 客户端因收到损坏/缺失的必须数据包而触发原生断连画面。
 */
public record RequiredDataPayload(int sequence, byte[] data) implements CustomPacketPayload {
    private static final Random RANDOM = new Random();

    public static final Type&lt;RequiredDataPayload&gt; TYPE =
        new Type&lt;&gt;(ResourceLocation.fromNamespaceAndPath(JavaError404.MODID, "required_data"));

    /** 剪刀丢包标记，被设置为 true 后下一个 RequiredDataPayload 到达时触发断连 */
    public static volatile boolean dropActive = false;

    public static void activateDrop() { dropActive = true; }

    public static RequiredDataPayload create(int sequence) {
        byte[] data = new byte[64 + RANDOM.nextInt(192)];
        RANDOM.nextBytes(data);
        return new RequiredDataPayload(sequence, data);
    }

    public static final StreamCodec&lt;RegistryFriendlyByteBuf, RequiredDataPayload&gt; STREAM_CODEC =
        new StreamCodec&lt;&gt;() {
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
    public Type&lt;? extends CustomPacketPayload&gt; type() { return TYPE; }

    public static void handle(RequiredDataPayload payload, IPayloadContext context) {
        if (dropActive) {
            dropActive = false;
            context.enqueueWork(() -&gt; {
                Minecraft mc = Minecraft.getInstance();
                if (mc.getConnection() != null) {
                    // 走 vanilla Connection 错误路径，
                    // 生成真实断连画面："Internal Exception: io.netty.handler.codec.DecoderException: java.io.IOException: 远程主机强迫关闭了一个现有的连接。"
                    mc.getConnection().getConnection().exceptionCaught(null,
                        new DecoderException("java.io.IOException: 远程主机强迫关闭了一个现有的连接。"));
                }
            });
        }
    }
}