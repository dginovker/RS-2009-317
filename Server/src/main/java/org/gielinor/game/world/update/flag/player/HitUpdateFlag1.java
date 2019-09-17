package org.gielinor.game.world.update.flag.player;

import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.world.update.flag.UpdateFlag;
import org.gielinor.game.world.update.flag.context.HitMark;
import org.gielinor.net.packet.PacketBuilder;

/**
 * The supportive hit update flag.
 *
 * @author Emperor
 */
public final class HitUpdateFlag1 extends UpdateFlag<HitMark> {

    /**
     * Constructs a new {@code HitUpdateFlag1} {@code Object}.
     *
     * @param context The hit mark.
     */
    public HitUpdateFlag1(HitMark context) {
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
        packetBuilder.putS(context.getType());
        packetBuilder.put(icon);
        packetBuilder.put(e.getSkills().getLifepoints());
        packetBuilder.putC(e.getSkills().getMaximumLifepoints());
    }

    @Override
    public int data() {
        return maskData();
    }

    @Override
    public int ordinal() {
        return 9;
    }

    /**
     * Gets the mask data.
     *
     * @return The mask data.
     */
    public static int maskData() {
        return 0x200;
    }

}