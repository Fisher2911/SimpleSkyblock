package io.github.fisher2911.simpleskyblock.user;

import io.github.fisher2911.simpleskyblock.SimpleSkyblock;
import io.github.fisher2911.simpleskyblock.util.Position;
import org.bukkit.configuration.file.YamlConfiguration;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserManager {

    private final SimpleSkyblock plugin;
    private final Map<UUID, User> userMap = new HashMap<>();

    public UserManager(final SimpleSkyblock plugin) {
        this.plugin = plugin;
    }

    @Nullable
    public User getUser(final UUID uuid) {
        return this.userMap.get(uuid);
    }

    public void loadUser(final UUID uuid) {
        final File file = this.getUserFile(uuid);
        if (!file.exists()) {
            this.userMap.put(uuid, new User(uuid, null));
            return;
        }
        final YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        final Position position = config.getObject("island", Position.class);
        this.userMap.put(uuid, new User(uuid, position));
    }

    public void saveUser(final UUID uuid) {
        final User user = this.userMap.get(uuid);
        if (user == null) {
            this.plugin.getLogger().severe("User null: " + uuid);
            return;
        }
        final File file = this.getUserFile(uuid);

        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
                this.plugin.getLogger().info("Creating player file");
            } catch (final IOException exception) {
                exception.printStackTrace();
            }
        }

        final YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set("island", user.getIslandPosition());

        try {
            config.save(file);
            plugin.getLogger().info("Saving player");
        } catch (final IOException exception) {
            exception.printStackTrace();
        }
    }

    private File getUserFile(final UUID uuid) {
        final File file = Path.of(
                this.plugin.getDataFolder().getPath(),
                "players",
                uuid.toString() + ".yml"
        ).toFile();

        return file;
    }
}
