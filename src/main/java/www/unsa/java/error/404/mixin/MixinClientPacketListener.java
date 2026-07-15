package www.unsa.java.error.404.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundBundlePacket;
import net.minecraft.network.protocol.game.ClientboundDisconnectPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class MixinClientPacketListener {

    // 通过丢弃特定包来模拟连接丢失，具体丢弃逻辑可通过静态标志位控制
    @Inject(method = "handleBundlePacket", at = @At("HEAD"), cancellable = true)
    private void onBundle(ClientboundBundlePacket packet, CallbackInfo ci) {
        if (PacketDropper.shouldDrop()) {
            ci.cancel();
        }
    }

    // 如果需要，也可以拦截断开连接包
    public static class PacketDropper {
        private static boolean drop = false;

        public static void activateDrop() {
            drop = true;
        }

        public static boolean shouldDrop() {
            if (drop) {
                drop = false;
                return true;
            }
            return false;
        }
    }
}
