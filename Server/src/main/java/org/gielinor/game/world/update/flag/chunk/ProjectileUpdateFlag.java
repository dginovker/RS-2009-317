package org.gielinor.game.world.update.flag.chunk;

import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.impl.Projectile;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.update.flag.UpdateFlag;
import org.gielinor.net.packet.PacketBuilder;

/**
 * Handles the projectile updating.
 *
 * @author Emperor
 */
public final class ProjectileUpdateFlag extends UpdateFlag<Projectile> {

    /**
     * Constructs a new {@code ProjectileUpdateFlag} {@code Object}.
     *
     * @param projectile The projectile.
     */
    public ProjectileUpdateFlag(Projectile projectile) {
        super(projectile);
    }

    @Override
    public void write(PacketBuilder packetBuilder) {
        Projectile projectile = context;
        Location start = projectile.getSourceLocation();
        Entity target = projectile.getVictim();
        Location end = projectile.isLocationBased() ? projectile.getEndLocation() : target.getLocation();
        packetBuilder.put((byte) 117);
        packetBuilder.put((byte) (start.getChunkOffsetX() << 4) | (start.getChunkOffsetY() & 0x7));
        packetBuilder.put((byte) -(start.getX() - end.getX()));
        packetBuilder.put((byte) -(start.getY() - end.getY()));
        packetBuilder.putShort(target != null ? (target.getProjectileLockonIndex()) : -1);
        packetBuilder.putShort(projectile.getProjectileId());
        packetBuilder.put((byte) projectile.getStartHeight());
        packetBuilder.put((byte) projectile.getEndHeight());
        packetBuilder.putShort(projectile.getStartDelay());
        packetBuilder.putShort(projectile.getSpeed());
        packetBuilder.put((byte) projectile.getDistance());
        packetBuilder.put((byte) projectile.getAngle());
    }

    @Override
    public int data() {
        return 0;
    }

    @Override
    public int ordinal() {
        return 11;
    }

}