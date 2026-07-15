package www.unsa.java.error.404.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import www.unsa.java.error.404.util.CrashHelper;

public class ExceptionItem extends Item {
    private final String exceptionName;
    private final boolean causesCrash;

    public ExceptionItem(String exceptionName, boolean causesCrash) {
        super(new Properties().stacksTo(1));
        this.exceptionName = exceptionName;
        this.causesCrash = causesCrash;
    }

    public String getExceptionName() { return exceptionName; }
    public boolean isCausesCrash() { return causesCrash; }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (player.isCrouching()) {
            // 自杀，但不直接 kill，在事件中处理以便自定义消息
            player.hurt(player.damageSources().genericKill(), Float.MAX_VALUE);
            return InteractionResultHolder.consume(player.getItemInHand(hand));
        }
        return super.use(level, player, hand);
    }
}
