package io.github.fisher2911.simpleskyblock.listener;

import io.github.fisher2911.simpleskyblock.SimpleSkyblock;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class ClickListener implements Listener {

    private final SimpleSkyblock plugin;

    public ClickListener(final SimpleSkyblock plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClick(final PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        final Block block = event.getClickedBlock();

        if (block == null || block.getType() != Material.OBSIDIAN) return;

        final Player player = event.getPlayer();

        final ItemStack inHand = player.getInventory().getItemInMainHand();

        if (inHand.getType() != Material.BUCKET) return;

        block.setType(Material.AIR);

        if (inHand.getAmount() == 1) {
            inHand.setType(Material.LAVA_BUCKET);
            return;
        }

        inHand.setAmount(inHand.getAmount() - 1);

        player.getInventory().setItemInMainHand(
                inHand
        );

        final Map<Integer, ItemStack> items = player.getInventory().addItem(
                new ItemStack(Material.LAVA)
        );

        items.forEach((slot, item) ->
                player.getWorld().dropItem(player.getLocation(),
                        new ItemStack(Material.LAVA_BUCKET))
        );
    }
}
