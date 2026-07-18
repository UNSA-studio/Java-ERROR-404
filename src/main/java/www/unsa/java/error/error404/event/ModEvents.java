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

    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        Player user = event.getEntity();
        ItemStack stack = user.getItemInHand(event.getHand());
        if (stack.is(ModItems.SCISSORS.get())) {
            if (event.getTarget() instanceof Player targetPlayer) {
                givePacketAndSave(targetPlayer);
                scheduleDisconnect(targetPlayer, "Connection lost: Packet not received", 2000L);
                event.setCanceled(true);
            } else {
                user.displayClientMessage(Component.literal("Unable to intercept the corresponding player network packet"), true);
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onLeftClickEmpty(PlayerInteractEvent.LeftClickEmpty event) {
        Player player = event.getEntity();
        ItemStack stack = player.getItemInHand(event.getHand());

        if (stack.is(ModItems.SCISSORS.get())) {
            if (RANDOM.nextFloat() < 0.1f) {
                givePacketAndSave(player);
                scheduleDisconnect(player, "Packet loss: Server stopped sending packets", 2000L);
            }
        }

        if (stack.getItem() instanceof JavaItem && player.isCrouching()) {
            JavaItem.nextMode(stack);
            player.displayClientMessage(Component.literal("Switched to " + JavaItem.getMode(stack)), true);
        }
    }

    private static void givePacketAndSave(Player player) {
        ItemStack packet = new ItemStack(ModItems.JAVA_NETWORK_PACKET.get());
        if (!player.getInventory().add(packet)) {
            player.drop(packet, false);
        }
        player.getInventory().setChanged();
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

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof Player victim) {
            final ExceptionItem exc;
            boolean isSuicide = false;

            if (victim.getMainHandItem().getItem() instanceof ExceptionItem e
                && event.getSource().getEntity() == null) {
                exc = e;
                isSuicide = true;
            } else if (event.getSource().getEntity() instanceof Player attacker) {
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
                event.setCanceled(true);

                // 根据异常类型触发效果
                if (exc.isCausesCrash()) {
                    // 致命异常：需要真实崩溃
                    if (victim.level().isClientSide) {
                        // 单人游戏或客户端，直接在本地抛出 Error 崩溃
                        CrashHelper.crashJvm(exc.getExceptionName());
                    } else {
                        // 专用服务器，发送网络包到客户端让其崩溃
                        PacketDistributor.sendToPlayer((ServerPlayer) victim, new CrashPayload());
                    }
                } else {
                    // 非致命异常：断开连接
                    if (victim instanceof ServerPlayer sp) {
                        sp.connection.disconnect(Component.literal(exc.getExceptionName()));
                    } else if (victim.level().isClientSide) {
                        Minecraft.getInstance().player.connection.getConnection().disconnect(Component.literal(exc.getExceptionName()));
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
