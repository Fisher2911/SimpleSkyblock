package io.github.fisher2911.simpleskyblock.util;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.Map;

@SerializableAs("Position")
public record Position(int x, int y, int z) implements ConfigurationSerializable {

    public static Position of(final int x, final int y, final int z) {
        return new Position(x, y, z);
    }

    public static Position fromLocation(final Location location) {
        return new Position(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    public Location toLocation(final World world) {
        return new Location(world, this.x, this.y, this.z);
    }

    @Override
    public Map<String, Object> serialize() {
        return Map.of("x", this.x,
                "y", this.y,
                "z", this.z);
    }

    public static Position deserialize(final Map<String, Object> args) {
        return Position.of(
                (int) args.get("x"),
                (int) args.get("y"),
                (int) args.get("z")
        );
    }
}
