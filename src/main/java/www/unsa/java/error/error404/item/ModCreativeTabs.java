package www.unsa.java.error.error404.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredRegister;
import www.unsa.java.error.error404.JavaError404;

import java.util.function.Supplier;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> TABS =
        DeferredRegister.create(Registries.CREATIVE_MODE_TAB, JavaError404.MODID);

    public static final Supplier<CreativeModeTab> JAVA_ERROR_TAB = TABS.register("java_error_tab",
        () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.java_error_404"))
            .icon(() -> new ItemStack(ModItems.JAVA.get()))
            .displayItems((params, output) -> {
                // 添加所有模组物品
                output.accept(ModItems.JAVA_NETWORK_PACKET.get());
                output.accept(ModItems.SCISSORS.get());
                output.accept(ModItems.JAVA.get());
                // 异常物品
                output.accept(ModItems.NULL_POINTER.get());
                output.accept(ModItems.CLASS_CAST.get());
                output.accept(ModItems.ARRAY_INDEX.get());
                output.accept(ModItems.STRING_INDEX.get());
                output.accept(ModItems.ILLEGAL_ARGUMENT.get());
                output.accept(ModItems.ILLEGAL_STATE.get());
                output.accept(ModItems.NUMBER_FORMAT.get());
                output.accept(ModItems.ARITHMETIC.get());
                output.accept(ModItems.NEGATIVE_ARRAY_SIZE.get());
                output.accept(ModItems.CLASS_NOT_FOUND.get());
                output.accept(ModItems.NO_CLASS_DEF.get());
                output.accept(ModItems.NO_SUCH_METHOD.get());
                output.accept(ModItems.NO_SUCH_FIELD.get());
                output.accept(ModItems.OUT_OF_MEMORY.get());
                output.accept(ModItems.STACK_OVERFLOW.get());
                output.accept(ModItems.UNSUPPORTED_OP.get());
                output.accept(ModItems.INTERRUPTED.get());
                output.accept(ModItems.EXCEPTION_IN_INIT.get());
                output.accept(ModItems.SECURITY.get());
                output.accept(ModItems.ILLEGAL_ACCESS.get());
                output.accept(ModItems.INSTANTIATION.get());
                output.accept(ModItems.CONCURRENT_MOD.get());
                output.accept(ModItems.NO_SUCH_ELEMENT.get());
                output.accept(ModItems.EMPTY_STACK.get());
                output.accept(ModItems.TOO_MANY_LISTENERS.get());
                output.accept(ModItems.MISSING_RESOURCE.get());
                output.accept(ModItems.INPUT_MISMATCH.get());
                output.accept(ModItems.ILLEGAL_FORMAT.get());
                output.accept(ModItems.INVALID_PROPERTIES.get());
                output.accept(ModItems.IO_EXCEPTION.get());
                output.accept(ModItems.FILE_NOT_FOUND.get());
                output.accept(ModItems.EOF_EXCEPTION.get());
                output.accept(ModItems.UTF_DATA.get());
                output.accept(ModItems.NOT_SERIALIZABLE.get());
                output.accept(ModItems.STREAM_CORRUPTED.get());
                output.accept(ModItems.INTERRUPTED_IO.get());
                output.accept(ModItems.BUFFER_OVERFLOW.get());
                output.accept(ModItems.BUFFER_UNDERFLOW.get());
                output.accept(ModItems.READ_ONLY_BUFFER.get());
                output.accept(ModItems.FILE_SYSTEM.get());
                output.accept(ModItems.CLOSED_CHANNEL.get());
                output.accept(ModItems.CLOSED_FILE_SYSTEM.get());
                output.accept(ModItems.SOCKET.get());
                output.accept(ModItems.UNKNOWN_HOST.get());
                output.accept(ModItems.CONNECT.get());
                output.accept(ModItems.BIND.get());
                output.accept(ModItems.MALFORMED_URL.get());
                output.accept(ModItems.HTTP_RETRY.get());
            })
            .build()
    );
}
