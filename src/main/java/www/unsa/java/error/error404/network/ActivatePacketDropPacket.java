package www.unsa.java.error.error404.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import www.unsa.java.error.error404.JavaError404;

/**
 * 剪刀激活包 — 仅设置 RequiredDataPayload.dropActive 标记。
 * 真正的断连由 RequiredDataPayload 通道处理（"必须数据丢失"）。
 */
public record ActivatePacketDropPacket() implements CustomPacketPayload {
    public static final Type<ActivatePacketDropPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(JavaError404.MODID, "activate_drop"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ActivatePacketDropPacket> STREAM_CODEC =
        StreamCodec.<RegistryFriendlyByteBuf, ActivatePacketDropPacket>unit(new ActivatePacketDropPacket());

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static void handle(ActivatePacketDropPacket payload, IPayloadContext context) {
        context.enqueueWork(RequiredDataPayload::activateDrop);
    }
}
