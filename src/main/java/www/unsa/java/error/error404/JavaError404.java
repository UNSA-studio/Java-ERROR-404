package www.unsa.java.error.error404;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import www.unsa.java.error.error404.item.ModCreativeTabs;
import www.unsa.java.error.error404.item.ModDataComponents;
import www.unsa.java.error.error404.item.ModItems;
import www.unsa.java.error.error404.network.ActivatePacketDropPacket;
import www.unsa.java.error.error404.network.ClientboundCrashPacket;

@Mod(JavaError404.MODID)
public class JavaError404 {
    public static final String MODID = "java_error_404";

    public JavaError404(IEventBus modEventBus) {
        ModItems.ITEMS.register(modEventBus);
        ModDataComponents.REGISTRY.register(modEventBus);
        ModCreativeTabs.TABS.register(modEventBus);
        modEventBus.addListener(this::registerPayloads);
    }

    private void registerPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(MODID);
        registrar.playToClient(
            ClientboundCrashPacket.TYPE,
            ClientboundCrashPacket.STREAM_CODEC,
            ClientboundCrashPacket::handle
        );
        registrar.playToClient(
            ActivatePacketDropPacket.TYPE,
            ActivatePacketDropPacket.STREAM_CODEC,
            ActivatePacketDropPacket::handle
        );
    }
}
