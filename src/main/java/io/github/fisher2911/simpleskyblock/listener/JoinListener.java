package io.github.fisher2911.simpleskyblock.listener;

import com.sk89q.worldedit.WorldEditException;
import io.github.fisher2911.simpleskyblock.SimpleSkyblock;
import io.github.fisher2911.simpleskyblock.settings.IslandLocations;
import io.github.fisher2911.simpleskyblock.user.User;
import io.github.fisher2911.simpleskyblock.user.UserManager;
import io.github.fisher2911.simpleskyblock.util.Position;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.IOException;
import java.util.UUID;

public class JoinListener implements Listener {

    private final SimpleSkyblock plugin;
    private final UserManager userManager;
    private final IslandLocations islandLocations;

    public JoinListener(final SimpleSkyblock plugin) {
        this.plugin = plugin;
        this.userManager = this.plugin.getUserManager();
        this.islandLocations = this.plugin.getIslandLocations();
    }

    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final UUID uuid = player.getUniqueId();

        this.userManager.loadUser(uuid);
        final User user = this.userManager.getUser(uuid);

        if (user == null || user.getIslandPosition() != null) return;

        try {
            final Position position = this.islandLocations.pasteSchematic(user);
            final Location location = position.toLocation(
                    islandLocations.getWorld()
            );
            Bukkit.getScheduler().runTaskLater(this.plugin,
                    () -> {
                        player.teleport(location);
                        player.setBedSpawnLocation(location, true);
                    },
                    5);
        } catch (final IOException | WorldEditException exception) {
            exception.printStackTrace();
        }

        player.discoverRecipe(
                new NamespacedKey(this.plugin, "dirt")
        );
        player.discoverRecipe(
                new NamespacedKey(this.plugin, "sand")
        );
    }

    @EventHandler
    public void onQuit(final PlayerQuitEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(
                this.plugin,
                () -> this.userManager.saveUser(event.getPlayer().getUniqueId())
        );
    }
}
