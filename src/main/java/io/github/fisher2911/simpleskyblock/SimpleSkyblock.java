package io.github.fisher2911.simpleskyblock;

import io.github.fisher2911.simpleskyblock.command.BaseCommand;
import io.github.fisher2911.simpleskyblock.listener.ClickListener;
import io.github.fisher2911.simpleskyblock.listener.CobbleGenListener;
import io.github.fisher2911.simpleskyblock.listener.JoinListener;
import io.github.fisher2911.simpleskyblock.listener.PlayerRespawnListener;
import io.github.fisher2911.simpleskyblock.settings.GeneratorSettings;
import io.github.fisher2911.simpleskyblock.settings.IslandLocations;
import io.github.fisher2911.simpleskyblock.user.UserManager;
import io.github.fisher2911.simpleskyblock.util.Position;
import io.github.fisher2911.simpleskyblock.world.generator.CustomGenerator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class SimpleSkyblock extends JavaPlugin {

    static {
        ConfigurationSerialization.registerClass(Position.class, "Position");
    }

    private GeneratorSettings settings;
    private IslandLocations islandLocations;
    private UserManager userManager;

    @Override
    public void onEnable() {
        this.addRecipes();
        this.settings = new GeneratorSettings(this);
        this.settings.load();
        this.userManager = new UserManager(this);
        this.islandLocations = new IslandLocations(this,
                "skyblock");
        this.islandLocations.load();

        List.of(
                new CobbleGenListener(this.settings),
                new JoinListener(this),
                new PlayerRespawnListener(this),
                new ClickListener(this)
        ).forEach(listener ->
                this.getServer().getPluginManager().registerEvents(listener, this));

        getCommand("simpleskyblock").setExecutor(new BaseCommand(this));

        Bukkit.getScheduler().runTask(this,
                () -> {
                    final WorldCreator worldCreator = new WorldCreator("skyblock");
                    worldCreator.generator(new CustomGenerator());
                    worldCreator.createWorld();
                });
    }

    @Override
    public void onDisable() {
        this.islandLocations.save();
        for (final Player player : Bukkit.getOnlinePlayers()) {
            this.userManager.saveUser(player.getUniqueId());
            this.getLogger().info("Saving user: " + player.getUniqueId());
        }
    }

    public GeneratorSettings getSettings() {
        return settings;
    }

    public IslandLocations getIslandLocations() {
        return islandLocations;
    }

    public UserManager getUserManager() {
        return userManager;
    }

    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
        return new CustomGenerator();
    }

    private void addRecipes() {
        Bukkit.addRecipe(new ShapelessRecipe(
                new NamespacedKey(this,
                        "dirt"),
                new ItemStack(Material.DIRT)
        ).addIngredient(9, Material.COBBLESTONE));

        Bukkit.addRecipe(new ShapelessRecipe(
                new NamespacedKey(this,
                        "sand"),
                new ItemStack(Material.SAND)
        ).addIngredient(9, Material.DIRT));
    }
}
