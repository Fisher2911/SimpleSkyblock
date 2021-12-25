package io.github.fisher2911.simpleskyblock.command;

import io.github.fisher2911.simpleskyblock.SimpleSkyblock;
import io.github.fisher2911.simpleskyblock.settings.GeneratorSettings;
import io.github.fisher2911.simpleskyblock.settings.IslandLocations;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class BaseCommand implements CommandExecutor {

    private final GeneratorSettings settings;
    private final IslandLocations islandLocations;

    public BaseCommand(final SimpleSkyblock plugin) {
        this.settings = plugin.getSettings();
        this.islandLocations = plugin.getIslandLocations();
    }

    @Override
    public boolean onCommand(
            final CommandSender sender,
            final Command command,
            final String label,
            final String[] args) {

        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            this.settings.load();
            sender.sendMessage(ChatColor.GREEN + "Cobble generator successfully reloaded");
            return true;
        }
        sender.sendMessage(ChatColor.RED + "Invalid command!");
        return true;
    }
}
