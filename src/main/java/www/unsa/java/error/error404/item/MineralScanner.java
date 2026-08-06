package www.unsa.java.error.error404.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.joml.Vector3f;

import java.util.*;

public class MineralScanner {
    private static final int SCAN_RADIUS = 160;
    private static final int SAMPLES_PER_SCAN = 300;

    private static final Map<Block, Vector3f> ORE_COLORS = new LinkedHashMap<>();

    static {
        ORE_COLORS.put(Blocks.COAL_ORE,         new Vector3f(0.2f, 0.2f, 0.2f));
        ORE_COLORS.put(Blocks.DEEPSLATE_COAL_ORE, new Vector3f(0.2f, 0.2f, 0.2f));
        ORE_COLORS.put(Blocks.IRON_ORE,         new Vector3f(0.7f, 0.55f, 0.4f));
        ORE_COLORS.put(Blocks.DEEPSLATE_IRON_ORE, new Vector3f(0.7f, 0.55f, 0.4f));
        ORE_COLORS.put(Blocks.COPPER_ORE,       new Vector3f(0.85f, 0.45f, 0.2f));
        ORE_COLORS.put(Blocks.DEEPSLATE_COPPER_ORE, new Vector3f(0.85f, 0.45f, 0.2f));
        ORE_COLORS.put(Blocks.GOLD_ORE,         new Vector3f(1.0f, 0.85f, 0.1f));
        ORE_COLORS.put(Blocks.DEEPSLATE_GOLD_ORE, new Vector3f(1.0f, 0.85f, 0.1f));
        ORE_COLORS.put(Blocks.REDSTONE_ORE,     new Vector3f(0.9f, 0.1f, 0.1f));
        ORE_COLORS.put(Blocks.DEEPSLATE_REDSTONE_ORE, new Vector3f(0.9f, 0.1f, 0.1f));
        ORE_COLORS.put(Blocks.LAPIS_ORE,        new Vector3f(0.15f, 0.25f, 0.85f));
        ORE_COLORS.put(Blocks.DEEPSLATE_LAPIS_ORE, new Vector3f(0.15f, 0.25f, 0.85f));
        ORE_COLORS.put(Blocks.DIAMOND_ORE,      new Vector3f(0.3f, 0.85f, 0.9f));
        ORE_COLORS.put(Blocks.DEEPSLATE_DIAMOND_ORE, new Vector3f(0.3f, 0.85f, 0.9f));
        ORE_COLORS.put(Blocks.EMERALD_ORE,      new Vector3f(0.1f, 0.8f, 0.3f));
        ORE_COLORS.put(Blocks.DEEPSLATE_EMERALD_ORE, new Vector3f(0.1f, 0.8f, 0.3f));
        ORE_COLORS.put(Blocks.NETHER_QUARTZ_ORE, new Vector3f(0.9f, 0.85f, 0.8f));
        ORE_COLORS.put(Blocks.NETHER_GOLD_ORE,  new Vector3f(1.0f, 0.85f, 0.1f));
        ORE_COLORS.put(Blocks.ANCIENT_DEBRIS,   new Vector3f(0.4f, 0.2f, 0.3f));
    }

    private static final Vector3f FALLBACK_COLOR = new Vector3f(0.5f, 0.5f, 0.5f);

    private static final Random RANDOM = new Random();

    public static void scan(ServerPlayer player) {
        ServerLevel level = player.serverLevel();
        BlockPos center = player.blockPosition();

        for (int i = 0; i < SAMPLES_PER_SCAN; i++) {
            int dx = RANDOM.nextInt(SCAN_RADIUS * 2 + 1) - SCAN_RADIUS;
            int dz = RANDOM.nextInt(SCAN_RADIUS * 2 + 1) - SCAN_RADIUS;
            int dy = RANDOM.nextInt(Math.min(128, level.getMaxBuildHeight())) - 64;
            BlockPos pos = center.offset(dx, dy, dz);
            if (pos.getY() < level.getMinBuildHeight() || pos.getY() >= level.getMaxBuildHeight()) continue;

            BlockState state = level.getBlockState(pos);
            Block block = state.getBlock();
            Vector3f color = ORE_COLORS.getOrDefault(block, null);

            if (color == null && isOreName(block)) {
                color = FALLBACK_COLOR;
            }
            if (color == null) continue;

            // 在方块边缘生成粒子框
            spawnParticleBox(level, pos, color);
        }
    }

    private static boolean isOreName(Block block) {
        String name = block.getDescriptionId();
        return name.contains("_ore") || name.contains("debris");
    }

    private static void spawnParticleBox(ServerLevel level, BlockPos pos, Vector3f color) {
        DustParticleOptions particle = new DustParticleOptions(color, 1.2f);
        double x = pos.getX() + 0.5, y = pos.getY() + 0.5, z = pos.getZ() + 0.5;
        int edges = 3;
        for (int ex = 0; ex <= edges; ex++) {
            for (int ey = 0; ey <= edges; ey++) {
                for (int ez = 0; ez <= edges; ez++) {
                    if ((ex == 0 || ex == edges) && (ey == 0 || ey == edges) && (ez == 0 || ez == edges)) {
                        double px = pos.getX() + (ex * 1.0 / edges);
                        double py = pos.getY() + (ey * 1.0 / edges);
                        double pz = pos.getZ() + (ez * 1.0 / edges);
                        level.sendParticles(particle, px, py, pz, 1, 0, 0, 0, 0.01);
                    }
                }
            }
        }
    }
}