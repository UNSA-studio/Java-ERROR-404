package www.unsa.java.error.error404.item;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.ambient.AmbientCreature;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.ChatFormatting;

import java.util.*;

public class EntityScanner {
    private static final int SCAN_RADIUS = 160;

    private static final Map<UUID, String> PREV_TEAMS = new HashMap<>();

    public static void scan(ServerPlayer player) {
        AABB area = new AABB(player.blockPosition()).inflate(SCAN_RADIUS);
        List<Entity> entities = player.level().getEntities(player, area,
            e -> !e.isSpectator() && e != player);

        Scoreboard scoreboard = player.level().getScoreboard();
        String teamName = "j404_" + player.getName().getString();

        for (Entity entity : entities) {
            ChatFormatting color = getColor(entity);
            PlayerTeam team = scoreboard.getPlayerTeam(teamName);
            if (team == null) {
                team = scoreboard.addPlayerTeam(teamName);
                team.setColor(color);
            } else {
                team.setColor(color);
            }
            // 记录原队伍以便恢复
            PlayerTeam oldTeam = scoreboard.getPlayersTeam(entity.getScoreboardName());
            if (oldTeam != null && !oldTeam.getName().equals(teamName)) {
                PREV_TEAMS.put(entity.getUUID(), oldTeam.getName());
            }
            scoreboard.addPlayerToTeam(entity.getScoreboardName(), team);
            entity.setGlowingTag(true);
        }
    }

    public static void cleanup(ServerPlayer player) {
        String teamName = "j404_" + player.getName().getString();
        Scoreboard scoreboard = player.level().getScoreboard();
        PlayerTeam team = scoreboard.getPlayerTeam(teamName);
        if (team == null) return;

        // 遍历前拷贝，避免 ConcurrentModificationException
        List<String> members = new ArrayList<>(team.getPlayers());
        for (String member : members) {
            scoreboard.removePlayerFromTeam(member, team);
            // 恢复原队伍
            for (Entity e : player.level().getEntities(player,
                    new AABB(player.blockPosition()).inflate(SCAN_RADIUS),
                    ent -> ent.getScoreboardName().equals(member))) {
                UUID uuid = e.getUUID();
                String oldTeamName = PREV_TEAMS.remove(uuid);
                if (oldTeamName != null) {
                    PlayerTeam oldTeam = scoreboard.getPlayerTeam(oldTeamName);
                    if (oldTeam != null) scoreboard.addPlayerToTeam(member, oldTeam);
                }
                break;
            }
        }
        scoreboard.removePlayerTeam(team);
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
        // 中立生物用紫色
        if (entity instanceof Mob mob && mob.getTarget() == null) {
            return ChatFormatting.LIGHT_PURPLE;
        }
        return ChatFormatting.LIGHT_PURPLE;
    }
}