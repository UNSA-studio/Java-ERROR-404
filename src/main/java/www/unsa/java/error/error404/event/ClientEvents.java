package www.unsa.java.error.error404.event;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import www.unsa.java.error.error404.network.RequiredDataPayload;

@EventBusSubscriber(value = Dist.CLIENT, modid = "java_error_404")
public class ClientEvents {

    @SubscribeEvent
    public static void onClientLogin(ClientPlayerNetworkEvent.LoggingIn event) {
        RequiredDataPayload.dropActive = false;
    }
}