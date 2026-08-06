package www.unsa.java.error.error404.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import www.unsa.java.error.error404.util.CrashHelper;

import java.util.List;

public class JavaItem extends Item {
    public JavaItem(Properties properties) {
        super(properties.food(new FoodProperties.Builder().alwaysEdible().nutrition(0).saturationModifier(0).build()));
    }

    public static String getMode(ItemStack stack) {
        return stack.getOrDefault(ModDataComponents.MODE.get(), "Ordinary");
    }

    public static void setMode(ItemStack stack, String mode) {
        stack.set(ModDataComponents.MODE.get(), mode);
    }

    public static void nextMode(ItemStack stack) {
        String current = getMode(stack);
        String next = switch (current) {
            case "Ordinary" -> "Overload";
            case "Overload" -> "Nothing";
            case "Nothing" -> "Ordinary";
            default -> "Ordinary";
        };
        setMode(stack, next);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (entity instanceof Player player) {
            String mode = getMode(stack);
            switch (mode) {
                case "Ordinary" -> CrashHelper.crashJvm(() -> {
                    throw new UnsatisfiedLinkError("Unable to load library 'java': java.lang.UnsatisfiedLinkError");
                });
                case "Overload" -> {
                    int overloads = stack.getOrDefault(ModDataComponents.OVERLOAD_COUNT.get(), 0) + 1;
                    if (overloads >= 5) {
                        stack.set(ModDataComponents.OVERLOAD_COUNT.get(), 0);
                        CrashHelper.crashJvm(() -> {
                            throw new UnsatisfiedLinkError("Unable to load library 'java': java.lang.UnsatisfiedLinkError");
                        });
                    } else {
                        stack.set(ModDataComponents.OVERLOAD_COUNT.get(), overloads);
                        player.displayClientMessage(Component.literal("Overload " + overloads + "/5"), true);
                    }
                }
                default -> { /* Nothing mode: 无效果 */ }
            }
        }
        return super.finishUsingItem(stack, level, entity);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("Mode: " + getMode(stack)));
        int overloads = stack.getOrDefault(ModDataComponents.OVERLOAD_COUNT.get(), 0);
        if (overloads > 0) tooltip.add(Component.literal("Overloads: " + overloads + "/5"));
    }
}
