// Fixed
package www.unsa.java.error.error404.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
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
        super(properties.food(new FoodProperties.Builder().alwaysEdible().nutrition(0).saturationMod(0).build()));
    }

    public static String getMode(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        if (!tag.contains("mode")) tag.putString("mode", "Ordinary");
        return tag.getString("mode");
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
        stack.getOrCreateTag().putString("mode", next);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (entity instanceof Player player) {
            if (getMode(stack).equals("Ordinary") || getMode(stack).equals("Overload")) {
                // 触发 JVM 崩溃：Java 找不到
                if (!level.isClientSide) {
                    CrashHelper.crashJvm("ClassNotFoundException: Java not found");
                }
            }
        }
        return super.finishUsingItem(stack, level, entity);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("Mode: " + getMode(stack)));
        int overloads = stack.getOrCreateTag().getInt("overloadCount");
        if (overloads > 0) tooltip.add(Component.literal("Overloads: " + overloads + "/5"));
    }
}
