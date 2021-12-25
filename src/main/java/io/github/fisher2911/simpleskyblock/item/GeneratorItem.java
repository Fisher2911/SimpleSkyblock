package io.github.fisher2911.simpleskyblock.item;

import org.bukkit.Material;

public record GeneratorItem(Material material, float chance) implements Comparable<GeneratorItem> {

    @Override
    public int compareTo(final GeneratorItem o) {
        return Float.compare(this.chance, o.chance);
    }
}
