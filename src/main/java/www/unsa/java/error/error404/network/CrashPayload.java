package www.unsa.java.error.error404.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import www.unsa.java.error.error404.JavaError404;
import www.unsa.java.error.error404.util.CrashHelper;

public record CrashPayload() implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<CrashPayload> TYPE =
        new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(JavaError404.MODID, "crash"));
    public static final StreamCodec<ByteBuf, CrashPayload> STREAM_CODEC = StreamCodec.unit(new CrashPayload());

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static void handle(CrashPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> CrashHelper.crashJvm("Remote crash triggered"));
    }
}
