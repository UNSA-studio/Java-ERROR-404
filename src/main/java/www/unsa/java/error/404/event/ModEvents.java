// Fixed
package www.unsa.java.error.404.event;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import www.unsa.java.error.404.JavaError404;
import www.unsa.java.error.404.item.ExceptionItem;
import www.unsa.java.error.404.item.ModItems;
import www.unsa.java.error.404.util.CrashHelper;

import java.util.Random;

@EventBusSubscriber(modid = JavaError404.MODID)
public class ModEvents {
    private static final Random RANDOM = new Random();

    // 处理攻击实体事件：Java秒杀，异常物品秒杀并触发崩溃/断连
    @SubscribeEvent
    public static void onAttackEntity(AttackEntityEvent event) {
        Player attacker = event.getEntity();
        ItemStack weapon = attacker.getMainHandItem();
        if (!(event.getTarget() instanceof LivingEntity target)) return;

        if (weapon.is(ModItems.JAVA.get())) {
            // Java 秒杀
            target.hurt(attacker.damageSources().genericKill(), Float.MAX_VALUE);
            event.setCanceled(true); // 阻止原版伤害事件
        }
        if (weapon.getItem() instanceof ExceptionItem exc) {
            target.hurt(attacker.damageSources().genericKill(), Float.MAX_VALUE);
            event.setCanceled(true);
            if (target instanceof Player victim) {
                // 根据类型触发相应崩溃或断连
                if (exc.isCausesCrash()) {
                    CrashHelper.crashJvm(exc.getExceptionName());
                } else {
                    // 网络错误类：断开连接，显示报错页面
                    if (victim instanceof net.minecraft.client.player.LocalPlayer) {
                        // 客户端自己，直接踢出
                        victim.connection.getConnection().disconnect(Component.literal(exc.getExceptionName()));
                    }
                    // 如果是服务端，可以给玩家发送一个踢出包，但这里简化处理
                }
            }
        }
    }

    // 剪刀右键实体：仅对玩家有效
    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        Player user = event.getEntity();
        ItemStack stack = user.getItemInHand(event.getHand());
        if (stack.is(ModItems.SCISSORS.get())) {
            if (event.getTarget() instanceof Player targetPlayer) {
                ItemStack packet = new ItemStack(ModItems.JAVA_NETWORK_PACKET.get());
                if (!targetPlayer.addItem(packet)) {
                    targetPlayer.drop(packet, false);
                }
                // 目标断开连接（丢包）
                if (targetPlayer instanceof net.minecraft.client.player.LocalPlayer) {
                    targetPlayer.connection.getConnection().disconnect(
                            Component.literal("Connection lost: Packet not received"));
                }
                event.setCanceled(true);
            } else {
                user.displayClientMessage(
                        Component.literal("Unable to intercept the corresponding player network packet"), true);
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
                // 给予网络通信包
                ItemStack packet = new ItemStack(ModItems.JAVA_NETWORK_PACKET.get());
                if (!player.addItem(packet)) {
                    player.drop(packet, false);
                }
                // 断连
                if (!player.level().isClientSide) {
                    player.connection.disconnect(Component.literal("Packet loss: Server stopped sending packets"));
                } else {
                    // 客户端可以直接断开
                    player.connection.getConnection().disconnect(Component.literal("Packet loss"));
                }
            }
        }
    }

    // 死亡消息定制
    @SubscribeEvent
    public static void onDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof Player victim) {
            DamageSource source = event.getSource();
            if (source.getEntity() instanceof Player attacker) {
                ItemStack weapon = attacker.getMainHandItem();
                if (weapon.getItem() instanceof ExceptionItem exc) {
                    Component deathMsg;
                    if (RANDOM.nextFloat() < 0.6) {
                        deathMsg = Component.literal(victim.getName().getString() + " Killed by Java.Error.404");
                    } else {
                        // 随机乱码
                        String gibberish = generateGibberish();
                        deathMsg = Component.literal(victim.getName().getString() + " was killed by " + gibberish);
                    }
                    event.setCanceled(true); // 取消原版死亡消息
                    if (!victim.level().isClientSide) {
                        victim.level().players().forEach(p -> p.sendSystemMessage(deathMsg));
                    }
                }
            } else if (source.getEntity() == null && source == victim.damageSources().genericKill()) {
                // 自杀 (蹲下右键自己)
                if (victim.getMainHandItem().getItem() instanceof ExceptionItem) {
                    Component msg = Component.literal(victim.getName().getString() + " ※ui꧂idᝰ >hy $%e y№u ￡o€n¥ t[i♯?");
                    event.setCanceled(true);
                    if (!victim.level().isClientSide) {
                        victim.level().players().forEach(p -> p.sendSystemMessage(msg));
                    }
                }
            }
        }
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
