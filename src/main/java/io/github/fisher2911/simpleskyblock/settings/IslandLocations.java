package io.github.fisher2911.simpleskyblock.settings;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import io.github.fisher2911.simpleskyblock.SimpleSkyblock;
import io.github.fisher2911.simpleskyblock.user.User;
import io.github.fisher2911.simpleskyblock.util.Position;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;

public class IslandLocations {

    private final SimpleSkyblock plugin;
    private final String worldName;
    private Position previousPosition;
    private int distance;

    private static final String LAST_POSITION_PATH = "last-position";
    private static final String ISLAND_DISTANCE_PATH = "island-distance";

    public IslandLocations(
            final SimpleSkyblock plugin,
            final String worldName) {
        this.plugin = plugin;
        this.worldName = worldName;
    }

    public Position pasteSchematic(final User user) throws IOException, WorldEditException {
        final World world = Bukkit.getWorld(this.worldName);

        if (this.previousPosition == null) {
            this.previousPosition = Position.of(
                    0,
                    world.getMaxHeight() / 2,
                    0
            );
        }

        Clipboard clipboard;

        final File file =
                Path.of(
                        this.plugin.getDataFolder().getPath(),
                        "island.schem"
                ).toFile();

        if (!file.exists()) {
            this.plugin.saveResource("island.schem", false);
        }

        ClipboardFormat format = ClipboardFormats.findByFile(file);
        try (ClipboardReader reader = format.getReader(new FileInputStream(file))) {
            clipboard = reader.read();
        }

        try (final EditSession editSession =
                     WorldEdit.getInstance().newEditSession(
                             BukkitAdapter.adapt(world))) {
            Operation operation = new ClipboardHolder(clipboard)
                    .createPaste(editSession)
                    .to(BlockVector3.at(
                            this.previousPosition.x(),
                            this.previousPosition.y(),
                            this.previousPosition.z()))
                    .ignoreAirBlocks(false)
                    .build();
            Operations.complete(operation);
        }

        final Position copy = this.previousPosition;

        user.setIslandPosition(this.previousPosition);
        this.updateIslandPosition();
        return copy;
    }

    public void save() {
        final File file = this.getFile();

        final YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        config.set(LAST_POSITION_PATH, this.previousPosition);

        try {
            config.save(file);
        } catch (final IOException exception) {
            exception.printStackTrace();
        }
    }

    public void load() {
        final File file = this.getFile();
        final YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        this.distance = config.getInt(ISLAND_DISTANCE_PATH);
        final Position position = config.getObject(LAST_POSITION_PATH, Position.class);
        if (position == null) return;
        this.previousPosition = position;
    }

    private void updateIslandPosition() {
        int x = this.previousPosition.x();
        int z = this.previousPosition.z();
        if (x < z) {
            if (-1 * x < z) {
                x += this.distance;
            } else {
                z += this.distance;
            }
        } else if (x > z) {
            if (-1 * x >= z) {
                x -= this.distance;
            } else {
                z -= this.distance;
            }
        } else { // x == z
            if (x <= 0) {
                z += this.distance;
            } else {
                z -= this.distance;
            }
        }
        this.previousPosition = Position.of(
                x,
                this.previousPosition.y(),
                z
        );
        this.save();
    }

    private File getFile() {
        final File file = Path.of(
                this.plugin.getDataFolder().getPath(),
                "islandlocation.yml"
        ).toFile();

        if (!file.exists()) {
            this.plugin.saveResource(
                    "islandlocation.yml",
                    false
            );
        }

        return file;
    }

    public World getWorld() {
        return Bukkit.getWorld(this.worldName);
    }
}
