package org.gielinor.game.world.update.flag.player;

import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.world.update.flag.UpdateFlag;
import org.gielinor.game.world.update.flag.context.HitMark;
import org.gielinor.net.packet.PacketBuilder;

/**
 * The main hit update flag.
 *
 * @author Emperor
 */
public final class HitUpdateFlag extends UpdateFlag<HitMark> {

    /**
     * Constructs a new {@code HitUpdateFlag} {@code Object}.
     *
     * @param context The hit mark.
     */
    public HitUpdateFlag(HitMark context) {
        super(context);
    }

    @Override
    public void write(PacketBuilder packetBuilder) {
        Entity e = context.getEntity();
        int icon = -1;
        if (context.getCombatStyle() != null) {
            switch (context.getCombatStyle()) {
                case MELEE:
                    icon = 0;
                    break;
                case RANGE:
                    icon = 1;
                    break;
                case MAGIC:
                    icon = 2;
                    break;
            }
        }
        packetBuilder.put(context.getDamage());
        packetBuilder.putA(context.getType());
        packetBuilder.put(icon);
        packetBuilder.putC(e.getSkills().getLifepoints());
        packetBuilder.put(e.getSkills().getMaximumLifepoints());
        // 0 = melee
        // 1 = ranged
        // 2 = mage
        // 3 = deflect
        // 4 = cannon
        // 5 = defence
    }

    @Override
    public int data() {
        return maskData();
    }

    @Override
    public int ordinal() {
        return 8;
    }

    /**
     * Gets the mask data.
     *
     * @return The mask data.
     */
    public static int maskData() {
        return 0x20;
    }

}