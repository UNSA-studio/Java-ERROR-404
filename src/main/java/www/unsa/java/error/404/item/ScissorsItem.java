package www.unsa.java.error.404.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import www.unsa.java.error.404.util.CrashHelper;

public class ScissorsItem extends Item {
    public ScissorsItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        // 左键空气在客户端完成，这里只处理右键实体（在事件中）
        return InteractionResult.PASS;
    }
}
