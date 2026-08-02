package www.unsa.java.error.error404.item;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredRegister;
import www.unsa.java.error.error404.JavaError404;

import java.util.function.Supplier;

public class ModDataComponents {
    public static final DeferredRegister<DataComponentType<?>> REGISTRY =
        DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, JavaError404.MODID);

    public static final Supplier<DataComponentType<String>> MODE = REGISTRY.register("mode",
        () -> DataComponentType.<String>builder().persistent(Codec.STRING).build());
    public static final Supplier<DataComponentType<Integer>> OVERLOAD_COUNT = REGISTRY.register("overload_count",
        () -> DataComponentType.<Integer>builder().persistent(Codec.INT).build());
}
