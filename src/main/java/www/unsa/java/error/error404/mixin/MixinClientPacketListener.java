package www.unsa.java.error.error404.mixin;

import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundBundlePacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.atomic.AtomicBoolean;

@Mixin(ClientPacketListener.class)
public class MixinClientPacketListener {
    private static final AtomicBoolean shouldDrop = new AtomicBoolean(false);

    public static void activateDrop() {
        shouldDrop.set(true);
    }

    @Inject(method = "handleBundlePacket", at = @At("HEAD"), cancellable = true)
    private void onBundle(ClientboundBundlePacket packet, CallbackInfo ci) {
        if (shouldDrop.getAndSet(false)) {
            ci.cancel();
        }
    }
}
