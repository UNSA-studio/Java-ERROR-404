package www.unsa.java.error.error404.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import www.unsa.java.error.error404.network.CrashType;

public class ExceptionItem extends Item {
    public static final String TAG_CRASH_TYPE = "java_error_404_death_crash_type";
    public static final String TAG_CAUSES_CRASH = "java_error_404_death_causes_crash";
    public static final String TAG_IS_SUICIDE = "java_error_404_death_suicide";

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
            CompoundTag data = player.getPersistentData();
            data.putString(TAG_CRASH_TYPE, crashType.name());
            data.putBoolean(TAG_CAUSES_CRASH, causesCrash);
            data.putBoolean(TAG_IS_SUICIDE, true);
            player.hurt(player.damageSources().genericKill(), Float.MAX_VALUE);
            return InteractionResultHolder.consume(player.getItemInHand(hand));
        }
        return super.use(level, player, hand);
    }
}
