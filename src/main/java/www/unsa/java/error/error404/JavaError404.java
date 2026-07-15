package www.unsa.java.error.error404;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import www.unsa.java.error.error404.item.ModDataComponents;
import www.unsa.java.error.error404.item.ModItems;
import www.unsa.java.error.error404.network.CrashPayload;
import www.unsa.java.error.error404.network.DisconnectPayload;

@Mod(JavaError404.MODID)
public class JavaError404 {
    public static final String MODID = "java.error.404";

    public JavaError404(IEventBus modEventBus) {
        ModItems.ITEMS.register(modEventBus);
        ModDataComponents.REGISTRY.register(modEventBus);
        modEventBus.addListener(this::onRegisterPayloads);
    }

    private void onRegisterPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(MODID);
        registrar.playToClient(CrashPayload.TYPE, CrashPayload.STREAM_CODEC, CrashPayload::handle);
        registrar.playToClient(DisconnectPayload.TYPE, DisconnectPayload.STREAM_CODEC, DisconnectPayload::handle);
    }
}
