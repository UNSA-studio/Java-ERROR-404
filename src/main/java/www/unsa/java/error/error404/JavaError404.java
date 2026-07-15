package www.unsa.java.error.error404;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import www.unsa.java.error.error404.item.ModItems;

@Mod(JavaError404.MODID)
public class JavaError404 {
    public static final String MODID = "java_error_404";

    public JavaError404(IEventBus modEventBus) {
        ModItems.ITEMS.register(modEventBus);
    }
}
