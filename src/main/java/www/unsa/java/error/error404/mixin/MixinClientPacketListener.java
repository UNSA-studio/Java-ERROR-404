package www.unsa.java.error.error404.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundBundlePacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import www.unsa.java.error.error404.util.PacketDropHelper;

@Mixin(ClientPacketListener.class)
public class MixinClientPacketListener {

    @Inject(method = "handleBundlePacket", at = @At("HEAD"), cancellable = true)
    private void onBundle(ClientboundBundlePacket packet, CallbackInfo ci) {
        if (PacketDropHelper.consumeDrop()) {
            ci.cancel();
            Minecraft.getInstance().execute(() -> {
                throw new RuntimeException("Internal Exception: io.netty.handler.codec.DecoderException: java.io.IOException: Packet was discarded");
            });
        }
    }
}
