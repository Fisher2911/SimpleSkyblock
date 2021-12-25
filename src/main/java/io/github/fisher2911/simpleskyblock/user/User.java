package io.github.fisher2911.simpleskyblock.user;

import io.github.fisher2911.simpleskyblock.util.Position;

import java.util.UUID;

public class User {

    private final UUID uuid;
    private Position islandPosition;

    public User(final UUID uuid, final Position islandPosition) {
        this.uuid = uuid;
        this.islandPosition = islandPosition;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setIslandPosition(final Position islandPosition) {
        this.islandPosition = islandPosition;
    }

    public Position getIslandPosition() {
        return islandPosition;
    }
}
