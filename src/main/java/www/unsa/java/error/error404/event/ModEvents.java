package www.unsa.java.error.error404.event;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
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

import java.util.Random;

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
        if (weapon.getItem() instanceof ExceptionItem exc) {
            target.hurt(attacker.damageSources().genericKill(), Float.MAX_VALUE);
            event.setCanceled(true);
            if (target instanceof ServerPlayer victim) {
                if (exc.isCausesCrash()) {
                    PacketDistributor.sendToPlayer(victim, new CrashPayload());
                } else {
                    PacketDistributor.sendToPlayer(victim, new DisconnectPayload(exc.getExceptionName()));
                }
            }
        }
    }

    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        Player user = event.getEntity();
        ItemStack stack = user.getItemInHand(event.getHand());
        if (stack.is(ModItems.SCISSORS.get())) {
            if (event.getTarget() instanceof ServerPlayer targetPlayer) {
                ItemStack packet = new ItemStack(ModItems.JAVA_NETWORK_PACKET.get());
                if (!targetPlayer.addItem(packet)) {
                    targetPlayer.drop(packet, false);
                }
                PacketDistributor.sendToPlayer(targetPlayer, new DisconnectPayload("Connection lost: Packet not received"));
                event.setCanceled(true);
            } else if (event.getTarget() instanceof Player) {
                // 客户端玩家自己（单人模式）或其他情况，略过
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
                ItemStack packet = new ItemStack(ModItems.JAVA_NETWORK_PACKET.get());
                if (!player.addItem(packet)) {
                    player.drop(packet, false);
                }
                // 断开连接：服务端踢出或客户端自断
                if (player instanceof ServerPlayer sp) {
                    sp.connection.disconnect(Component.literal("Packet loss: Server stopped sending packets"));
                } else if (player.level().isClientSide) {
                    player.connection.getConnection().disconnect(Component.literal("Packet loss"));
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof Player victim) {
            DamageSource source = event.getSource();
            if (source.getEntity() instanceof Player attacker) {
                ItemStack weapon = attacker.getMainHandItem();
                if (weapon.getItem() instanceof ExceptionItem) {
                    Component deathMsg;
                    if (RANDOM.nextFloat() < 0.6) {
                        deathMsg = Component.literal(victim.getName().getString() + " Killed by Java.Error.404");
                    } else {
                        deathMsg = Component.literal(victim.getName().getString() + " was killed by " + generateGibberish());
                    }
                    event.setCanceled(true);
                    victim.level().players().forEach(p -> p.sendSystemMessage(deathMsg));
                }
            } else if (source == victim.damageSources().genericKill() && victim.getMainHandItem().getItem() instanceof ExceptionItem) {
                Component msg = Component.literal(victim.getName().getString() + " ※ui꧂idᝰ >hy $%e y№u ￡o€n¥ t[i♯?");
                event.setCanceled(true);
                victim.level().players().forEach(p -> p.sendSystemMessage(msg));
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
