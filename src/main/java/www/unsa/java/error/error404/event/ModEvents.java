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
import www.unsa.java.error.error404.network.CrashType;
import www.unsa.java.error.error404.util.CrashHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@EventBusSubscriber(modid = JavaError404.MODID)
public class ModEvents {
    private static final Random RANDOM = new Random();
    private static final Map<UUID, Integer> SCISSOR_COUNT = new HashMap<>();
    private static final Map<UUID, Boolean> PENDING_PACKETS = new ConcurrentHashMap<>();

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
            CompoundTag data = target.getPersistentData();
            data.putString(ExceptionItem.TAG_CRASH_TYPE, exc.getCrashType().name());
            data.putBoolean(ExceptionItem.TAG_CAUSES_CRASH, exc.isCausesCrash());
            data.putBoolean(ExceptionItem.TAG_IS_SUICIDE, false);
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
        double probability = Math.min(count * 0.05, 0.8);
        if (RANDOM.nextDouble() < probability) {
            SCISSOR_COUNT.remove(uuid);
            ServerPlayer spTarget = (target instanceof ServerPlayer) ? (ServerPlayer) target : spUser;
            PENDING_PACKETS.put(spTarget.getUUID(), true);
            PacketDistributor.sendToPlayer(spTarget, new ActivatePacketDropPacket());
        }
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer sp) {
            UUID uuid = sp.getUUID();
            if (PENDING_PACKETS.remove(uuid) != null) {
                ItemStack packet = new ItemStack(ModItems.JAVA_NETWORK_PACKET.get());
                if (!sp.getInventory().add(packet)) {
                    sp.drop(packet, false);
                }
                sp.getInventory().setChanged();
                SCISSOR_COUNT.remove(uuid);
            }
        }
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof Player victim) {
            CompoundTag data = victim.getPersistentData();
            if (!data.contains(ExceptionItem.TAG_CRASH_TYPE)) return;

            String crashTypeName = data.getString(ExceptionItem.TAG_CRASH_TYPE);
            boolean causesCrash = data.getBoolean(ExceptionItem.TAG_CAUSES_CRASH);
            boolean isSuicide = data.getBoolean(ExceptionItem.TAG_IS_SUICIDE);

            // 清理标记
            data.remove(ExceptionItem.TAG_CRASH_TYPE);
            data.remove(ExceptionItem.TAG_CAUSES_CRASH);
            data.remove(ExceptionItem.TAG_IS_SUICIDE);

            CrashType crashType;
            try {
                crashType = CrashType.valueOf(crashTypeName);
            } catch (IllegalArgumentException e) {
                return;
            }

            Component deathMsg;
            if (isSuicide) {
                deathMsg = Component.literal(victim.getName().getString() + " " + generateGibberish(15, 30));
            } else {
                deathMsg = Component.literal(victim.getName().getString() + " was killed by " + generateGibberish(8, 20));
            }
            victim.level().players().forEach(p -> p.sendSystemMessage(deathMsg));
            event.setCanceled(true);

            if (causesCrash) {
                if (victim instanceof ServerPlayer sp) {
                    PacketDistributor.sendToPlayer(sp, new ClientboundCrashPacket(crashType));
                } else if (victim.level().isClientSide) {
                    CrashHelper.crashJvm(crashType.name());
                }
            } else {
                String gibberishReason = generateGibberish(20, 40);
                if (victim instanceof ServerPlayer sp) {
                    sp.connection.disconnect(Component.literal(crashType.name() + "\n\n" + gibberishReason));
                } else if (victim.level().isClientSide) {
                    net.minecraft.client.Minecraft.getInstance().player.connection.getConnection()
                        .disconnect(Component.literal(crashType.name() + "\n\n" + gibberishReason));
                }
            }
        }
    }

    private static String generateGibberish(int minLen, int maxLen) {
        // 多套字符集，确保每次都不重样
        String[] charsets = {
            // ASCII 乱码
            "!@#$%^&*()_+-=[]{}|;:',.<>?/`~",
            // 逆向 + 乱码符号
            "\u00A2\u00A3\u00A5\u00A9\u00AE\u2122\u00B1\u00D7\u00F7\u2020\u2021\u2022\u221A\u221E\u2248\u2260\u2264\u2265",
            // 杂项数学/技术符号
            "\u2200\u2202\u2203\u2205\u2207\u2208\u2209\u220B\u2211\u2212\u2217\u2218\u2219\u221A\u221D\u221E\u2220\u2227\u2228\u2229\u222A\u222B\u222C\u222D\u222E\u2234\u2235\u223C\u223D",
            // 方块/箭头/几何
            "\u25A0\u25A1\u25B2\u25B3\u25B6\u25B7\u25BC\u25BD\u25C0\u25C1\u25C6\u25C7\u25CB\u25CE\u25CF\u25D0\u25D1\u25E6\u25E7",
            // 菱形/三角
            "\u2660\u2661\u2662\u2663\u2664\u2665\u2666\u2667\u2669\u266A\u266B\u266C\u266D\u266E\u266F",
            // 东亚标点
            "\u3001\u3002\u300C\u300D\u300E\u300F\u3010\u3011\u3014\u3015\u301C\u303D\u30FB\uFF01\uFF04\uFF05\uFF06\uFF08\uFF09\uFF0A\uFF0B\uFF0C\uFF0D\uFF0E\uFF0F\uFF1A\uFF1B\uFF1C\uFF1D\uFF1E\uFF1F\uFF20",
            // 混合数字/字母/符号
            "0123456789abcdefABCDEF\u00D8\u00F8\u00DE\u00FE\u00DF\u00E6\u00C6",
        };

        StringBuilder sb = new StringBuilder();
        int targetLen = minLen + RANDOM.nextInt(maxLen - minLen + 1);

        // 随机挑选 2~4 套字符集混合使用
        int setCount = 2 + RANDOM.nextInt(3);
        StringBuilder pool = new StringBuilder();
        for (int i = 0; i < setCount; i++) {
            pool.append(charsets[RANDOM.nextInt(charsets.length)]);
        }
        // 也加入一些随机 ASCII 可打印字符增加混乱度
        for (int i = 0; i < 20; i++) {
            char c = (char) (0x21 + RANDOM.nextInt(0x7E - 0x21 + 1));
            pool.append(c);
        }
        String combined = pool.toString();

        for (int i = 0; i < targetLen; i++) {
            sb.append(combined.charAt(RANDOM.nextInt(combined.length())));
        }
        return sb.toString();
    }
}
