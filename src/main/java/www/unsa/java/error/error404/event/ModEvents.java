package www.unsa.java.error.error404.event;

import net.minecraft.nbt.CompoundTag;
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
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import www.unsa.java.error.error404.JavaError404;
import www.unsa.java.error.error404.item.ExceptionItem;
import www.unsa.java.error.error404.item.JavaItem;
import www.unsa.java.error.error404.item.ModItems;
import www.unsa.java.error.error404.network.ActivatePacketDropPacket;
import www.unsa.java.error.error404.network.ClientboundCrashPacket;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@EventBusSubscriber(modid = JavaError404.MODID)
public class ModEvents {
    private static final Random RANDOM = new Random();
    // 存储剪刀使用次数（服务端）
    private static final Map<UUID, Integer> SCISSOR_COUNT = new HashMap<>();
    // 存储等待给予物品的状态
    private static final String TAG_PENDING_PACKET = "java_error_404_pending_packet";

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
                handleScissorUse(user, targetPlayer);
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
            handleScissorUse(player, player);
        }

        if (stack.getItem() instanceof JavaItem && player.isCrouching()) {
            JavaItem.nextMode(stack);
            player.displayClientMessage(Component.literal("Switched to " + JavaItem.getMode(stack)), true);
        }
    }

    private static void handleScissorUse(Player user, Player target) {
        if (!(user instanceof ServerPlayer spUser)) return;

        UUID uuid = user.getUUID();
        int count = SCISSOR_COUNT.getOrDefault(uuid, 0) + 1;
        SCISSOR_COUNT.put(uuid, count);
        // 概率 = min(count * 5%, 80%)
        double probability = Math.min(count * 0.05, 0.8);
        if (RANDOM.nextDouble() < probability) {
            // 触发丢包
            SCISSOR_COUNT.remove(uuid); // 重置计数
            // 给目标玩家添加状态（持久化）
            ServerPlayer spTarget = (target instanceof ServerPlayer) ? (ServerPlayer) target : spUser;
            CompoundTag data = spTarget.getPersistentData();
            data.putBoolean(TAG_PENDING_PACKET, true);
            // 发送激活丢包数据包
            PacketDistributor.sendToPlayer(spTarget, new ActivatePacketDropPacket());
            // 注意：不直接给予物品，等玩家重连后再给
        }
    }

    // 玩家登录事件
    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer sp) {
            CompoundTag data = sp.getPersistentData();
            if (data.getBoolean(TAG_PENDING_PACKET)) {
                data.remove(TAG_PENDING_PACKET);
                // 给予网络包
                ItemStack packet = new ItemStack(ModItems.JAVA_NETWORK_PACKET.get());
                if (!sp.getInventory().add(packet)) {
                    sp.drop(packet, false);
                }
                sp.getInventory().setChanged();
                // 清除剪刀计数（可选）
                SCISSOR_COUNT.remove(sp.getUUID());
            }
        }
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

                if (exc.isCausesCrash()) {
                    // 发送崩溃数据包到客户端
                    if (victim instanceof ServerPlayer sp) {
                        PacketDistributor.sendToPlayer(sp, new ClientboundCrashPacket(exc.getCrashType()));
                    } else if (victim.level().isClientSide) {
                        // 单人游戏直接执行
                        exc.getCrashType().execute();
                    }
                } else {
                    // 非致命：断连
                    if (victim instanceof ServerPlayer sp) {
                        sp.connection.disconnect(Component.literal(exc.getCrashType().name()));
                    } else if (victim.level().isClientSide) {
                        victim.connection.getConnection().disconnect(Component.literal(exc.getCrashType().name()));
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
