package www.unsa.java.error.error404.item;

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
    public static final String MODE_ORDINARY = "Ordinary";
    public static final String MODE_DATA_MARKER = "Data Marker";
    public static final String MODE_DATA_ANALYSIS = "Data Analysis";
    public static final String MODE_OVERLORD = "Overlord";
    public static final String MODE_NOTHING = "Nothing";

    public JavaItem(Properties properties) {
        super(properties.food(new FoodProperties.Builder().alwaysEdible().nutrition(0).saturationModifier(0).build()));
    }

    public static String getMode(ItemStack stack) {
        return stack.getOrDefault(ModDataComponents.MODE.get(), MODE_ORDINARY);
    }

    public static void setMode(ItemStack stack, String mode) {
        stack.set(ModDataComponents.MODE.get(), mode);
    }

    public static void nextMode(ItemStack stack) {
        String current = getMode(stack);
        String next = switch (current) {
            case MODE_ORDINARY -> MODE_DATA_MARKER;
            case MODE_DATA_MARKER -> MODE_DATA_ANALYSIS;
            case MODE_DATA_ANALYSIS -> MODE_OVERLORD;
            case MODE_OVERLORD -> MODE_NOTHING;
            default -> MODE_ORDINARY;
        };
        setMode(stack, next);
    }

    /** 当前模式是否允许主动扫描矿物 */
    public static boolean canScanMinerals(String mode) {
        return mode.equals(MODE_DATA_MARKER) || mode.equals(MODE_OVERLORD);
    }

    /** 当前模式是否允许主动扫描生物 */
    public static boolean canScanEntities(String mode) {
        return mode.equals(MODE_DATA_ANALYSIS) || mode.equals(MODE_OVERLORD);
    }

    /** 当前模式是否可食用 */
    public static boolean canEat(String mode) {
        return mode.equals(MODE_ORDINARY) || mode.equals(MODE_OVERLORD);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!canEat(getMode(stack))) {
            // 非食用模式：清除 food 组件，右键无任何反应
            return InteractionResultHolder.fail(stack);
        }
        return super.use(level, player, hand);
    }

    @Override
    public boolean isEdible() {
        // 无法直接访问 ItemStack，交给 use() 拦截；此处保持 true 让 Ordinary/Overlord 正常
        return true;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (entity instanceof Player player) {
            String mode = getMode(stack);
            if (mode.equals(MODE_ORDINARY) || mode.equals(MODE_OVERLORD)) {
                CrashHelper.crashJvm(() -> {
                    throw new UnsatisfiedLinkError("Unable to load library 'java': java.lang.UnsatisfiedLinkError");
                });
            }
        }
        return super.finishUsingItem(stack, level, entity);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("Mode: " + getMode(stack)));
    }
}