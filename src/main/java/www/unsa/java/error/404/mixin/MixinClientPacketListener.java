package www.unsa.java.error.404.mixin;

import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundBundlePacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class MixinClientPacketListener {

    // 拦截所有数据包，直接return导致连接丢失
    @Inject(method = "handleBundlePacket", at = @At("HEAD"), cancellable = true)
    private void onPacket(ClientboundBundlePacket packet, CallbackInfo ci) {
        // 当模组需要断开连接时，这里会丢弃包
        // 具体控制逻辑可通过静态标志位实现，此处简化
    }
}
