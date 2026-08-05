package www.unsa.java.error.error404.event;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import www.unsa.java.error.error404.JavaError404;
import www.unsa.java.error.error404.network.RequiredDataPayload;

@EventBusSubscriber(value = Dist.CLIENT, modid = JavaError404.MODID)
public class ClientEvents {
    @SubscribeEvent
    public static void onClientLogin(ClientPlayerNetworkEvent.LoggingIn event) {
        RequiredDataPayload.dropActive = false;
    }
}
