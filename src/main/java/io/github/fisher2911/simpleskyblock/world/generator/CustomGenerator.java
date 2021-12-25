package io.github.fisher2911.simpleskyblock.world.generator;

import org.bukkit.Material;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;

import java.util.Random;

public class CustomGenerator extends ChunkGenerator {

    public CustomGenerator() {
    }

    @Override
    public void generateSurface(final WorldInfo worldInfo, final Random random, final int x, final int z, final ChunkData chunkData) {
        for (int chunkX = 0; chunkX < 15; chunkX++) {
            for (int chunkY = worldInfo.getMinHeight(); chunkY < worldInfo.getMaxHeight(); chunkY++) {
                for (int chunkZ = 0; chunkZ < 15; chunkZ ++) {
                    chunkData.setBlock(chunkX, chunkY, chunkZ, Material.AIR);
                }
            }
        }
    }

    @Override
    public void generateNoise(final WorldInfo worldInfo, final Random random, final int x, final int z, final ChunkData chunkData) {

    }

    @Override
    public boolean shouldGenerateNoise() {
        return false;
    }

    @Override
    public boolean shouldGenerateSurface() {
        return true;
    }

    @Override
    public boolean shouldGenerateBedrock() {
        return false;
    }

    @Override
    public boolean shouldGenerateCaves() {
        return false;
    }

    @Override
    public boolean shouldGenerateDecorations() {
        return false;
    }

    @Override
    public boolean shouldGenerateStructures() {
        return false;
    }
}
