package www.unsa.java.error.error404.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.ambient.AmbientCreature;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.*;

/**
 * 生物扫描器 — 通过 Scoreboard 队伍实现高亮。
 * 每种颜色一个独立队伍（j404_BLUE / j404_RED 等），避免互相覆盖。
 * 原队伍记录按 (玩家UUID, 实体UUID) 双键存储，多人互不污染。
 */
public class EntityScanner {
    private static final int SCAN_RADIUS = 160;

    /** key: 玩家UUID + "|" + 实体UUID, value: 原队伍名（null 表示原本无队伍） */
    private static final Map<String, String> PREV_TEAMS = new HashMap<>();

    public static void scan(ServerPlayer player) {
        AABB area = new AABB(player.blockPosition()).inflate(SCAN_RADIUS);
        List<Entity> entities = player.level().getEntities(player, area,
            e -> !e.isSpectator() && e != player);

        Scoreboard scoreboard = player.level().getScoreboard();

        for (Entity entity : entities) {
            ChatFormatting color = getColor(entity);
            String teamName = "j404_" + color.name();
            PlayerTeam team = scoreboard.getPlayerTeam(teamName);
            if (team == null) {
                team = scoreboard.addPlayerTeam(teamName);
                team.setColor(color);
            }
            String key = player.getUUID() + "|" + entity.getUUID();
            if (!PREV_TEAMS.containsKey(key)) {
                PlayerTeam oldTeam = scoreboard.getPlayersTeam(entity.getScoreboardName());
                PREV_TEAMS.put(key, oldTeam != null ? oldTeam.getName() : null);
            }
            scoreboard.addPlayerToTeam(entity.getScoreboardName(), team);
            entity.setGlowingTag(true);
        }
    }

    public static void cleanup(ServerPlayer player) {
        Scoreboard scoreboard = player.level().getScoreboard();
        String prefix = player.getUUID() + "|";

        List<String> keys = new ArrayList<>();
        for (String key : PREV_TEAMS.keySet()) {
            if (key.startsWith(prefix)) keys.add(key);
        }

        for (String key : keys) {
            String entityUuidStr = key.substring(prefix.length());
            Entity entity = findEntityByUuid(player, entityUuidStr);
            if (entity == null) {
                // 实体已卸载，直接清记录
                PREV_TEAMS.remove(key);
                continue;
            }

            entity.setGlowingTag(false);

            PlayerTeam currentTeam = scoreboard.getPlayersTeam(entity.getScoreboardName());
            if (currentTeam != null && currentTeam.getName().startsWith("j404_")) {
                scoreboard.removePlayerFromTeam(entity.getScoreboardName(), currentTeam);
            }

            String prevTeamName = PREV_TEAMS.remove(key);
            if (prevTeamName != null) {
                PlayerTeam prevTeam = scoreboard.getPlayerTeam(prevTeamName);
                if (prevTeam != null) {
                    scoreboard.addPlayerToTeam(entity.getScoreboardName(), prevTeam);
                }
            }
        }

        // 清理空队伍
        for (ChatFormatting color : ChatFormatting.values()) {
            String tn = "j404_" + color.name();
            PlayerTeam t = scoreboard.getPlayerTeam(tn);
            if (t != null && t.getPlayers().isEmpty()) {
                scoreboard.removePlayerTeam(t);
            }
        }
    }

    private static Entity findEntityByUuid(ServerPlayer player, String uuidStr) {
        try {
            UUID uuid = UUID.fromString(uuidStr);
            return player.level().getEntity(uuid);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static void showTargetInfo(ServerPlayer player) {
        HitResult hit = player.pick(20, 0, false);
        if (hit instanceof EntityHitResult entityHit) {
            Entity target = entityHit.getEntity();
            String name = target instanceof Player
                ? ((Player) target).getName().getString()
                : target.getType().getDescription().getString();
            player.displayClientMessage(Component.literal("Target: " + name), true);
        }
    }

    private static ChatFormatting getColor(Entity entity) {
        if (entity instanceof Player) return ChatFormatting.BLUE;
        if (entity instanceof Enemy)  return ChatFormatting.RED;
        if (entity instanceof AbstractVillager) return ChatFormatting.GREEN;
        if (entity instanceof Animal) return ChatFormatting.GREEN;
        if (entity instanceof WaterAnimal) return ChatFormatting.GREEN;
        if (entity instanceof AmbientCreature) return ChatFormatting.GREEN;
        return ChatFormatting.LIGHT_PURPLE;
    }
}