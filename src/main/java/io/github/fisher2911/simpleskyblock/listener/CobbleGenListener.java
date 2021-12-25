package io.github.fisher2911.simpleskyblock.listener;

import io.github.fisher2911.simpleskyblock.item.GeneratorItem;
import io.github.fisher2911.simpleskyblock.settings.GeneratorSettings;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFormEvent;

public class CobbleGenListener implements Listener {

    private final GeneratorSettings settings;

    public CobbleGenListener(final GeneratorSettings settings) {
        this.settings = settings;
    }

    @EventHandler
    public void onCobbleForm(final BlockFormEvent event) {
        final Material material = event.getNewState().getType();

        if (material != Material.COBBLESTONE && material != Material.STONE) return;

        final GeneratorItem item = settings.getRandomItem();

        if (item == null) return;
        final BlockState state = event.getNewState();
        state.setType(item.material());
    }
}
