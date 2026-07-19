package www.unsa.java.error.error404.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import www.unsa.java.error.error404.network.CrashType;

public class ExceptionItem extends Item {
    private final CrashType crashType;
    private final boolean causesCrash;

    public ExceptionItem(CrashType crashType, boolean causesCrash) {
        super(new Properties().stacksTo(1));
        this.crashType = crashType;
        this.causesCrash = causesCrash;
    }

    public CrashType getCrashType() { return crashType; }
    public boolean isCausesCrash() { return causesCrash; }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (player.isCrouching()) {
            player.hurt(player.damageSources().genericKill(), Float.MAX_VALUE);
            return InteractionResultHolder.consume(player.getItemInHand(hand));
        }
        return super.use(level, player, hand);
    }
}
