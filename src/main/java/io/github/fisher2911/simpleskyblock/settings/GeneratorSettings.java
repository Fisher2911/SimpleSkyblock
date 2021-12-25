package io.github.fisher2911.simpleskyblock.settings;

import io.github.fisher2911.simpleskyblock.SimpleSkyblock;
import io.github.fisher2911.simpleskyblock.item.GeneratorItem;
import io.github.fisher2911.simpleskyblock.util.Random;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class GeneratorSettings  {

    private final SimpleSkyblock plugin;
    private final List<GeneratorItem> generatorItems = new ArrayList<>();

    public GeneratorSettings(final SimpleSkyblock plugin) {
        this.plugin = plugin;
    }

    private static final String GENERATOR_PATH = "generator";
    private float maxChance;

    public void load() {
        this.generatorItems.clear();
        this.plugin.saveDefaultConfig();
        this.plugin.reloadConfig();
        final FileConfiguration config = this.plugin.getConfig();
        final var generatorSection = config.getConfigurationSection(GENERATOR_PATH);

        if (generatorSection == null) return;

        for (final String key : generatorSection.getKeys(false)) {
            final float chance =  (float) generatorSection.getDouble(key);
            this.generatorItems.add(
                    new GeneratorItem(
                            Material.valueOf(key.toUpperCase(Locale.ROOT)),
                            chance
                    )
            );
            this.maxChance = Math.max(this.maxChance, chance);
        }

        Collections.sort(this.generatorItems);
    }

    @Nullable
    public GeneratorItem getRandomItem() {
        final float chance = (float) Random.RANDOM.nextDouble(0, this.maxChance);
        for (GeneratorItem item : this.generatorItems) {
            if (chance < item.chance()) return item;
        }
        return null;
    }
}
