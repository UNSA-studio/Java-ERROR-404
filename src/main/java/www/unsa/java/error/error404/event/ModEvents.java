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
import www.unsa.java.error.error404.item.JavaItem;
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

    // ----- 攻击秒杀 -----
    @SubscribeEvent
    public static void onAttackEntity(AttackEntityEvent event) {
        Player attacker = event.getEntity();
        ItemStack weapon = attacker.getMainHandItem();
        if (!(event.getTarget() instanceof LivingEntity target)) return;

        if (weapon.is(ModItems.JAVA.get())) {
            target.hurt(attacker.damageSources().genericKill(), Float.MAX_VALUE);
            event.setCanceled(true);
        }
        if (weapon.getItem() instanceof ExceptionItem) {
            target.hurt(attacker.damageSources().genericKill(), Float.MAX_VALUE);
            event.setCanceled(true);
        }
    }

    // ----- 剪刀：右键玩家给包并2秒后断开 -----
    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        Player user = event.getEntity();
        ItemStack stack = user.getItemInHand(event.getHand());
        if (stack.is(ModItems.SCISSORS.get())) {
            if (event.getTarget() instanceof Player targetPlayer) {
                givePacket(targetPlayer);
                scheduleDisconnect(targetPlayer, "Connection lost: Packet not received", 2000L);
                event.setCanceled(true);
            } else {
                user.displayClientMessage(Component.literal("Unable to intercept the corresponding player network packet"), true);
                event.setCanceled(true);
            }
        }
    }

    // ----- 左键空气：剪刀（10%给包+2秒断开）和 Java 模式切换 -----
    @SubscribeEvent
    public static void onLeftClickEmpty(PlayerInteractEvent.LeftClickEmpty event) {
        Player player = event.getEntity();
        ItemStack stack = player.getItemInHand(event.getHand());

        // 剪刀：对准空气左键
        if (stack.is(ModItems.SCISSORS.get())) {
            if (RANDOM.nextFloat() < 0.1f) {
                givePacket(player);
                scheduleDisconnect(player, "Packet loss: Server stopped sending packets", 2000L);
            }
        }

        // Java 物品：潜行 + 左键空气切换模式
        if (stack.getItem() instanceof JavaItem && player.isCrouching()) {
            JavaItem.nextMode(stack);
            player.displayClientMessage(Component.literal("Switched to " + JavaItem.getMode(stack)), true);
        }
    }

    // 给予网络包（直接进背包，失败则掉落）
    private static void givePacket(Player player) {
        ItemStack packet = new ItemStack(ModItems.JAVA_NETWORK_PACKET.get());
        if (!player.getInventory().add(packet)) {
            player.drop(packet, false);
        }
    }

    // 延迟断连
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

    // ----- 死亡事件：异常物品死亡消息与真实崩溃/断连 -----
    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof Player victim) {
            final ExceptionItem exc;
            boolean isSuicide = false;

            // 判断自杀：伤害源为空且主手是异常物品
            if (victim.getMainHandItem().getItem() instanceof ExceptionItem e
                && event.getSource().getEntity() == null) {
                exc = e;
                isSuicide = true;
            }
            // 判断他杀：攻击者持有异常物品
            else if (event.getSource().getEntity() instanceof Player attacker) {
                ItemStack weapon = attacker.getMainHandItem();
                if (weapon.getItem() instanceof ExceptionItem e) {
                    exc = e;
                } else {
                    exc = null;
                }
            } else {
                exc = null;
            }

            if (exc != null) {
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

                // 取消原版死亡界面
                event.setCanceled(true);

                // 根据异常类型触发效果
                if (exc.isCausesCrash()) {
                    // 延迟 1 秒让死亡消息显示，再真实崩溃
                    final String crashReason = exc.getExceptionName();
                    new Thread(() -> {
                        try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
                        CrashHelper.crashJvm(crashReason);
                    }).start();
                } else {
                    // 立即断连（显示报错页面）
                    if (victim instanceof ServerPlayer sp) {
                        sp.connection.disconnect(Component.literal(exc.getExceptionName()));
                    } else if (victim.level().isClientSide) {
                        Minecraft.getInstance().player.connection.getConnection().disconnect(Component.literal(exc.getExceptionName()));
                    }
                }
            }
        }
    }

    // 乱码生成
    private static String generateGibberish() {
        String chars = "W^y €r℡ y#꧁ %¢i꧂g t%i꧂ ※o №i*?";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append(chars.charAt(RANDOM.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
