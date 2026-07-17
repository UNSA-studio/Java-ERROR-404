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
            case "Ordinary" -> "Data Marker";
            case "Data Marker" -> "Data Analysis";
            case "Data Analysis" -> "Overload";
            case "Overload" -> "Nothing";
            default -> "Ordinary";
        };
        setMode(stack, next);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (entity instanceof Player player) {
            String mode = getMode(stack);
            if (mode.equals("Ordinary") || mode.equals("Overload")) {
                if (!level.isClientSide) {
                    CrashHelper.crashJvm("ClassNotFoundException: Java not found");
                }
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
