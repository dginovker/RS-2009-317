package org.gielinor.game.world.update.flag.npc;

import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.world.update.flag.UpdateFlag;
import org.gielinor.game.world.update.flag.context.HitMark;
import org.gielinor.net.packet.PacketBuilder;

/**
 * The NPC's main hit update flag.
 *
 * @author Emperor
 */
public final class NPCHitFlag extends UpdateFlag<HitMark> {

    /**
     * Constructs a new {@code NPCHitFlag} {@code Object}.
     *
     * @param context The hit mark.
     */
    public NPCHitFlag(HitMark context) {
        super(context);
    }

    @Override
    public void write(PacketBuilder packetBuilder) {
        Entity e = context.getEntity();
        double max = e.getSkills().getMaximumLifepoints();
        double calc = e.getSkills().getLifepoints() / max;
        int percentage = (int) (calc * 100);
        if (percentage > 100) {
            percentage = 100;
        }
        packetBuilder.putC((byte) context.getDamage());
        packetBuilder.putS((byte) context.getType());
        int icon = -1;
        if (context.getCombatStyle() != null) {
            switch (context.getCombatStyle()) {
                case MELEE:
                    icon = 0;
                    break;
                case RANGE:
                    icon = 1;//context.getCombatStyle().getSwingHandler().isCannon() ? 4 : 1;
                    break;
                case MAGIC:
                    icon = 2;
                    break;
            }
        }
        packetBuilder.put((byte) icon);
        packetBuilder.putS((byte) percentage);
        packetBuilder.putC((byte) 100);//max);
    }

    @Override
    public int data() {
        return maskData();
    }

    @Override
    public int ordinal() {
        return 5;
    }

    /**
     * Gets the mask data.
     *
     * @return The mask data.
     */
    public static int maskData() {
        return 0x40;
    }

}