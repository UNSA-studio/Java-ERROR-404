package www.unsa.java.error.error404.event;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import www.unsa.java.error.error404.JavaError404;
import www.unsa.java.error.error404.item.ExceptionItem;
import www.unsa.java.error.error404.item.ModItems;
import www.unsa.java.error.error404.network.CrashPayload;
import www.unsa.java.error.error404.network.DisconnectPayload;
import www.unsa.java.error.error404.util.CrashHelper;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

@EventBusSubscriber(modid = JavaError404.MODID)
public class ModEvents {
    private static final Random RANDOM = new Random();

    // 攻击秒杀
    @SubscribeEvent
    public static void onAttackEntity(AttackEntityEvent event) {
        Player attacker = event.getEntity();
        ItemStack weapon = attacker.getMainHandItem();
        if (!(event.getTarget() instanceof LivingEntity target)) return;

        if (weapon.is(ModItems.JAVA.get())) {
            target.hurt(attacker.damageSources().genericKill(), Float.MAX_VALUE);
            event.setCanceled(true);
        }
        if (weapon.getItem() instanceof ExceptionItem exc) {
            target.hurt(attacker.damageSources().genericKill(), Float.MAX_VALUE);
            event.setCanceled(true);
        }
    }

    // 剪刀右键玩家
    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        Player user = event.getEntity();
        ItemStack stack = user.getItemInHand(event.getHand());
        if (stack.is(ModItems.SCISSORS.get())) {
            if (event.getTarget() instanceof Player targetPlayer) {
                ItemStack packet = new ItemStack(ModItems.JAVA_NETWORK_PACKET.get());
                // 直接放进背包
                if (!targetPlayer.getInventory().add(packet)) {
                    targetPlayer.drop(packet, false);
                }
                // 2 秒后断开
                scheduleDisconnect(targetPlayer, "Connection lost: Packet not received", 2000L);
                event.setCanceled(true);
            } else {
                user.displayClientMessage(Component.literal("Unable to intercept the corresponding player network packet"), true);
                event.setCanceled(true);
            }
        }
    }

    // 剪刀左键空气
    @SubscribeEvent
    public static void onLeftClickEmpty(PlayerInteractEvent.LeftClickEmpty event) {
        Player player = event.getEntity();
        ItemStack stack = player.getItemInHand(event.getHand());
        if (stack.is(ModItems.SCISSORS.get())) {
            if (RANDOM.nextFloat() < 0.1f) {
                ItemStack packet = new ItemStack(ModItems.JAVA_NETWORK_PACKET.get());
                if (!player.getInventory().add(packet)) {
                    player.drop(packet, false);
                }
                // 2 秒后断开
                scheduleDisconnect(player, "Packet loss: Server stopped sending packets", 2000L);
            }
        }
    }

    private static void scheduleDisconnect(Player player, String reason, long delay) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (player instanceof ServerPlayer sp) {
                    sp.connection.disconnect(Component.literal(reason));
                } else if (player.level().isClientSide) {
                    Minecraft.getInstance().player.connection.getConnection().disconnect(Component.literal(reason));
                }
            }
        }, delay);
    }

    // 死亡事件：处理异常物品的崩溃/断连与乱码消息
    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof Player victim) {
            // 自杀：伤害源为自己且主手持有异常物品
            if (victim.getMainHandItem().getItem() instanceof ExceptionItem exc
                && event.getSource().getEntity() == null) {
                applyExceptionEffect(victim, exc, true, event);
            }
            // 他杀：攻击者持有异常物品
            else if (event.getSource().getEntity() instanceof Player attacker) {
                ItemStack weapon = attacker.getMainHandItem();
                if (weapon.getItem() instanceof ExceptionItem exc) {
                    applyExceptionEffect(victim, exc, false, event);
                }
            }
        }
    }

    private static void applyExceptionEffect(Player victim, ExceptionItem exc, boolean isSuicide, LivingDeathEvent event) {
        // 发送死亡消息
        Component deathMsg;
        if (isSuicide) {
            deathMsg = Component.literal(victim.getName().getString() + " ※ui꧂idᝰ >hy $%e y№u ￡o€n¥ t[i♯?");
        } else {
            if (RANDOM.nextFloat() < 0.6) {
                deathMsg = Component.literal(victim.getName().getString() + " Killed by Java.Error.404");
            } else {
                deathMsg = Component.literal(victim.getName().getString() + " was killed by " + generateGibberish());
            }
        }
        victim.level().players().forEach(p -> p.sendSystemMessage(deathMsg));

        // 根据异常类型触发效果
        if (exc.isCausesCrash()) {
            // JVM 致命崩溃
            CrashHelper.crashJvm(exc.getExceptionName());
        } else {
            // 网络错误：断开连接
            if (victim instanceof ServerPlayer sp) {
                sp.connection.disconnect(Component.literal(exc.getExceptionName()));
            } else if (victim.level().isClientSide) {
                Minecraft.getInstance().player.connection.getConnection().disconnect(Component.literal(exc.getExceptionName()));
            }
        }
        event.setCanceled(true);
    }

    private static String generateGibberish() {
        String chars = "W^y €r℡ y#꧁ %¢i꧂g t%i꧂ ※o №i*?";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append(chars.charAt(RANDOM.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
