package io.github.fisher2911.simpleskyblock.listener;

import io.github.fisher2911.simpleskyblock.SimpleSkyblock;
import io.github.fisher2911.simpleskyblock.settings.IslandLocations;
import io.github.fisher2911.simpleskyblock.user.User;
import io.github.fisher2911.simpleskyblock.user.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerRespawnListener implements Listener {

    private final SimpleSkyblock plugin;
    private final IslandLocations islandLocations;
    private final UserManager userManager;

    public PlayerRespawnListener(final SimpleSkyblock plugin) {
        this.plugin = plugin;
        this.islandLocations = this.plugin.getIslandLocations();
        this.userManager = this.plugin.getUserManager();
    }

    @EventHandler
    public void onRespawn(final PlayerRespawnEvent event) {
        final Player player = event.getPlayer();
        final User user = this.userManager.getUser(player.getUniqueId());
        if (user == null || player.getBedSpawnLocation() != null) return;
        final Location location =
                user.getIslandPosition().toLocation(this.islandLocations.getWorld());
        player.setBedSpawnLocation(
                location,
                true);

        player.sendMessage(location.toString());

        Bukkit.getScheduler().runTaskLater(
                this.plugin,
                () -> player.teleport(location),
                1
        );
    }
}
